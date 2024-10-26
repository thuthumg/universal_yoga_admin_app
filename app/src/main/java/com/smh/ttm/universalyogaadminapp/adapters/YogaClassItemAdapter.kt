package com.smh.ttm.universalyogaadminapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smh.ttm.universalyogaadminapp.data.YogaClassInstance
import com.smh.ttm.universalyogaadminapp.databinding.ViewHolderClassItemBinding
import com.smh.ttm.universalyogaadminapp.delegates.YogaClassItemDelegate
import com.smh.ttm.universalyogaadminapp.viewholders.YogaClassViewHolder

class YogaClassItemAdapter(var onTapClassItemDelegate: YogaClassItemDelegate) : RecyclerView.Adapter<YogaClassViewHolder>() {
    private var mItemList: List<YogaClassInstance> = listOf()
    lateinit var binding: ViewHolderClassItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YogaClassViewHolder {
        binding = ViewHolderClassItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YogaClassViewHolder(binding,onTapClassItemDelegate)
    }

    override fun onBindViewHolder(holder: YogaClassViewHolder, position: Int) {
        if (mItemList.isNotEmpty()) {
            holder.bindData(mItemList[position])
        }
    }

    override fun getItemCount(): Int = mItemList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItemData(items: List<YogaClassInstance>) {
        mItemList = items
        notifyDataSetChanged()
    }

}
