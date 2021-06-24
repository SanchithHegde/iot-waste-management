plugins {
  id("com.onesignal.androidsdk.onesignal-gradle-plugin")
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-kapt")
  id("dagger.hilt.android.plugin")
  id("com.google.secrets_gradle_plugin") version "0.6"
}

android {
  compileSdkVersion(30)
  buildToolsVersion("30.0.3")

  defaultConfig {
    applicationId = "me.sanchithhegde.wastecollection"
    minSdkVersion(26)
    targetSdkVersion(30)
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    versionCode = 1
    versionName = "1.0"
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    dataBinding = true
  }
}

dependencies {
  implementation(AndroidX.appCompat)
  implementation(AndroidX.constraintLayout)
  implementation(AndroidX.core.ktx)
  implementation(AndroidX.hilt.lifecycleViewModel)
  implementation(AndroidX.lifecycle.viewModelKtx)
  implementation(AndroidX.lifecycle.liveDataKtx)
  implementation(AndroidX.navigation.fragmentKtx)
  implementation(AndroidX.navigation.uiKtx)
  implementation(AndroidX.preferenceKtx)
  implementation(AndroidX.room.runtime)
  implementation(AndroidX.room.ktx)
  implementation(Google.android.material)
  implementation(Google.dagger.hilt.android)
  implementation("com.onesignal:OneSignal:_")

  kapt(AndroidX.room.compiler)
  kapt(Google.dagger.hilt.compiler)
}

secrets {
  propertiesFileName = "secrets.properties"
}