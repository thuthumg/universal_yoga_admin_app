package com.smh.ttm.universalyogaadminapp.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.smh.ttm.universalyogaadminapp.R
import com.smh.ttm.universalyogaadminapp.data.YogaClassInstance
import com.smh.ttm.universalyogaadminapp.databinding.ActivityClassItemListBinding
import com.smh.ttm.universalyogaadminapp.delegates.YogaClassItemDelegate
import com.smh.ttm.universalyogaadminapp.mvvm.Resource
import com.smh.ttm.universalyogaadminapp.mvvm.YogaClassInstanceViewModel
import com.smh.ttm.universalyogaadminapp.viewpods.YogaClassListViewPod

class ClassItemListActivity : AppCompatActivity(), YogaClassItemDelegate {

    private lateinit var binding: ActivityClassItemListBinding
    private lateinit var classItemListViewPod: YogaClassListViewPod
    private  val yogaClassViewModel: YogaClassInstanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // setContentView(R.layout.activity_class_item_list)

        binding = ActivityClassItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpViewPods()
        clickListener()
       observeData()

    }

    private fun observeData() {
        yogaClassViewModel.allClassInstances.observe(this) { resources ->
            when (resources) {
                is Resource.Loading -> {
                    // Show loading indicator if necessary

                }

                is Resource.Success -> {
                    // Handle success, e.g., update UI or show a message


                    resources.data?.let {
                        classItemListViewPod.setData(this, itemList = it)
                    }

                }

                is Resource.Error -> {
                    // Handle error, e.g., show an error message
                    Toast.makeText(this, "Error: ${resources.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clickListener() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enables back arrow
        binding.toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.white))
        binding.toolbar.setNavigationOnClickListener {
            finish() // or your desired back action
        }
        binding.fbCreateNew.setOnClickListener {

            startActivity(ClassDetailActivity.newIntent(this, null))
        }
        binding.classEtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Implement your search logic here
                performSearch(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Set up clear icon functionality if needed
        binding.textInputLayout.setEndIconOnClickListener {
            binding.classEtSearch.text?.clear() // Clear the text when the icon is clicked
        }
    }


    private fun performSearch(query: String) {
        // Implement your search logic here
        // e.g., filter a list, make a network request, etc.
    }

    private fun setUpViewPods() {
        classItemListViewPod = binding.vpClassList
    }

    override fun onTapClassItem(yogaClassInstance: YogaClassInstance) {
        startActivity(ClassDetailActivity.newIntent(this, yogaClassInstance))
    }
}