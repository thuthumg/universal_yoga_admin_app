package com.smh.ttm.universalyogaadminapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smh.ttm.universalyogaadminapp.data.YogaCourse
import com.smh.ttm.universalyogaadminapp.databinding.ViewHolderItemBinding
import com.smh.ttm.universalyogaadminapp.delegates.CourseItemDelegate
import com.smh.ttm.universalyogaadminapp.viewholders.ViewHolder

class YogaCourseItemAdapter(private var onTapCourseItem: CourseItemDelegate) : RecyclerView.Adapter<ViewHolder>() {
    private var mItemList: List<YogaCourse> = listOf()
    lateinit var binding: ViewHolderItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ViewHolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding,onTapCourseItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mItemList.isNotEmpty()) {
            holder.bindData(mItemList[position])
        }
    }

    override fun getItemCount(): Int = mItemList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItemData(items: List<YogaCourse>) {
        mItemList = items
        notifyDataSetChanged()
    }

}
