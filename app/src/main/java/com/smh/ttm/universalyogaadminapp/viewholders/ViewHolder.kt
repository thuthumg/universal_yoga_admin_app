package com.smh.ttm.universalyogaadminapp.viewholders

import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.smh.ttm.universalyogaadminapp.R
import com.smh.ttm.universalyogaadminapp.data.YogaCourse
import com.smh.ttm.universalyogaadminapp.databinding.ViewHolderItemBinding
import com.smh.ttm.universalyogaadminapp.delegates.CourseItemDelegate
import com.smh.ttm.universalyogaadminapp.dummy.classTypes
import com.bumptech.glide.Glide

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

        // Load the image using the imageUri, fallback to default drawable if imageUri is null or empty
        if (!item.imageUri.isNullOrEmpty()) {
           // Log.d("courselist","${item.imageUri}")
            Glide.with(binding.root.context)
                .load(Uri.parse(item.imageUri))
                .into(binding.ivItemImg)
        } else {
            binding.ivItemImg.setImageResource(R.drawable.default_yoga_img)
        }
    }
}
