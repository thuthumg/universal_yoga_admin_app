package com.smh.ttm.universalyogaadminapp.mvvm

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.smh.ttm.universalyogaadminapp.data.YogaClassInstance
import com.smh.ttm.universalyogaadminapp.data.YogaCourse
import com.smh.ttm.universalyogaadminapp.persistence.YogaDatabase
import com.smh.ttm.universalyogaadminapp.repository.YogaRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class YogaClassInstanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: YogaRepository

    private val _allClassInstances = MutableLiveData<Resource<List<YogaClassInstance>>>()
    val allClassInstances: LiveData<Resource<List<YogaClassInstance>>> get() = _allClassInstances


    private val _insertClassInstanceStatus = MutableLiveData<Resource<YogaClassInstance>>()
    val insertClassInstanceStatus: LiveData<Resource<YogaClassInstance>> get() = _insertClassInstanceStatus

    private val _updateClassInstanceStatus = MutableLiveData<Resource<YogaClassInstance>>()
    val updateClassInstanceStatus: LiveData<Resource<YogaClassInstance>> get() = _updateClassInstanceStatus


    private val _deleteClassInstanceStatus = MutableLiveData<Resource<YogaClassInstance>>()
    val deleteClassInstanceStatus: LiveData<Resource<YogaClassInstance>> get() = _deleteClassInstanceStatus
    private val searchSubject = PublishSubject.create<String>()

    init {
        val yogaCourseDao = YogaDatabase.getDatabase(application).yogaCourseDao()
        val yogaClassInstanceDao = YogaDatabase.getDatabase(application).yogaClassInstanceDao()
        val firebaseDb = FirebaseFirestore.getInstance()

        repository = YogaRepository(yogaCourseDao, yogaClassInstanceDao, firebaseDb,application)

        loadClassInstances()

//        searchSubject
//            .debounce(500, TimeUnit.MILLISECONDS) // Adjust debounce time as needed
//            .filter { it.isNotEmpty() } // Optional: Ignore empty queries
//            .switchMap { teacher ->
//                repository.searchByTeacher(teacher)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .onErrorReturn { emptyList() } // Handle errors gracefully
//            }
//            .subscribe({ classInstances ->
//                _allClassInstances.value = Resource.Success(classInstances) // Emit results
//            }, { error ->
//                _allClassInstances.value = Resource.Error("Failed to search: ${error.message}") // Emit error state
//            })
    }
    fun loadClassInstances() {
        _allClassInstances.value = Resource.Loading()  // Emit loading state

        repository.getAllClassInstances()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ classInstances ->
                _allClassInstances.value = Resource.Success(classInstances)  // Emit success state
            }, { error ->
                _allClassInstances.value = Resource.Error("Failed to load courses: ${error.message}")  // Emit error state
            })
    }
    //SearchByTeacher
    fun searchByTeacher(teacher:String) {
//        _allClassInstances.value = Resource.Loading()  // Emit loading state
//
//        repository.searchByTeacher(teacher)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ classInstances ->
//                _allClassInstances.value = Resource.Success(classInstances)  // Emit success state
//            }, { error ->
//                _allClassInstances.value = Resource.Error("Failed to load courses: ${error.message}")  // Emit error state
//            })
        searchSubject
            .debounce(500, TimeUnit.MILLISECONDS) // Adjust debounce time as needed
            .filter { it.isNotEmpty() } // Optional: Ignore empty queries
            .switchMap { teacher ->
                repository.searchByTeacher(teacher)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn { emptyList() } // Handle errors gracefully
            }
            .subscribe({ classInstances ->
                _allClassInstances.value = Resource.Success(classInstances) // Emit results
            }, { error ->
                _allClassInstances.value = Resource.Error("Failed to search: ${error.message}") // Emit error state
            })

        searchSubject.onNext(teacher) // Emit the search query
    }

    //SearchByDate
    fun searchByDate(searchDate:String) {
        _allClassInstances.value = Resource.Loading()  // Emit loading state

        repository.searchByDate(searchDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ classInstances ->
                _allClassInstances.value = Resource.Success(classInstances)  // Emit success state
            }, { error ->
                _allClassInstances.value = Resource.Error("Failed to load courses: ${error.message}")  // Emit error state
            })
    }

    // Insert a new class instance with Resource handling
    fun insertClassInstance(yogaClassInstance: YogaClassInstance) {

        _insertClassInstanceStatus.value = Resource.Loading()  // Emit loading state


        repository.insertClassInstance(yogaClassInstance)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Handle success if needed, e.g., notify UI
                _insertClassInstanceStatus.value = Resource.Success(yogaClassInstance)

                Log.d("YogaClassViewModel", "Yoga class instance inserted successfully!")
                Toast.makeText(getApplication(), "Yoga class instance inserted successfully", Toast.LENGTH_SHORT).show()

            }, { error ->
                _insertClassInstanceStatus.value = Resource.Error("Error inserting yoga course: ${error.message}")  // Emit error state
                Log.e("YogaCourseViewModel", "Error inserting yoga course: ${error.message}")
                Toast.makeText(getApplication(), "Failed to insert yoga course", Toast.LENGTH_SHORT).show()

            })
    }


    fun updateClassInstance(yogaClassInstance: YogaClassInstance) {
        _updateClassInstanceStatus.value = Resource.Loading()  // Emit loading state

        repository.updateClassInstance(yogaClassInstance)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _updateClassInstanceStatus.value = Resource.Success(yogaClassInstance)  // Emit success state
                Log.d("YogaCourseViewModel", "Yoga course updated successfully!")
                Toast.makeText(getApplication(), "Yoga course updated successfully", Toast.LENGTH_SHORT).show()
            }, { error ->
                _updateClassInstanceStatus.value = Resource.Error("Error updating yoga course: ${error.message}")  // Emit error state
                Log.e("YogaCourseViewModel", "Error updating yoga course: ${error.message}")
                Toast.makeText(getApplication(), "Failed to update yoga course", Toast.LENGTH_SHORT).show()
            })
    }
    fun deleteClassInstance(yogaClassInstance: YogaClassInstance) {
        _deleteClassInstanceStatus.value = Resource.Loading()  // Emit loading state

        repository.deleteClassInstance(yogaClassInstance)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _deleteClassInstanceStatus.value = Resource.Success(yogaClassInstance)  // Emit success state
                Log.d("YogaCourseViewModel", "Yoga course updated successfully!")
                Toast.makeText(getApplication(), "Yoga course updated successfully", Toast.LENGTH_SHORT).show()
            }, { error ->
                _deleteClassInstanceStatus.value = Resource.Error("Error updating yoga course: ${error.message}")  // Emit error state
                Log.e("YogaCourseViewModel", "Error updating yoga course: ${error.message}")
                Toast.makeText(getApplication(), "Failed to update yoga course", Toast.LENGTH_SHORT).show()
            })
    }

    // Fetch class instances for a specific course with Resource handling
