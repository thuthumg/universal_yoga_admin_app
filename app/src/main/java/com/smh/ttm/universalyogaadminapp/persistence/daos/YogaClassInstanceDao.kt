package com.smh.ttm.universalyogaadminapp.persistence.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.smh.ttm.universalyogaadminapp.data.YogaClassInstance
import com.smh.ttm.universalyogaadminapp.data.YogaCourse

import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface YogaClassInstanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(yogaClassInstance: YogaClassInstance): Completable


    @Query("SELECT * FROM yoga_class_instance_table")
    fun getAllClassInstances(): Observable<List<YogaClassInstance>>


    @Query("SELECT * FROM yoga_class_instance_table WHERE courseId = :courseId")
    fun getClassInstances(courseId: Int): Observable<List<YogaClassInstance>>

    @Delete
    fun delete(yogaClassInstance: YogaClassInstance): Completable


    @Update
    fun update(yogaClassInstance: YogaClassInstance): Completable

    // Search by teacher
    @Query("SELECT * FROM yoga_class_instance_table WHERE teacher LIKE '%' || :teacher || '%'")
    fun searchByTeacher(teacher: String): Observable<List<YogaClassInstance>>

//    // Search by date
//    @Query("SELECT * FROM yoga_class_instance_table WHERE date = :date")
//    fun searchByDate(date: String): Observable<List<YogaClassInstance>>

    // Search by date range
    @Query("SELECT * FROM yoga_class_instance_table WHERE date BETWEEN :startDate AND :endDate")
    fun searchByDateRange(startDate: String, endDate: String): Observable<List<YogaClassInstance>>

    // Combined search
    @Query("""
        SELECT * FROM yoga_class_instance_table 
        WHERE (:teacher IS NULL OR teacher LIKE '%' || :teacher || '%') 
        AND (:date IS NULL OR date = :date)
    """)
    fun search(
        teacher: String? = null,
        date: String? = null
    ): Observable<List<YogaClassInstance>>
}
