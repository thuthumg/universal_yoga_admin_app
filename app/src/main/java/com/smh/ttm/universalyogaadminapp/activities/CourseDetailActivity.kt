package com.smh.ttm.universalyogaadminapp.activities

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.smh.ttm.universalyogaadminapp.R
import com.smh.ttm.universalyogaadminapp.data.YogaCourse
import com.smh.ttm.universalyogaadminapp.databinding.ActivityCourseDetailBinding
import com.smh.ttm.universalyogaadminapp.dummy.classTypes
import com.smh.ttm.universalyogaadminapp.dummy.daysOfWeek
import com.smh.ttm.universalyogaadminapp.mvvm.Resource
import com.smh.ttm.universalyogaadminapp.mvvm.YogaCourseViewModel
import java.io.File
import java.io.IOException
import java.util.Calendar
import kotlin.random.Random

class CourseDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseDetailBinding

    private var checkedClassTypeItem = intArrayOf(-1)
    private var checkedDaysOfWeekItem = intArrayOf(-1)
    private val yogaCourseViewModel: YogaCourseViewModel by viewModels()
    private var mCourseVO: YogaCourse? = null

    // Image picker result launcher
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { uriPath ->
            yogaCourseViewModel.selectedImageUriStr.value = uriPath.toString()
            Glide.with(this)
                .load(uriPath)
                .into(binding.ivUpload)

            if (Build.VERSION.SDK_INT >= 29) {

                this.contentResolver?.let {
                    val source: ImageDecoder.Source =
                        ImageDecoder.createSource(it, uriPath)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    yogaCourseViewModel.selectedImageBitmap.value = bitmap

                }


            } else {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    this.applicationContext?.contentResolver, uriPath
                )
                yogaCourseViewModel.selectedImageBitmap.value = bitmap

            }




        }
    }

    // Permission launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openImagePicker()
        } else {
            Toast.makeText(this, "Permission required to select image", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        private const val YOGA_COURSE_VO = "YOGA_COURSE_VO"

        fun newIntent(context: Context, yogaCourseVO: YogaCourse?): Intent {
            val intent = Intent(context, CourseDetailActivity::class.java)
            intent.putExtra(YOGA_COURSE_VO, yogaCourseVO)
            return intent
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // setContentView(R.layout.activity_course_detail)
        binding = ActivityCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enables back arrow
        binding.toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black))

        getIntentParam()
        clickListener()
    }

    private fun clickListener() {
        binding.toolbar.setNavigationOnClickListener {
            finish() // or your desired back action
        }

        binding.typeOfClass.setOnClickListener { showClassTypeDialog() }
        binding.dayOfWeek.setOnClickListener { showDayOfTheWeekDialog() }
        binding.courseTime.setOnClickListener { showTimePickerDialog() }
        binding.btnSubmit.setOnClickListener { handleSubmit() }

        binding.btnFileUpload.setOnClickListener {
            checkPermissionAndPickImage()
        }

        if (mCourseVO == null) {
            binding.ivDelete.visibility = View.GONE
        } else {
            binding.ivDelete.visibility = View.VISIBLE
            binding.ivDelete.setOnClickListener {
                mCourseVO?.let { deleteYogaCourse(it) }
            }
        }

    }

    private fun checkPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openImagePicker()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openImagePicker()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openImagePicker() {
        pickImage.launch("image/*")
    }

    private fun loadLocalImage(imageView: ImageView, imagePath: String) {
        val imageFile = File(imagePath)
        if (imageFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            imageView.setImageBitmap(bitmap)
        } else {
            // Handle file not found case, if needed
            imageView.setImageResource(R.drawable.default_yoga_img) // Set a placeholder image
        }
    }

    private fun getIntentParam() {
        val intent = intent

        if (intent.getSerializableExtra(YOGA_COURSE_VO) == null) {
            mCourseVO = null
            clearData()
        } else {
            mCourseVO = intent.getSerializableExtra(YOGA_COURSE_VO) as YogaCourse
            if (mCourseVO?.id.toString().isEmpty()) {
                clearData()
            } else {
                binding.dayOfWeek.setText(mCourseVO?.dayOfWeek)
                binding.courseTime.setText(mCourseVO?.time)
                binding.capacity.setText(mCourseVO?.capacity.toString())
                binding.duration.setText(mCourseVO?.duration.toString())
                binding.price.setText(mCourseVO?.price)
                binding.typeOfClass.setText(mCourseVO?.type)
                binding.description.setText(mCourseVO?.description)

                // Load image if available
                Log.d("course detail","${mCourseVO}")
                if (!(mCourseVO?.imageUri.isNullOrEmpty())) {
                    yogaCourseViewModel.selectedImageUriStr.value =  mCourseVO?.imageUri
                    val imageUri = Uri.parse(mCourseVO?.imageUri) // Convert the string URI to Uri object
                    // Load the image using Glide
                    Glide.with(this)
                        .load(imageUri)
                        .into(binding.ivUpload)
                } else {
                   // setDefaultImage(mCourseVO?.type)
                }


                val classTypeIndex = classTypes.indexOf(mCourseVO?.type)

                checkedClassTypeItem = if (classTypeIndex != -1) {
                    intArrayOf(classTypeIndex)

                } else {
                    intArrayOf(0)
                }

                val dayOfWeekIndex = daysOfWeek.indexOf(mCourseVO?.dayOfWeek)

                checkedDaysOfWeekItem = if (dayOfWeekIndex != -1) {
                    intArrayOf(dayOfWeekIndex)

                } else {
                    intArrayOf(0)
                }


                binding.btnSubmit.text = "Update"
            }
        }

        when(binding.typeOfClass.text.toString())
        {
            classTypes[0]->{
              //  binding.ivImg.setImageResource(R.drawable.flow_yoga)
            }
            classTypes[1] -> {
              //  binding.ivImg.setImageResource(R.drawable.arial_yoga)
            }
            classTypes[2] -> {
               // binding.ivImg.setImageResource(R.drawable.family_yoga)
            }
        }


    }

