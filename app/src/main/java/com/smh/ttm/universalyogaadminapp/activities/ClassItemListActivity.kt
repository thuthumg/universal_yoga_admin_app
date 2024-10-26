package com.smh.ttm.universalyogaadminapp.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.smh.ttm.universalyogaadminapp.R
import com.smh.ttm.universalyogaadminapp.data.YogaClassInstance
import com.smh.ttm.universalyogaadminapp.databinding.ActivityClassItemListBinding
import com.smh.ttm.universalyogaadminapp.delegates.YogaClassItemDelegate
import com.smh.ttm.universalyogaadminapp.dummy.filterTypes
import com.smh.ttm.universalyogaadminapp.mvvm.Resource
import com.smh.ttm.universalyogaadminapp.mvvm.YogaClassInstanceViewModel
import com.smh.ttm.universalyogaadminapp.viewpods.YogaClassListViewPod

class ClassItemListActivity : AppCompatActivity(), YogaClassItemDelegate {
    val checkedItem = intArrayOf(-1)
    private lateinit var binding: ActivityClassItemListBinding
    private lateinit var classItemListViewPod: YogaClassListViewPod
    private val yogaClassViewModel: YogaClassInstanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // setContentView(R.layout.activity_class_item_list)

        binding = ActivityClassItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.class_item_main)) { v, insets ->
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
                    showLoading()
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    binding.vpClassList.visibility = View.GONE
                }

                is Resource.Success -> {
                    // Handle success, e.g., update UI or show a message

                    hideLoading()
                    if (resources.data.isNullOrEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.vpClassList.visibility = View.GONE

                    } else {

                        binding.tvNoDataFound.visibility = View.GONE
                        binding.vpClassList.visibility = View.VISIBLE
                        classItemListViewPod.setData(this, itemList = resources.data)

                    }

                }

                is Resource.Error -> {
                    // Handle error, e.g., show an error message
                    hideLoading()
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    binding.vpClassList.visibility = View.GONE
                    Toast.makeText(this, "Error: ${resources.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clickListener() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enables back arrow
        binding.toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black))
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

        binding.cvFilter.setOnClickListener {
            showFilterAlert()
        }
    }


    private fun performSearch(query: String) {
        // Implement your search logic here
        // e.g., filter a list, make a network request, etc.
        if(query.isNullOrEmpty())
        {
            yogaClassViewModel.loadClassInstances()
        }else{
            when(binding.classEtSearch.hint.toString())
            {
                filterTypes[0] -> {
                    getAllClassesSearchByTeacher(query)
                }
                filterTypes[1] -> {
                    getAllClassesSearchByDate(query)
                }
            }
        }


    }

    private fun getAllClassesSearchByTeacher(searchByTeacher: String) {
        yogaClassViewModel.searchByTeacher(searchByTeacher)
    }
    private fun getAllClassesSearchByDate(searchByDate: String) {
        yogaClassViewModel.searchByDate(searchByDate)
    }

    private fun setUpViewPods() {
        classItemListViewPod = binding.vpClassList
    }

    override fun onTapClassItem(yogaClassInstance: YogaClassInstance) {
        startActivity(ClassDetailActivity.newIntent(this, yogaClassInstance))
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

    private fun showFilterAlert() {


        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select the filter type")

        builder.setSingleChoiceItems(filterTypes, checkedItem[0]) { dialog, which ->
            // user checked an item
            checkedItem[0] = which
            binding.classEtSearch.hint = filterTypes[which]
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}