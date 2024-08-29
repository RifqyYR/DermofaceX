plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.makassar.dermofacex"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.makassar.dermofacex"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_URL", "\"https://6066-103-195-142-125.ngrok-free.app/\"")
            buildConfigField("boolean", "CATALYST_DEBUG", "true")
            buildConfigField("boolean", "ALLOW_INVALID_CERTIFICATE", "true")
        }
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.camera.core)

    // Face Detection
    implementation(libs.face.detection)
    implementation(libs.play.services.mlkit.face.detection)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    api(libs.retrofit2.kotlin.coroutines.adapter)
    implementation(libs.retrofit2.converter.scalars)

    // OkHTTP3
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Coroutine
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Livedata
    api(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // view model
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //library glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    //koin
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.core.coroutines)


    // Camera X
    implementation(libs.androidx.camera.core.v110alpha05)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.firebase.crashlytics.buildtools)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}