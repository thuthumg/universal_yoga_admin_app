package com.smh.ttm.universalyogaadminapp.viewpods

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smh.ttm.universalyogaadminapp.adapters.YogaClassItemAdapter
import com.smh.ttm.universalyogaadminapp.data.YogaClassInstance
import com.smh.ttm.universalyogaadminapp.databinding.ViewPodClassItemListBinding
import com.smh.ttm.universalyogaadminapp.delegates.YogaClassItemDelegate

class YogaClassListViewPod @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {
    private  var binding: ViewPodClassItemListBinding
    lateinit var mYogaClassItemAdapter: YogaClassItemAdapter
    lateinit var mDelegate: YogaClassItemDelegate

    init {
        // Inflate the layout for the view pod
        binding = ViewPodClassItemListBinding.inflate(LayoutInflater.from(context), this, true)

    }

    private fun setDelegate(delegate: YogaClassItemDelegate){
        this.mDelegate = delegate
    }

    private fun setupItemRecyclerView(delegate: YogaClassItemDelegate) {
        mYogaClassItemAdapter =YogaClassItemAdapter(delegate)
        binding.rvClassItemList.adapter = mYogaClassItemAdapter
        // binding.rvItemList.layoutManager = GridLayoutManager(context,2)
        binding.rvClassItemList.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        binding.rvClassItemList.isNestedScrollingEnabled = false

    }
    fun setData(delegate: YogaClassItemDelegate,itemList: List<YogaClassInstance>){
        setDelegate(delegate)
        setupItemRecyclerView(mDelegate)
        mYogaClassItemAdapter.setItemData(itemList)
    }

}