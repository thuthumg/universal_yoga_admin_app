package com.smh.ttm.universalyogaadminapp.repository

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.smh.ttm.universalyogaadminapp.data.YogaClassInstance
import com.smh.ttm.universalyogaadminapp.data.YogaCourse
import com.smh.ttm.universalyogaadminapp.persistence.daos.YogaClassInstanceDao
import com.smh.ttm.universalyogaadminapp.persistence.daos.YogaCourseDao


import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class YogaRepository(
    private val yogaCourseDao: YogaCourseDao,
    private val yogaClassInstanceDao: YogaClassInstanceDao,
    private val firebaseDb: FirebaseFirestore,
    private val context: Context
) {

    // Function to check internet connectivity
    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    // Insert YogaClass into the Room database

    fun insertCourse(yogaCourse: YogaCourse): Completable {
        return yogaCourseDao.insert(yogaCourse)
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                if (isInternetAvailable()) {
                    syncCourseToFirebase(yogaCourse)
                }

            }
    }

    //updateCourse
    fun updateCourse(yogaCourse: YogaCourse): Completable {
        return yogaCourseDao.update(yogaCourse) // Ensure you have an update method in your DAO
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                if (isInternetAvailable()) {
                    syncCourseToFirebase(yogaCourse)
                }
            }
    }

    //deleteCourse
    fun deleteCourse(yogaCourse: YogaCourse): Completable {
        return yogaCourseDao.delete(yogaCourse) // Ensure you have an update method in your DAO
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                if (isInternetAvailable()) {
                    syncCourseToFirebase(yogaCourse)
                }
            }
    }

    // Insert class instance and sync to Firebase
    fun insertClassInstance(yogaClassInstance: YogaClassInstance): Completable {
        return yogaClassInstanceDao.insert(yogaClassInstance)
            .subscribeOn(Schedulers.io())
            .doOnComplete {

                if (isInternetAvailable()) {
                    syncClassInstanceToFirebase(yogaClassInstance)
                }


            }
    }

    //class instance
    fun updateClassInstance(yogaClassInstance: YogaClassInstance): Completable {
        return yogaClassInstanceDao.update(yogaClassInstance) // Ensure you have an update method in your DAO
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                if (isInternetAvailable()) {
                    syncClassInstanceToFirebase(yogaClassInstance)
                }
            }
    }

    //class instance
    fun deleteClassInstance(yogaClassInstance: YogaClassInstance): Completable {
        return yogaClassInstanceDao.delete(yogaClassInstance) // Ensure you have an update method in your DAO
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                if (isInternetAvailable()) {
                    syncClassInstanceToFirebase(yogaClassInstance)
                }
            }
    }



    // Fetch all courses as an Observable
    fun getAllCourses(): Observable<List<YogaCourse>> {
        return yogaCourseDao.getAllCourses()
            .subscribeOn(Schedulers.io())  // Specify background thread for the operation
    }

    fun getAllCoursesByWeekDay(weekDay:String): Observable<List<YogaCourse>> {
        return yogaCourseDao.getAllCoursesByDayOfTheWeek(weekDay)
            .subscribeOn(Schedulers.io())  // Specify background thread for the operation
    }


    // Fetch all courses as an Observable
    fun getAllClassInstances(): Observable<List<YogaClassInstance>> {
        return yogaClassInstanceDao.getAllClassInstances()
            .subscribeOn(Schedulers.io())  // Specify background thread for the operation
    }

    // Fetch class instances for a specific course as an Observable
    fun getClassInstances(courseId: Int): Observable<List<YogaClassInstance>> {
        return yogaClassInstanceDao.getClassInstances(courseId)
            .subscribeOn(Schedulers.io())  // Ensure the operation runs on a background thread
    }

    // Sync course to Firebase
    private fun syncCourseToFirebase(yogaCourse: YogaCourse) {
        firebaseDb.collection("yoga_courses")
            .document(yogaCourse.id.toString())
            .set(yogaCourse)
            .addOnSuccessListener {
                Log.d("repository", "Sync course to Firebase successfully ")
            }
            .addOnFailureListener {
                Log.d("repository", "Sync course to Firebase fail ")
            }
    }

    // Sync class instance to Firebase
    private fun syncClassInstanceToFirebase(yogaClassInstance: YogaClassInstance) {
        firebaseDb.collection("yoga_class_instances")
            .document(yogaClassInstance.id.toString())
            .set(yogaClassInstance)
            .addOnSuccessListener {
                Log.d("repository", "Sync course to Firebase successfully ")
            }
            .addOnFailureListener {
                Log.d("repository", "Sync course to Firebase fail ")
            }
    }

    // Sync from Firebase to Room
    fun syncCoursesFromFirebase() {
        firebaseDb.collection("yoga_courses").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val yogaCourse = document.toObject(YogaCourse::class.java)
                    insertCourse(yogaCourse).subscribe()
                }
            }
    }

    fun syncClassInstancesFromFirebase() {
        firebaseDb.collection("yoga_class_instances").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val yogaClassInstance = document.toObject(YogaClassInstance::class.java)
                    insertClassInstance(yogaClassInstance).subscribe()
                }
            }
    }
    // Sync all courses from Firebase to Room
    /* fun syncCoursesFromFirebase(): Completable {
         return Completable.create { emitter ->
             firebaseDb.collection("yoga_courses").get()
                 .addOnSuccessListener { result ->
                     for (document in result) {
                         val yogaCourse = document.toObject(YogaCourse::class.java)
                         insertCourse(yogaCourse).subscribe()
                     }
                     emitter.onComplete()
                 }
                 .addOnFailureListener { e ->
                     emitter.onError(e)
                 }
         }.subscribeOn(Schedulers.io())
     }

     // Sync all class instances from Firebase to Room
     fun syncClassInstancesFromFirebase(): Completable {
         return Completable.create { emitter ->
             firebaseDb.collection("yoga_class_instances").get()
                 .addOnSuccessListener { result ->
                     for (document in result) {
                         val yogaClassInstance = document.toObject(YogaClassInstance::class.java)
                         insertClassInstance(yogaClassInstance).subscribe()
                     }
                     emitter.onComplete()
                 }
                 .addOnFailureListener { e ->
                     emitter.onError(e)
                 }
         }.subscribeOn(Schedulers.io())
     }*/

    fun syncCoursesFromRoomToFirebase() {
        getAllCourses()  // Fetch courses from Room
            .subscribe { courses ->
                for (course in courses) {
                    syncCourseToFirebase(course)  // Sync each course to Firebase
                }
            }
    }

    fun syncClassInstancesFromRoomToFirebase() {
        getAllClassInstances()  // Fetch courses from Room
            .subscribe { classInstances ->
                for (classInstance in classInstances) {
                    syncClassInstanceToFirebase(classInstance)  // Sync each course to Firebase
                }
            }
    }

    //SearchByTeacher
    fun searchByTeacher(teacher:String): Observable<List<YogaClassInstance>> {
        return yogaClassInstanceDao.searchByTeacher(teacher)
            .subscribeOn(Schedulers.io())  // Specify background thread for the operation
    }

    //SearchByDate
    fun searchByDate(date:String): Observable<List<YogaClassInstance>> {
        return yogaClassInstanceDao.searchByDate(date)
            .subscribeOn(Schedulers.io())  // Specify background thread for the operation
    }

}

