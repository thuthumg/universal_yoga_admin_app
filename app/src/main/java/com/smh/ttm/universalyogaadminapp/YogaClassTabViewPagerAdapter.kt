package com.smh.ttm.universalyogaadminapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smh.ttm.universalyogaadminapp.fragments.ClassFragment
import com.smh.ttm.universalyogaadminapp.fragments.CourseFragment

class YogaClassTabViewPagerAdapter (fragment: FragmentActivity): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0-> return CourseFragment()
            1-> return ClassFragment()

        }
        return CourseFragment()

    }

}