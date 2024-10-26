package com.smh.ttm.universalyogaadminapp.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.smh.ttm.universalyogaadminapp.data.YogaCourse
import com.smh.ttm.universalyogaadminapp.databinding.ViewHolderItemBinding
import com.smh.ttm.universalyogaadminapp.delegates.CourseItemDelegate

class ViewHolder(private val binding: ViewHolderItemBinding, onTapCourseDelegate: CourseItemDelegate) : RecyclerView.ViewHolder(binding.root) {
    lateinit var mCourseData:YogaCourse
    init {
        binding.root.setOnClickListener {
            onTapCourseDelegate.onTapCourseItem(mCourseData)
         }
    }
    fun bindData(item: YogaCourse) {
        mCourseData = item

        binding.tvItemName.text = item.type
        binding.tvItemTime.text = "${item.time} (${item.duration} minutes)"
        binding.tvItemCapacity.text = item.capacity.toString()
        binding.tvItemDayOfWeek.text = item.dayOfWeek


    }
}
