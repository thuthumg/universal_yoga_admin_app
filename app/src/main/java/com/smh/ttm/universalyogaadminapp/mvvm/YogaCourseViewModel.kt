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

class YogaCourseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: YogaRepository
    private val _allCourses = MutableLiveData<Resource<List<YogaCourse>>>()
    val allCourses: LiveData<Resource<List<YogaCourse>>> get() = _allCourses

    private val _allCoursesByWeekDay = MutableLiveData<Resource<List<YogaCourse>>>()
    val allCoursesByWeekDay: LiveData<Resource<List<YogaCourse>>> get() = _allCoursesByWeekDay

    private val _insertCourseStatus = MutableLiveData<Resource<YogaCourse>>()
    val insertCourseStatus: LiveData<Resource<YogaCourse>> get() = _insertCourseStatus

    private val _updateCourseStatus = MutableLiveData<Resource<YogaCourse>>()
    val updateCourseStatus: LiveData<Resource<YogaCourse>> get() = _updateCourseStatus


    private val _deleteCourseStatus = MutableLiveData<Resource<YogaCourse>>()
    val deleteCourseStatus: LiveData<Resource<YogaCourse>> get() = _deleteCourseStatus


    init {
        val yogaCourseDao = YogaDatabase.getDatabase(application).yogaCourseDao()
        val yogaClassInstanceDao = YogaDatabase.getDatabase(application).yogaClassInstanceDao()
        val firebaseDb = FirebaseFirestore.getInstance()

        repository = YogaRepository(yogaCourseDao, yogaClassInstanceDao, firebaseDb,application)

        // Load courses from the repository when ViewModel is initialized
        loadCourses()
    }
    fun loadCoursesByWeekDay(weekday: String) {
        _allCoursesByWeekDay.value = Resource.Loading()  // Emit loading state

        repository.getAllCoursesByWeekDay(weekday)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ courses ->
                _allCoursesByWeekDay.value = Resource.Success(courses)  // Emit success state
            }, { error ->
                _allCoursesByWeekDay.value = Resource.Error("Failed to load courses: ${error.message}")  // Emit error state
            })
    }
     fun loadCourses() {
        _allCourses.value = Resource.Loading()  // Emit loading state

        repository.getAllCourses()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ courses ->
                _allCourses.value = Resource.Success(courses)  // Emit success state
            }, { error ->
                _allCourses.value = Resource.Error("Failed to load courses: ${error.message}")  // Emit error state
            })
    }

    // Insert a new yoga course into the Room database with success and failure handling
    fun insertCourse(yogaCourse: YogaCourse) {
        _insertCourseStatus.value = Resource.Loading()  // Emit loading state

        repository.insertCourse(yogaCourse)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _insertCourseStatus.value = Resource.Success(yogaCourse)  // Emit success state
                Log.d("YogaCourseViewModel", "Yoga course inserted successfully!")
                Toast.makeText(getApplication(), "Yoga course inserted successfully", Toast.LENGTH_SHORT).show()
            }, { error ->
                _insertCourseStatus.value = Resource.Error("Error inserting yoga course: ${error.message}")  // Emit error state
                Log.e("YogaCourseViewModel", "Error inserting yoga course: ${error.message}")
                Toast.makeText(getApplication(), "Failed to insert yoga course", Toast.LENGTH_SHORT).show()
            })
    }
    fun updateCourse(yogaCourse: YogaCourse) {
        _updateCourseStatus.value = Resource.Loading()  // Emit loading state

        repository.updateCourse(yogaCourse)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _updateCourseStatus.value = Resource.Success(yogaCourse)  // Emit success state
                Log.d("YogaCourseViewModel", "Yoga course updated successfully!")
                Toast.makeText(getApplication(), "Yoga course updated successfully", Toast.LENGTH_SHORT).show()
            }, { error ->
                _updateCourseStatus.value = Resource.Error("Error updating yoga course: ${error.message}")  // Emit error state
                Log.e("YogaCourseViewModel", "Error updating yoga course: ${error.message}")
                Toast.makeText(getApplication(), "Failed to update yoga course", Toast.LENGTH_SHORT).show()
            })
    }


    fun deleteCourse(yogaCourse: YogaCourse) {
        _deleteCourseStatus.value = Resource.Loading()  // Emit loading state

        repository.deleteCourse(yogaCourse)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _deleteCourseStatus.value = Resource.Success(yogaCourse)  // Emit success state
                Log.d("YogaCourseViewModel", "Yoga course updated successfully!")
                Toast.makeText(getApplication(), "Yoga course updated successfully", Toast.LENGTH_SHORT).show()
            }, { error ->
                _deleteCourseStatus.value = Resource.Error("Error updating yoga course: ${error.message}")  // Emit error state
                Log.e("YogaCourseViewModel", "Error updating yoga course: ${error.message}")
                Toast.makeText(getApplication(), "Failed to update yoga course", Toast.LENGTH_SHORT).show()
            })
    }

    fun searchByCourseType(courseType: String): Observable<List<YogaCourse>> {
        return repository.searchByCourseType(courseType)
            .onErrorResumeNext { _: Throwable -> Observable.just(emptyList()) } // Specify lambda to resolve overload
            .subscribeOn(Schedulers.io())
    }
}
