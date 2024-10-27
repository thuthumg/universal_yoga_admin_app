package com.smh.ttm.universalyogaadminapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "yoga_class_instance_table")
data class YogaClassInstance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var classId: Long,
    var className:String,
    var courseId: Int, // Foreign key referencing YogaCourse
    var courseName: String,
    var date: String,  // e.g. 17/10/2023
    var teacher: String,
    var additionalComments: String? = null // Optional comments
): Serializable

