plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.kapt") // Add kapt plugin explicitly
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
   // id("kotlin-android-extensions")
  //  id(libs.plugins.kotlinKapt)
   // alias(libs.plugins.hilt)
  //  id("com.google.dagger:hilt-android-gradle-plugin:2.44")
   // id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.smh.ttm.universalyogaadminapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.smh.ttm.universalyogaadminapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // Room
    implementation(libs.roomRuntime)
    implementation(libs.androidx.annotation)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    kapt(libs.roomCompiler) // Correct use of kapt with Room
    implementation(libs.roomKtx)
    // Room with RxJava3
    implementation (libs.androidx.room.rxjava2)
    implementation (libs.reactivestreams)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // RxJava and RxKotlin
    implementation (libs.rxjava)
    implementation (libs.rxkotlin)


    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofitGson)

    // Hilt
    implementation(libs.hilt.android.gradle.plugin)
    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler) // Correct use of kapt with Hilt compiler

    // LiveData and ViewModel
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.lifecycleViewModel)
    implementation(libs.lifecycleLiveData)

    // WorkManager
    implementation(libs.workManager)

    // Coroutines
    implementation(libs.coroutinesCore)
    implementation(libs.coroutinesAndroid)
    implementation(libs.firebase.bom)
    implementation (libs.firebase.storage)
   // implementation(libs.firebase.analytics)

   implementation(libs.android.gif.drawable)
   implementation(libs.circleimageview)

    implementation(libs.rxandroid)
   // implementation ("com.jakewharton.rxbinding4:rxbinding:4.0.0")

    //RxKotlin
//    implementation ("io.reactivex.rxjava3:rxkotlin:3.0.0")
//    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
//    implementation ("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")
//
//
//    //RxBiniding
//    implementation ("com.jakewharton.rxbinding4:rxbinding-core:4.0.0")
//    implementation ("com.jakewharton.rxbinding4:rxbinding-appcompat:4.0.0")
    implementation (libs.androidx.swiperefreshlayout)


    //glide for image
    implementation (libs.github.glide)
  //  implementation (libs.androidx.espresso.contrib)
  //  implementation (libs.androidx.navigation.ui.ktx)
    annotationProcessor (libs.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}