/*    fun getClassInstances(courseId: Int) {
        _allClassInstances.value = Resource.Loading()  // Emit loading state

        repository.getClassInstances(courseId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ classInstances ->
                _allClassInstances.value = Resource.Success(classInstances)  // Emit success state
            }, { error ->
                _allClassInstances.value = Resource.Error("Failed to load class instances: ${error.message}")  // Emit error state
            })
    }*/



    // Sync class instances from Firebase to Room with Resource handling
//    fun syncClassInstancesFromFirebase() {
//        _allClassInstances.value = Resource.Loading()  // Emit loading state
//
//        repository.syncClassInstancesFromFirebase()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                // Handle sync completed: emit success or handle as needed
//            }, { error ->
//                _allClassInstances.value = Resource.Error("Failed to sync class instances: ${error.message}")  // Emit error state
//            })
//    }
}

//class YogaClassInstanceViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val repository: YogaRepository
//    private val _allClassInstances = MutableLiveData<List<YogaClassInstance>>()
//    val allClassInstances: LiveData<List<YogaClassInstance>> get() = _allClassInstances
//
//    init {
//        val yogaCourseDao = YogaDatabase.getDatabase(application).yogaCourseDao()
//        val yogaClassInstanceDao = YogaDatabase.getDatabase(application).yogaClassInstanceDao()
//        val firebaseDb = FirebaseFirestore.getInstance()
//
//        repository = YogaRepository(yogaCourseDao, yogaClassInstanceDao, firebaseDb)
//    }
//
//    // Fetch class instances for a specific course
//    fun getClassInstances(courseId: Int) {
//        repository.getClassInstances(courseId)  // Assuming this returns Observable<List<YogaClassInstance>>
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({ classInstances ->
//                _allClassInstances.value = classInstances
//            }, { error ->
//                // Handle error
//                error.printStackTrace()
//            })
//    }
//
//    // Insert a new class instance
//    fun insertClassInstance(yogaClassInstance: YogaClassInstance) {
//        repository.insertClassInstance(yogaClassInstance)
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                // Handle success, e.g., log success or update UI
//            }, { error ->
//                // Handle error
//                error.printStackTrace()
//            })
//    }
//
//    // Sync class instances from Firebase to Room
//    fun syncClassInstancesFromFirebase() {
//        repository.syncClassInstancesFromFirebase()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                // Sync completed
//            }, { error ->
//                // Handle error
//                error.printStackTrace()
//            })
//    }
//}

