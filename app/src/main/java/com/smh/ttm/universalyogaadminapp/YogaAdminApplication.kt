package com.smh.ttm.universalyogaadminapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.google.firebase.firestore.FirebaseFirestore
import com.smh.ttm.universalyogaadminapp.persistence.YogaDatabase
import com.smh.ttm.universalyogaadminapp.repository.YogaRepository

class YogaAdminApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        checkAndSyncData()
    }

    private fun checkAndSyncData() {
        if (isInternetAvailable()) {
            // Assume you have a repository instance available
            val yogaCourseDao = YogaDatabase.getDatabase(this).yogaCourseDao()
            val yogaClassInstanceDao = YogaDatabase.getDatabase(this).yogaClassInstanceDao()
            val firebaseDb = FirebaseFirestore.getInstance()
            val repository = YogaRepository(yogaCourseDao, yogaClassInstanceDao, firebaseDb, this)

            // Sync courses from Room to Firebase
            repository.syncCoursesFromRoomToFirebase()
            repository.syncClassInstancesFromRoomToFirebase()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}