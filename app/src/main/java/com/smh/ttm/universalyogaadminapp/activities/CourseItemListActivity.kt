package com.smh.ttm.universalyogaadminapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.smh.ttm.universalyogaadminapp.R
import com.smh.ttm.universalyogaadminapp.data.YogaCourse
import com.smh.ttm.universalyogaadminapp.databinding.ActivityCourseItemListBinding
import com.smh.ttm.universalyogaadminapp.delegates.CourseItemDelegate
import com.smh.ttm.universalyogaadminapp.mvvm.Resource
import com.smh.ttm.universalyogaadminapp.mvvm.YogaCourseViewModel
import com.smh.ttm.universalyogaadminapp.viewpods.ItemListViewPod
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CourseItemListActivity : AppCompatActivity(), CourseItemDelegate {

    private lateinit var binding: ActivityCourseItemListBinding
    private lateinit var courseItemListViewPod: ItemListViewPod
    private val yogaCourseViewModel: YogaCourseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCourseItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.course_item_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpViewPods()
        clickListener()
        observeData()

    }

    private fun observeData() {
        yogaCourseViewModel.allCourses.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator if necessary
                    showLoading()
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    binding.vpCourseList.visibility = View.GONE
                }

                is Resource.Success -> {
                    // Handle success, e.g., update UI or show a message
                    hideLoading()
                    binding.tvNoDataFound.visibility = View.GONE
                    binding.vpCourseList.visibility = View.VISIBLE

                    if (resource.data.isNullOrEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.vpCourseList.visibility = View.GONE
                    } else {
                        binding.tvNoDataFound.visibility = View.GONE
                        binding.vpCourseList.visibility = View.VISIBLE
                        courseItemListViewPod.setData(this, itemList = resource.data)
                    }



                }

                is Resource.Error -> {
                    // Handle error, e.g., show an error message
                    hideLoading()
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    binding.vpCourseList.visibility = View.GONE
                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun searchClickListener(){
        Observable.create<String> { emitter ->
            binding.etSearchCourse.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    emitter.onNext(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
            .debounce(500L, TimeUnit.MILLISECONDS)
            .flatMap { query ->
                yogaCourseViewModel.searchByCourseType(query)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ results ->
                courseItemListViewPod.setData(this, results)
            }, { error ->
                showError(error.localizedMessage ?: "An error occurred")
            })
    }
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun clickListener() {
        searchClickListener()
        binding.fbCreateNew.setOnClickListener {

//            val intent = Intent(this, CourseDetailActivity::class.java)
//            startActivity(intent)
            startActivity(CourseDetailActivity.newIntent(this,null))
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enables back arrow
        binding.toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black))
        binding.toolbar.setNavigationOnClickListener {
            finish() // or your desired back action
        }
    }

    private fun setUpViewPods() {
        courseItemListViewPod = binding.vpCourseList
    }

    override fun onTapCourseItem(courseData: YogaCourse) {
        startActivity(CourseDetailActivity.newIntent(this, courseData))
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