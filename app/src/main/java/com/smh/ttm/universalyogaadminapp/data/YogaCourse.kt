package com.smh.ttm.universalyogaadminapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "yoga_course_table")
data class YogaCourse(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var courseId: Long,
    var courseName: String,
    var dayOfWeek: String, // e.g. Monday
    var time: String,      // e.g. 10:00
    var capacity: Int,     // Number of attendees
    var duration: Int,     // Duration in minutes
    var price: String,     // Price per class
    var type: String,      // Type of class (Flow Yoga, Aerial Yoga, etc.)
    var description: String? = null // Optional description
) : Serializable
