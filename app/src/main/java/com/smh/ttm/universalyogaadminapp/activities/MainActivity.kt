package com.smh.ttm.universalyogaadminapp.activities


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.smh.ttm.universalyogaadminapp.R
import com.smh.ttm.universalyogaadminapp.data.YogaClassInstance
import com.smh.ttm.universalyogaadminapp.data.YogaCourse
import com.smh.ttm.universalyogaadminapp.databinding.ActivityMainBinding
import com.smh.ttm.universalyogaadminapp.delegates.CourseItemDelegate
import com.smh.ttm.universalyogaadminapp.delegates.YogaClassItemDelegate
import com.smh.ttm.universalyogaadminapp.mvvm.Resource
import com.smh.ttm.universalyogaadminapp.mvvm.YogaClassInstanceViewModel
import com.smh.ttm.universalyogaadminapp.mvvm.YogaCourseViewModel
import com.smh.ttm.universalyogaadminapp.viewpods.ItemListViewPod
import com.smh.ttm.universalyogaadminapp.viewpods.YogaClassListViewPod


class MainActivity : AppCompatActivity(),CourseItemDelegate,YogaClassItemDelegate {

    //View Pods
    private lateinit var courseItemListViewPod: ItemListViewPod
    private lateinit var classItemListViewPod: YogaClassListViewPod
    private lateinit var binding: ActivityMainBinding
    private val yogaCourseViewModel: YogaCourseViewModel by viewModels()
    private val yogaClassInstanceViewModel: YogaClassInstanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setContentView(R.layout.activity_main)

        // Force Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setUpViewPods()
        setUpListListener()

        // Observe data
        //Observe the class instances LiveData and update UI
        yogaClassInstanceViewModel.allClassInstances.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator if necessary

                }

                is Resource.Success -> {
                    // Handle success, e.g., update UI or show a message


                    resource.data?.let {
                        classItemListViewPod.setData(this,itemList = it)

                    }

                }

                is Resource.Error -> {
                    // Handle error, e.g., show an error message
                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }



        yogaCourseViewModel.allCourses.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator if necessary

                }

                is Resource.Success -> {
                    // Handle success, e.g., update UI or show a message


                    resource.data?.let {
                        courseItemListViewPod.setData(this,itemList = it)

                    }

                }

                is Resource.Error -> {
                    // Handle error, e.g., show an error message
                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    // Sync from Firebase
    //  yogaCourseViewModel.syncCoursesFromFirebase()



    // Sync from Firebase
    //  yogaClassInstanceViewModel.syncClassInstancesFromFirebase()


    private fun setUpListListener() {
        binding.linearLayout1.setOnClickListener {
            val intent = Intent(this, CourseItemListActivity::class.java)
            startActivity(intent)
        }

        binding.linearLayout2.setOnClickListener {

            val intent = Intent(this, ClassItemListActivity::class.java)
            startActivity(intent)
        }

        binding.tvClassesSeeAll.setOnClickListener {

            val intent = Intent(this, ClassItemListActivity::class.java)
            startActivity(intent)
        }
        binding.tvCoursesSeeAll.setOnClickListener {
            val intent = Intent(this, CourseItemListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpViewPods() {
        courseItemListViewPod = binding.vpCourseItemList as ItemListViewPod
        classItemListViewPod = binding.vpClassItemList as YogaClassListViewPod

    }

    override fun onTapCourseItem(courseData: YogaCourse) {
        startActivity(CourseDetailActivity.newIntent(this,courseData))
    }

    override fun onTapClassItem(yogaClassInstance: YogaClassInstance) {
        startActivity(ClassDetailActivity.newIntent(this,yogaClassInstance))
    }

}