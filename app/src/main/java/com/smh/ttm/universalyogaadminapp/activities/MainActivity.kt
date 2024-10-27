package com.smh.ttm.universalyogaadminapp.activities


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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


class MainActivity : AppCompatActivity(),CourseItemDelegate,YogaClassItemDelegate{

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
       // val toolbar: Toolbar = findViewById(R.id.toolbar)

        // Force Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.swipeRefreshLayout)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        setUpUI()
        setUpListListener()
        setUpObserveData()



    }
    private fun setUpUI() {
        setUpViewPods()
    }
    private fun setUpViewPods() {
        courseItemListViewPod = binding.vpCourseItemList as ItemListViewPod
        classItemListViewPod = binding.vpClassItemList as YogaClassListViewPod

    }
    private fun refreshData() {
        // Example of a refresh operation, like a data reload
        binding.swipeRefreshLayout.isRefreshing = true  // Show loading spinner

        // Simulate a network or data fetch with a delay (use real data loading here)
        binding.nsScrollView.postDelayed({
            // Refreshing done
            yogaCourseViewModel.loadCourses()
            yogaClassInstanceViewModel.loadClassInstances()
            binding.swipeRefreshLayout.isRefreshing = false  // Hide loading spinner
        }, 2000)  // Delay for 2 seconds
    }
//    private fun refreshData() {
//        // Simulate data loading
//        // For example, you could fetch new data from an API
//        binding.swipeRefreshLayout.isRefreshing = true // Show the refreshing spinner
//
//        // This is just a simulation; replace with actual data refresh logic
//        yogaCourseViewModel.loadCourses()
//        yogaClassInstanceViewModel.loadClassInstances()
//
//        // Stop the refreshing animation after data is loaded
//        binding.swipeRefreshLayout.isRefreshing = false
//    }
    private fun setUpListListener() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
        // Control SwipeRefreshLayout to refresh only when at the top
        binding.swipeRefreshLayout.setOnChildScrollUpCallback { _, _ ->
            binding.nsScrollView.scrollY > 0
        }
        binding.linearLayout1.setOnClickListener {
            val intent = Intent(this, CourseItemListActivity::class.java)
            startActivity(intent)
        }

        binding.linearLayout2.setOnClickListener {

            val intent = Intent(this, YogaClassListActivity::class.java)
            startActivity(intent)
        }

        binding.tvClassesSeeAll.setOnClickListener {

            val intent = Intent(this, YogaClassListActivity::class.java)
            startActivity(intent)
        }
        binding.tvCoursesSeeAll.setOnClickListener {
            val intent = Intent(this, CourseItemListActivity::class.java)
            startActivity(intent)
        }

        binding.searchLayout.setOnClickListener {

            val intent = Intent(this, YogaClassListActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setUpObserveData() {
        // Observe data
        yogaClassInstanceViewModel.allClassInstances.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator if necessary

                    showLoading()
                    binding.tvClassesTitle.visibility = View.GONE
                    binding.tvClassesSeeAll.visibility = View.GONE
                    binding.vpClassItemList.visibility = View.GONE
                }

                is Resource.Success -> {
                    // Handle success, e.g., update UI or show a message
                    hideLoading()
                    if(resource.data.isNullOrEmpty()){
                        binding.tvClassesTitle.visibility = View.GONE
                        binding.tvClassesSeeAll.visibility = View.GONE
                        binding.vpClassItemList.visibility = View.GONE

                    }else{
                        binding.tvClassesTitle.visibility = View.VISIBLE
                        binding.tvClassesSeeAll.visibility = View.VISIBLE
                        binding.vpClassItemList.visibility = View.VISIBLE
                        classItemListViewPod.setData(this,itemList = resource.data)

                    }



                }

                is Resource.Error -> {
                    // Handle error, e.g., show an error message
                    hideLoading()
                    binding.tvClassesTitle.visibility = View.GONE
                    binding.tvClassesSeeAll.visibility = View.GONE
                    binding.vpClassItemList.visibility = View.GONE
                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }



        yogaCourseViewModel.allCourses.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator if necessary
                    binding.tvCoursesTitle.visibility = View.GONE
                    binding.tvCoursesSeeAll.visibility = View.GONE
                    binding.vpCourseItemList.visibility = View.GONE
                }

                is Resource.Success -> {
                    // Handle success, e.g., update UI or show a message

                    if(resource.data.isNullOrEmpty()){
                        binding.tvCoursesTitle.visibility = View.GONE
                        binding.tvCoursesSeeAll.visibility = View.GONE
                        binding.vpCourseItemList.visibility = View.GONE
                    }else{
                        binding.tvCoursesTitle.visibility = View.VISIBLE
                        binding.tvCoursesSeeAll.visibility = View.VISIBLE
                        binding.vpCourseItemList.visibility = View.VISIBLE
                        courseItemListViewPod.setData(this,itemList = resource.data)

                    }



                }

                is Resource.Error -> {
                    // Handle error, e.g., show an error message

                    binding.tvCoursesTitle.visibility = View.GONE
                    binding.tvCoursesSeeAll.visibility = View.GONE
                    binding.vpCourseItemList.visibility = View.GONE
                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    override fun onTapCourseItem(courseData: YogaCourse) {
        startActivity(CourseDetailActivity.newIntent(this,courseData))
    }

    override fun onTapClassItem(yogaClassInstance: YogaClassInstance) {
        startActivity(ClassDetailActivity.newIntent(this,yogaClassInstance))
    }


    private fun showLoading() {
        binding.loadingOverlay.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE

    }

    // Function to hide loading and restore Toolbar background
    private fun hideLoading() {
        binding.loadingOverlay.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

    }
}