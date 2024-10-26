package com.smh.ttm.universalyogaadminapp.persistence

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.smh.ttm.universalyogaadminapp.data.YogaClassInstance
import com.smh.ttm.universalyogaadminapp.data.YogaCourse
import com.smh.ttm.universalyogaadminapp.persistence.daos.YogaClassInstanceDao
import com.smh.ttm.universalyogaadminapp.persistence.daos.YogaCourseDao

@Database(entities = [YogaCourse::class, YogaClassInstance::class], version = 1, exportSchema = false)
abstract class YogaDatabase : RoomDatabase() {

    abstract fun yogaCourseDao(): YogaCourseDao
    abstract fun yogaClassInstanceDao(): YogaClassInstanceDao

    companion object {
        @Volatile
        private var INSTANCE: YogaDatabase? = null

        fun getDatabase(context: Context): YogaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YogaDatabase::class.java,
                    "yoga_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
