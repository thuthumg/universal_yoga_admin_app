package com.smh.ttm.universalyogaadminapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
//
//    private lateinit var viewPager : ViewPager2
//    private lateinit var tabLayout:TabLayout
//    val dummyYogaClass = listOf("Courses","Classes")
//    private lateinit var fabMain: FloatingActionButton
//    private lateinit var fabMenu1: FloatingActionButton
//    private lateinit var fabMenu2: FloatingActionButton
//    private var isFabMenuOpen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Force Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Set up the Toolbar
       // val toolbar: Toolbar = findViewById(R.id.toolbar)
      //  setSupportActionBar(toolbar)
      //  setTitle("")
//          // Get the Action Bar Height
//        val actionBarHeight: Int
//        val styledAttributes = theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
//        actionBarHeight = styledAttributes.getDimensionPixelSize(0, 0)
//        styledAttributes.recycle()
//
//        // Log the Action Bar Height
//        Log.d("ActionBarHeight", "Action Bar Height: $actionBarHeight pixels")
//        // Set Padding to the Main Layout
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top + actionBarHeight, systemBars.right, systemBars.bottom)
//            insets
//        }

//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        viewPager = findViewById(R.id.viewPager)
//        tabLayout = findViewById(R.id.tlForYogaClass)
//
//
//         fabMain = findViewById(R.id.fab_main)
//         fabMenu1 = findViewById(R.id.fab_menu1)
//         fabMenu2 = findViewById(R.id.fab_menu2)
//
//
//        setUpViewPager()
//        setUpTabLayout()
//        setUpClickListener()
    }

//    private fun setUpViewPager() {
//        viewPager.adapter = YogaClassTabViewPagerAdapter(this)
//        viewPager.currentItem = 0
//        viewPager.isUserInputEnabled = false
//    }
//
//    private fun setUpTabLayout() {
//        dummyYogaClass.forEach {
//            tabLayout.newTab().apply {
//                text = it
//                tabLayout.addTab(this)
//            }
//        }
//    }
//    private fun setUpClickListener() {
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                dummyYogaClass.getOrNull(tab?.position ?: 0)?.let {
//                    when(tab?.position)
//                    {
//                        0 -> viewPager.currentItem = 0
//                        1 -> viewPager.currentItem = 2
//                        else -> viewPager.currentItem = 0
//                    }
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//            }
//        })
//
//
//        fabMain.setOnClickListener {
//            if (isFabMenuOpen) {
//                fabMenu1.visibility = View.GONE
//                fabMenu2.visibility = View.GONE
//                isFabMenuOpen = false
//            } else {
//                fabMenu1.visibility = View.VISIBLE
//                fabMenu2.visibility = View.VISIBLE
//                isFabMenuOpen = true
//            }
//        }
//
//        fabMenu1.setOnClickListener {
//            // Handle first mini FAB click (e.g., Edit action)
//        }
//
//        fabMenu2.setOnClickListener {
//            // Handle second mini FAB click (e.g., Share action)
//        }
//    }
}