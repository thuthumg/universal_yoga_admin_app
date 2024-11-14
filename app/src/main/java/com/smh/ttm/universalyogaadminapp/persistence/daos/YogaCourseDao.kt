package com.smh.ttm.universalyogaadminapp.persistence.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.smh.ttm.universalyogaadminapp.data.YogaClassInstance
import com.smh.ttm.universalyogaadminapp.data.YogaCourse


import io.reactivex.Completable

import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface YogaCourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(yogaCourse: YogaCourse): Completable


    @Query("SELECT * FROM yoga_course_table")
    fun getAllCourses(): Observable<List<YogaCourse>>

    @Query("SELECT * FROM yoga_course_table  WHERE dayOfWeek = :weekDay ")
    fun getAllCoursesByDayOfTheWeek(weekDay: String): Observable<List<YogaCourse>>

    // Search by teacher
    @Query("SELECT * FROM yoga_course_table WHERE type LIKE '%' || :courseType || '%'")
    fun searchByCourseType(courseType: String): Observable<List<YogaCourse>>

    // Delete a course with Completable
    @Delete
    fun delete(yogaCourse: YogaCourse): Completable


    @Update
    fun update(yogaCourse: YogaCourse): Completable


}