//    private fun updateYogaCourse(yogaCourse: YogaCourse) {
//        yogaCourseViewModel.updateCourse(yogaCourse)
//
//        yogaCourseViewModel.updateCourseStatus.observe(this) { resource ->
//            when (resource) {
//                is Resource.Loading -> {
//                    // Show loading indicator if necessary
//
//                }
//
//                is Resource.Success -> {
//                    // Handle success, e.g., update UI or show a message
//                    finish()
//                }
//
//                is Resource.Error -> {
//                    // Handle error, e.g., show an error message
//                    Toast.makeText(this, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
//
//        // Optionally, observe the LiveData if needed
//        yogaCourseViewModel.allCourses.observe(this) { courses ->
//            // Update your UI with the list of courses
//        }
//    }

    private fun insertYogaCourse(yogaCourse: YogaCourse) {
        //yogaCourseViewModel.insertCourse(yogaCourse)
        yogaCourseViewModel.selectedImageBitmap.value?.let {
            yogaCourseViewModel.uploadImageAndSaveCourse(yogaCourse,
                it
            )
        }
        yogaCourseViewModel.insertCourseStatus.observe(this) { resource ->
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
        yogaCourseViewModel.allCourses.observe(this) { courses ->
            // Update your UI with the list of courses
        }
    }

    private fun showDayOfTheWeekDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select day of the week")

        builder.setSingleChoiceItems(daysOfWeek, checkedDaysOfWeekItem[0]) { dialog, which ->
            // user checked an item
            checkedDaysOfWeekItem[0] = which
            binding.dayOfWeek.setText(daysOfWeek[which])
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()


    }

    private fun showClassTypeDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Type of Class")

        builder.setSingleChoiceItems(classTypes, checkedClassTypeItem[0]) { dialog, which ->
            // user checked an item
            checkedClassTypeItem[0] = which
            binding.typeOfClass.setText(classTypes[which])
//            when(classTypes[which]){
//                classTypes[0]->{
//                    binding.ivImg.setImageResource(R.drawable.flow_yoga)
//                }
//                classTypes[1] -> {
//                    binding.ivImg.setImageResource(R.drawable.arial_yoga)
//                }
//                classTypes[2] -> {
//                    binding.ivImg.setImageResource(R.drawable.family_yoga)
//                }
//            }
            dialog.dismiss()

        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteYogaCourse(yogaCourse: YogaCourse) {
        yogaCourseViewModel.deleteCourse(yogaCourse)

        yogaCourseViewModel.deleteCourseStatus.observe(this) { resource ->
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
        yogaCourseViewModel.allCourses.observe(this) { courses ->
            // Update your UI with the list of courses
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            binding.courseTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
        }, hour, minute, false)

        timePickerDialog.show()
    }

    private fun handleSubmit() {
        val dayOfWeek = binding.dayOfWeek.text.toString()
        val courseTime = binding.courseTime.text.toString()
        val capacity = binding.capacity.text.toString()
        val duration = binding.duration.text.toString()
        val price = binding.price.text.toString()
        val typeOfClass = binding.typeOfClass.text.toString()

        // Validate required fields
        if (dayOfWeek.isEmpty() || courseTime.isEmpty() || capacity.isEmpty() ||
            duration.isEmpty() || price.isEmpty() || typeOfClass.isEmpty()
        ) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Handle the submission logic here (e.g., save data to database)

        // Show confirmation message
        saveDataToLocal(dayOfWeek, courseTime, capacity, duration, price, typeOfClass)

    }

//    private fun setDefaultImage(type: String?) {
//        when(type) {
//            classTypes[0] -> binding.ivImg.setImageResource(R.drawable.flow_yoga)
//            classTypes[1] -> binding.ivImg.setImageResource(R.drawable.arial_yoga)
//            classTypes[2] -> binding.ivImg.setImageResource(R.drawable.family_yoga)
//            else -> binding.ivImg.setImageResource(R.drawable.default_yoga_img)
//        }
//    }

    private fun saveDataToLocal(
        dayOfWeek: String,
        courseTime: String,
        capacity: String,
        duration: String,
        price: String,
        typeOfClass: String
    ) {
        val yogaClass = YogaCourse(
            courseName = "${typeOfClass} Course (${dayOfWeek}-${courseTime})",
            courseId = getRandomSixDigitNumber().toLong(),
            dayOfWeek = dayOfWeek,
            time = courseTime,
            capacity = capacity.toInt(),
            duration = duration.toInt(),
            price = price.toDouble().toString(),
            type = typeOfClass,
            description = binding.description.text.toString(),
            imageUri =  yogaCourseViewModel.selectedImageUriStr.value.toString()
        )

        if (mCourseVO == null) {
            // Toast.makeText(this, "insertYogaCourse ${yogaClass}", Toast.LENGTH_SHORT).show()
            insertYogaCourse(yogaClass)

        } else {

            mCourseVO?.dayOfWeek = dayOfWeek
            mCourseVO?.time = courseTime
            mCourseVO?.capacity = capacity.toInt()
            mCourseVO?.duration = duration.toInt()
            mCourseVO?.price = price.toDouble().toString()
            mCourseVO?.type = typeOfClass
            mCourseVO?.description = binding.description.text.toString()
            mCourseVO?.imageUri =  yogaCourseViewModel.selectedImageBitmap.value.toString()

            // Toast.makeText(this, "updateYogaCourse ${yogaClass}", Toast.LENGTH_SHORT).show()
           // mCourseVO?.let { updateYogaCourse(it) }

            mCourseVO?.let { insertYogaCourse(it) }
        }


    }

    private fun getRandomSixDigitNumber(): Int {
        return Random.nextInt(100000, 1000000) // Generates a number from 100000 to 999999
    }

    private fun clearData() {
        binding.dayOfWeek.setText("")
        binding.courseTime.setText("")
        binding.capacity.setText("0")
        binding.duration.setText("0")
        binding.price.setText("")
        binding.typeOfClass.setText("")
        binding.description.setText("")

        binding.btnSubmit.text = "Save"
    }


}