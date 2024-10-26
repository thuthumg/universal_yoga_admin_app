package com.smh.ttm.universalyogaadminapp.viewpods

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smh.ttm.universalyogaadminapp.adapters.ItemAdapter
import com.smh.ttm.universalyogaadminapp.data.YogaCourse
import com.smh.ttm.universalyogaadminapp.databinding.ViewPodItemListBinding
import com.smh.ttm.universalyogaadminapp.delegates.CourseItemDelegate

class ItemListViewPod @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {
    private  var binding: ViewPodItemListBinding
    lateinit var mItemAdapter: ItemAdapter
    lateinit var mDelegate: CourseItemDelegate

//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        // Inflate the ViewBinding for this view
//        binding = ViewPodItemListBinding.bind(this)
//
//        setupItemRecyclerView()
//    }

    init {
        // Inflate the layout for the view pod
        binding = ViewPodItemListBinding.inflate(LayoutInflater.from(context), this, true)

    }

    private fun setDelegate(delegate: CourseItemDelegate){
        this.mDelegate = delegate
    }

    private fun setupItemRecyclerView(delegate: CourseItemDelegate) {
        mItemAdapter = ItemAdapter(delegate)
        binding.rvItemList.adapter = mItemAdapter
        // binding.rvItemList.layoutManager = GridLayoutManager(context,2)
        binding.rvItemList.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        binding.rvItemList.isNestedScrollingEnabled = false

    }
    fun setData(delegate: CourseItemDelegate,itemList: List<YogaCourse>){
        setDelegate(delegate)
        setupItemRecyclerView(mDelegate)
        mItemAdapter.setItemData(itemList)
    }
   /* fun setupItemViewPod(backgroundColorReference: Int, titleText: String, description:String) {
        mItemAdapter.binding.cvItem.setBackgroundColor(ContextCompat.getColor(context, backgroundColorReference))
        mItemAdapter.binding.tvItemName.text = titleText
        mItemAdapter.binding.tvItemDescription.text = description
    }*/
}