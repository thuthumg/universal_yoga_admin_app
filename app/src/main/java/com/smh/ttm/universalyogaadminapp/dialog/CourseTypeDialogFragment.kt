package com.smh.ttm.universalyogaadminapp.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.smh.ttm.universalyogaadminapp.data.YogaCourse


class CourseTypeDialogFragment : DialogFragment() {

    private lateinit var yogaCourses: List<YogaCourse>
    private var listener: ((YogaCourse) -> Unit)? = null

    companion object {
        fun newInstance(yogaCourses: List<YogaCourse>) = CourseTypeDialogFragment().apply {
            arguments = Bundle().apply {
                putSerializable("YOGA_COURSES", ArrayList(yogaCourses)) // Cast to ArrayList for serialization
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNCHECKED_CAST")
        yogaCourses = arguments?.getSerializable("YOGA_COURSES") as? List<YogaCourse> ?: emptyList()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val courseNames = yogaCourses.map { it.courseName }.toTypedArray()

        return AlertDialog.Builder(requireContext())
            .setTitle("Select Yoga Course")
            .setItems(courseNames) { _, which ->
                listener?.invoke(yogaCourses[which])
            }
            .create()
    }

    fun setOnCourseSelectedListener(listener: (YogaCourse) -> Unit) {
        this.listener = listener
    }
}

