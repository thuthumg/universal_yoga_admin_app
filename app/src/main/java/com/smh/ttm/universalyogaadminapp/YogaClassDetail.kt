package com.smh.ttm.universalyogaadminapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.smh.ttm.universalyogaadminapp.dummy.classTypes
import java.util.Calendar

class YogaClassDetail :  AppCompatActivity() {
    /* override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         enableEdgeToEdge()
         setContentView(R.layout.activity_main)
         ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
             val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
             v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
             insets
         }
     }*/

    private lateinit var dayOfWeekEditText: EditText
    private lateinit var courseTimeEditText: EditText
    private lateinit var capacityEditText: EditText
    private lateinit var durationEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var typeOfClassEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var instructorNameEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_yoga_class_detail)

        // Force Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        /*  // Get the Action Bar Height
          val actionBarHeight: Int
          val styledAttributes = theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
          actionBarHeight = styledAttributes.getDimensionPixelSize(0, 0)
          styledAttributes.recycle()

          // Log the Action Bar Height
          Log.d("ActionBarHeight", "Action Bar Height: $actionBarHeight pixels")
  */
        // Set Padding to the Main Layout
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top + actionBarHeight, systemBars.right, systemBars.bottom)
//            insets
//        }
        // Initialize EditTexts
        dayOfWeekEditText = findViewById(R.id.day_of_week)
        courseTimeEditText = findViewById(R.id.course_time)
        capacityEditText = findViewById(R.id.capacity)
        durationEditText = findViewById(R.id.duration)
        priceEditText = findViewById(R.id.price)
        typeOfClassEditText = findViewById(R.id.type_of_class)
        descriptionEditText = findViewById(R.id.description)
        locationEditText = findViewById(R.id.location)
        instructorNameEditText = findViewById(R.id.instructor_name)

        // Setup EditText for Day of the Week
        dayOfWeekEditText.setOnClickListener { showDatePickerDialog() }

        // Setup EditText for Time of Course
        courseTimeEditText.setOnClickListener { showTimePickerDialog() }

        // Setup EditText for Type of Class
        typeOfClassEditText.setOnClickListener { showClassTypeDialog() }

        // Setup Submit Button
        val submitButton: Button = findViewById(R.id.submit_button)
        submitButton.setOnClickListener { handleSubmit() }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create DatePickerDialog instance
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date
                val formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                dayOfWeekEditText.setText(formattedDate)
            },
            year,
            month,
            day
        )

        // Customize dialog appearance
        // datePickerDialog.setTitle("Select Date")
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() // Disable past dates
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().apply {
            add(Calendar.YEAR, 10) // Optional: Set a maximum limit (e.g., 10 years from now)
        }.timeInMillis // Allow selection of dates up to 10 years from today
        datePickerDialog.show() // Show the dialog
    }


    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            courseTimeEditText.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
        }, hour, minute, false)

        timePickerDialog.show()
    }

    private fun showClassTypeDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Type of Class")
            .setItems(classTypes) { _, which ->
                typeOfClassEditText.setText(classTypes[which])
            }
        builder.create().show()
    }

    private fun handleSubmit() {
        val dayOfWeek = dayOfWeekEditText.text.toString()
        val courseTime = courseTimeEditText.text.toString()
        val capacity = capacityEditText.text.toString()
        val duration = durationEditText.text.toString()
        val price = priceEditText.text.toString()
        val typeOfClass = typeOfClassEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val location = locationEditText.text.toString()
        val instructorName = instructorNameEditText.text.toString()

        // Validate required fields
        if (dayOfWeek.isEmpty() || courseTime.isEmpty() || capacity.isEmpty() ||
            duration.isEmpty() || price.isEmpty() || typeOfClass.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Handle the submission logic here (e.g., save data to database)

        // Show confirmation message
        Toast.makeText(this, "Class scheduled successfully!", Toast.LENGTH_SHORT).show()
    }
}