package com.smh.ttm.universalyogaadminapp.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.smh.ttm.universalyogaadminapp.R
import com.smh.ttm.universalyogaadminapp.data.YogaCourse
import com.smh.ttm.universalyogaadminapp.databinding.ViewHolderItemBinding
import com.smh.ttm.universalyogaadminapp.delegates.CourseItemDelegate
import com.smh.ttm.universalyogaadminapp.dummy.classTypes

class ViewHolder(private val binding: ViewHolderItemBinding, onTapCourseDelegate: CourseItemDelegate) : RecyclerView.ViewHolder(binding.root) {
    lateinit var mCourseData:YogaCourse
    init {
        binding.root.setOnClickListener {
            onTapCourseDelegate.onTapCourseItem(mCourseData)
         }
    }
    fun bindData(item: YogaCourse) {
        mCourseData = item
        when(item.type)
        {
            classTypes[0]->{
                binding.ivItemImg.setImageResource(R.drawable.flow_yoga)
            }
            classTypes[1] -> {
                binding.ivItemImg.setImageResource(R.drawable.arial_yoga)
            }
            classTypes[2] -> {
                binding.ivItemImg.setImageResource(R.drawable.family_yoga)
            }
        }
     //   binding.ivItemImg.setImageResource(R.drawable.default_yoga_img)
        binding.tvItemName.text = item.type
        binding.tvItemTime.text = "${item.time} (${item.duration} minutes)"
        binding.tvItemCapacity.text = item.capacity.toString()
        binding.tvItemDayOfWeek.text = item.dayOfWeek


    }
}
