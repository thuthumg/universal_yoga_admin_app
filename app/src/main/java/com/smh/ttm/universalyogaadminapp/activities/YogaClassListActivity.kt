package com.smh.ttm.universalyogaadminapp.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.smh.ttm.universalyogaadminapp.databinding.ActivityYogaClassListBinding
import com.smh.ttm.universalyogaadminapp.delegates.YogaClassItemDelegate
import com.smh.ttm.universalyogaadminapp.dummy.filterTypes
import com.smh.ttm.universalyogaadminapp.mvvm.Resource
import com.smh.ttm.universalyogaadminapp.mvvm.YogaClassInstanceViewModel
import com.smh.ttm.universalyogaadminapp.viewpods.YogaClassListViewPod
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

import java.util.concurrent.TimeUnit

class YogaClassListActivity : AppCompatActivity(), YogaClassItemDelegate {
    val checkedItem = intArrayOf(0)
    private lateinit var binding: ActivityYogaClassListBinding
    private lateinit var classItemListViewPod: YogaClassListViewPod
    private val yogaClassViewModel: YogaClassInstanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityYogaClassListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.class_item_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.classEtSearch.hint = filterTypes[0]
        binding.llSearchByTeacher.visibility = View.VISIBLE
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
        // Set up clear icon functionality if needed
        binding.textInputLayout.setEndIconOnClickListener {
            binding.classEtSearch.text?.clear() // Clear the text when the icon is clicked
        }

        binding.ivFilter.setOnClickListener {
            showFilterAlert()
        }
        binding.btnSearch.setOnClickListener {
            yogaClassViewModel.searchByDateRange(binding.etStartDate.text.toString(),
                binding.etEndDate.text.toString())
        }
        binding.etStartDate.setOnClickListener {
            showDatePickerDialog("start")
        }
        binding.etEndDate.setOnClickListener {
            showDatePickerDialog("end")
        }
        setUpSearchListener()
    }
    private fun showDatePickerDialog(type: String) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, { _, selectedYear, selectedMonth, selectedDay ->
                // Set the date in "dd-MM-yyyy" format
                when(type)
                {
                    "start" -> {
                        binding.etStartDate.setText(
                            String.format(
                                "%02d-%02d-%04d",
                                selectedDay,
                                selectedMonth + 1,
                                selectedYear
                            )
                        )
                    }
                    "end" -> {
                        binding.etEndDate.setText(
                            String.format(
                                "%02d-%02d-%04d",
                                selectedDay,
                                selectedMonth + 1,
                                selectedYear
                            )
                        )
                    }
                    else -> {
                        binding.etStartDate.setText(
                            String.format(
                                "%02d-%02d-%04d",
                                selectedDay,
                                selectedMonth + 1,
                                selectedYear
                            )
                        )
                    }
                }

            }, year, month, day
        )

        datePickerDialog.show()
    }
//    private fun setUpSearchListener() {
//        binding.classEtSearch
//            .textChanges() // Observes text changes
//            .debounce(500, TimeUnit.MILLISECONDS) // Waits for the user to stop typing
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({ query ->
//                performSearch(query.toString()) // Perform search when text changes
//            }, { error ->
//                Toast.makeText(this, "Error: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
//            })
//    }

   // For rxjava2 version code
    private fun setUpSearchListener() {
        // Listening for text changes
        Observable.create<String> { emitter ->
            binding.classEtSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    emitter.onNext(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
            .debounce(500L, TimeUnit.MILLISECONDS)
            .flatMap { query ->
                yogaClassViewModel.searchByTeacher(query)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ results ->
                classItemListViewPod.setData(this, results)
            }, { error ->
                showError(error.localizedMessage ?: "An error occurred")
            })
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
            changeSearchUI(filterTypes[which])
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun changeSearchUI(filterType: String) {
        when(filterType){
            filterTypes[0]-> {
                binding.llSearchByDate.visibility = View.GONE
                binding.llSearchByTeacher.visibility = View.VISIBLE
                binding.classEtSearch.hint = filterType
                yogaClassViewModel.loadClassInstances()
            }
            filterTypes[1]->{
                binding.llSearchByDate.visibility = View.VISIBLE
                binding.llSearchByTeacher.visibility = View.GONE
                binding.etStartDate.setText(getCurrentDate())// add current date
                binding.etEndDate.setText(getCurrentDate())// add current date
                yogaClassViewModel.searchByDateRange(getCurrentDate(),getCurrentDate())

            }

        }

    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

}