package com.smh.ttm.universalyogaadminapp.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.smh.ttm.universalyogaadminapp.dialog.CourseTypeDialogFragment
import com.smh.ttm.universalyogaadminapp.R
import com.smh.ttm.universalyogaadminapp.data.YogaClassInstance
import com.smh.ttm.universalyogaadminapp.data.YogaCourse
import com.smh.ttm.universalyogaadminapp.databinding.ActivityClassDetailBinding
import com.smh.ttm.universalyogaadminapp.mvvm.Resource
import com.smh.ttm.universalyogaadminapp.mvvm.YogaClassInstanceViewModel
import com.smh.ttm.universalyogaadminapp.mvvm.YogaCourseViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class ClassDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassDetailBinding
    private val yogaClassViewModel: YogaClassInstanceViewModel by viewModels()
    private val yogaCourseViewModel: YogaCourseViewModel by viewModels()

    private var mClassInstanceVO: YogaClassInstance? = null
    private var weekday: String = ""

    private var listOfYogaCourseByWeekDay: List<YogaCourse> = listOf()
    private var selectedYogaCourse: YogaCourse? = null


    companion object {

        private const val YOGA_CLASS_INSTANCE_VO = "YOGA_CLASS_INSTANCE_VO"

        fun newIntent(context: Context, yogaClassInstanceVO: YogaClassInstance?): Intent {
            val intent = Intent(context, ClassDetailActivity::class.java)
            intent.putExtra(YOGA_CLASS_INSTANCE_VO, yogaClassInstanceVO)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityClassDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enables back arrow
        binding.toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black))
        binding.ivImg.setImageResource(R.drawable.default_yoga_img)
        getIntentParam()
        clickListener()


    }

    private fun clickListener() {
        binding.toolbar.setNavigationOnClickListener {
            finish() // or your desired back action
        }

        binding.etDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.etCourse.setOnClickListener {
            showClassTypeDialog(listOfYogaCourseByWeekDay)
        }
        binding.btnClassSave.setOnClickListener {
            handleClassSave()
        }
        if (mClassInstanceVO == null) {
            binding.ivDelete.visibility = View.GONE
        } else {
            binding.ivDelete.visibility = View.VISIBLE
            binding.ivDelete.setOnClickListener {
                mClassInstanceVO?.let { deleteYogaClassInstance(it) }
            }
        }

    }

    private fun getIntentParam() {

        val intent = intent

        if (intent.getSerializableExtra(YOGA_CLASS_INSTANCE_VO) == null) {
            mClassInstanceVO = null
            clearData()
        } else {
            mClassInstanceVO =
                intent.getSerializableExtra(YOGA_CLASS_INSTANCE_VO) as YogaClassInstance
            if (mClassInstanceVO?.id.toString().isEmpty()) {

                clearData()
            } else {
                binding.etClassName.setText(mClassInstanceVO?.className)
                binding.etDate.setText(mClassInstanceVO?.date)
                binding.instructorName.setText(mClassInstanceVO?.teacher)
                binding.description.setText(mClassInstanceVO?.additionalComments)

                binding.etCourse.visibility = View.VISIBLE
                binding.tvCourse.visibility = View.VISIBLE
                weekday = getDayOfWeekFromString(binding.etDate.text.toString())
                getAllCourses(weekday)
                binding.etCourse.setText(mClassInstanceVO?.courseName)

                binding.btnClassSave.text = "Update"
            }
        }


    }

    private fun clearData() {
        binding.etDate.setText("")
        binding.instructorName.setText("")
        binding.description.setText("")
        binding.etClassName.setText("C-${getRandomSixDigitNumber()}")

        binding.btnClassSave.text = "Save"
    }

    private fun getDayOfWeekFromString(dateString: String): String {

        // Define the date format that matches the input string
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = dateFormat.parse(dateString)

        // Define the format to get the weekday name
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dayFormat.format(date ?: Date())
    }

    private fun showClassTypeDialog(listOfYogaCourses: List<YogaCourse>) {

        val dialog = CourseTypeDialogFragment.newInstance(listOfYogaCourses)
        dialog.setOnCourseSelectedListener { selectedCourse ->
            // Use the selected YogaCourse object
            println("Selected course: ${selectedCourse.courseName}")
            selectedYogaCourse = selectedCourse
            binding.etCourse.setText(selectedCourse.courseName)
        }
        dialog.show(supportFragmentManager, "CourseTypeDialog")

    }

    private fun handleClassSave() {
        val classDate = binding.etDate.text.toString()
        val instructorName = binding.instructorName.text.toString()
        val description = binding.description.text.toString()
        val selectedYogaCourseId = selectedYogaCourse?.id.toString()
        val selectedYogaCourseName = selectedYogaCourse?.courseName.toString()
        val className = binding.etClassName.text.toString()

        if (className.isEmpty() || classDate.isEmpty() || instructorName.isEmpty()
            || description.isEmpty() || selectedYogaCourseId.isEmpty() || binding.etCourse.text.toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        saveDataToLocal(
            selectedYogaCourseId,
            selectedYogaCourseName,
            classDate,
            instructorName,
            description,
            className
        )

    }

    private fun saveDataToLocal(
        courseId: String, courseName: String, classDate: String,
        instructorName: String, description: String, className: String
    ) {
        val yogaClassInstance = YogaClassInstance(
            classId = getRandomSixDigitNumber().toLong(),
            className = className,
            courseId = courseId.toInt(),
            courseName = courseName,
            date = classDate,
            teacher = instructorName,
            additionalComments = description
        )
        if (mClassInstanceVO == null) {
            // Toast.makeText(this, "insertYogaCourse ${yogaClass}", Toast.LENGTH_SHORT).show()
            insertYogaClassInstance(yogaClassInstance)

        } else {
            mClassInstanceVO?.courseId = courseId.toInt()
            mClassInstanceVO?.date = classDate
            mClassInstanceVO?.teacher = instructorName
            mClassInstanceVO?.additionalComments = description
            mClassInstanceVO?.courseName = courseName
            mClassInstanceVO?.className = className


            // Toast.makeText(this, "updateYogaCourse ${mClassInstanceVO}", Toast.LENGTH_SHORT).show()
            mClassInstanceVO?.let { updateYogaClassInstance(it) }
        }


    }

    private fun getRandomSixDigitNumber(): Int {
        return Random.nextInt(100000, 1000000) // Generates a number from 100000 to 999999
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, { _, selectedYear, selectedMonth, selectedDay ->
                // Set the date in "dd-MM-yyyy" format
                binding.etDate.setText(
                    String.format(
                        "%02d-%02d-%04d",
                        selectedDay,
                        selectedMonth + 1,
                        selectedYear
                    )
                )

                weekday = getDayOfWeek(selectedYear, selectedMonth, selectedDay)
                Log.d("activity", "weekday $weekday") // Output: Friday

                binding.etCourse.setText("")
                selectedYogaCourse = null
                getAllCourses(weekday)
                binding.etCourse.visibility = View.VISIBLE
                binding.tvCourse.visibility = View.VISIBLE
            }, year, month, day
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }


    private fun getAllCourses(weekday: String) {
        Log.d("activity", "getAllCourses ${weekday}") // Output: Friday
        yogaCourseViewModel.loadCoursesByWeekDay(weekday)

        yogaCourseViewModel.allCoursesByWeekDay.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator if necessary
                    Log.d("activity", "getAllCourses1") // Output: Friday
                }

                is Resource.Success -> {
                    // Handle success, e.g., update UI or show a message

                    Log.d("activity", "getAllCourses2") // Output: Friday
                    resource.data?.let {
                        listOfYogaCourseByWeekDay = it
                        Log.d(
                            "activity",
                            "getAllCourses3 ${listOfYogaCourseByWeekDay}"
                        ) // Output: Friday
                    }

                }

                is Resource.Error -> {
                    Log.d("activity", "getAllCourses4") // Output: Friday
                    // Handle error, e.g., show an error message
                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun getDayOfWeek(selectedYear: Int, selectedMonth: Int, selectedDay: Int): String {
        // Format the date string in "dd-MM-yyyy"
        val dateString = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)

        // Parse the date string with "dd-MM-yyyy" format
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = dateFormat.parse(dateString)

        // Get the day of the week
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dayFormat.format(date ?: Date())
    }

    private fun insertYogaClassInstance(yogaClassInstance: YogaClassInstance) {

        yogaClassViewModel.insertClassInstance(yogaClassInstance)

        yogaClassViewModel.insertClassInstanceStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator if necessary

                }

                is Resource.Success -> {
                    // Handle success, e.g., update UI or show a message
                    finish()
                }

                is Resource.Error -> {
                    // Handle error, e.g., show an error message
                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }


        // Optionally, observe the LiveData if needed
        yogaClassViewModel.allClassInstances.observe(this) { courses ->
            // Update your UI with the list of courses
        }
    }

    private fun updateYogaClassInstance(yogaClassInstance: YogaClassInstance) {

        yogaClassViewModel.updateClassInstance(yogaClassInstance)

        yogaClassViewModel.updateClassInstanceStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator if necessary

                }

                is Resource.Success -> {
                    // Handle success, e.g., update UI or show a message
                    finish()
                }

                is Resource.Error -> {
                    // Handle error, e.g., show an error message
                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }


        // Optionally, observe the LiveData if needed
        yogaClassViewModel.allClassInstances.observe(this) { courses ->
            // Update your UI with the list of courses
        }
    }

    private fun deleteYogaClassInstance(yogaClassInstance: YogaClassInstance) {
        yogaClassViewModel.deleteClassInstance(yogaClassInstance)

        yogaClassViewModel.deleteClassInstanceStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator if necessary

                }

                is Resource.Success -> {
                    // Handle success, e.g., update UI or show a message
                    finish()
                }

                is Resource.Error -> {
                    // Handle error, e.g., show an error message
                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }


        // Optionally, observe the LiveData if needed
        yogaClassViewModel.allClassInstances.observe(this) { courses ->
            // Update your UI with the list of courses
        }
    }
}