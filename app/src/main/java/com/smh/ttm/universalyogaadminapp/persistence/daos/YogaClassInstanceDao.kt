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
}
