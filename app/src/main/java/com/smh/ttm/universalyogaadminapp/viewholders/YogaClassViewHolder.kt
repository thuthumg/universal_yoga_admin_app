package com.smh.ttm.universalyogaadminapp.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.smh.ttm.universalyogaadminapp.data.YogaClassInstance
import com.smh.ttm.universalyogaadminapp.databinding.ViewHolderClassItemBinding
import com.smh.ttm.universalyogaadminapp.delegates.YogaClassItemDelegate

class YogaClassViewHolder(private val binding: ViewHolderClassItemBinding, onTapClassItemDelegate: YogaClassItemDelegate) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var mYogaClassInstance:YogaClassInstance
    init {
        binding.root.setOnClickListener {
            onTapClassItemDelegate.onTapClassItem(mYogaClassInstance)
         }
    }
    fun bindData(item: YogaClassInstance) {
        mYogaClassInstance = item
        binding.tvClassName.text = item.courseName
        binding.tvInstructorName.text = item.teacher
        binding.tvClassDate.text = item.date

    }
}
