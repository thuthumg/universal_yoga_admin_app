package com.smh.ttm.universalyogaadminapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

//@Entity(
//    tableName = "yoga_class_instance_table",
//    foreignKeys = [ForeignKey(
//        entity = YogaCourse::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("courseId"),
//        onDelete = ForeignKey.CASCADE
//    )]
//)
//data class YogaClassInstance(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val courseId: Int, // Foreign key from YogaCourse
//    val date: Long, // Timestamp for class date, e.g., 17/10/2023
//    val teacher: String, // Teacher for this class instance
//    val additionalComments: String? = null // Optional
//)

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

