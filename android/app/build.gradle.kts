plugins {
  id("com.onesignal.androidsdk.onesignal-gradle-plugin")
  id("com.android.application")
  id("kotlin-android")
  id("kotlin-kapt")
  id("dagger.hilt.android.plugin")
  id("com.google.secrets_gradle_plugin")
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
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.viewmodelKtx)
  implementation(libs.androidx.lifecycle.livedataKtx)
  implementation(libs.androidx.navigation.fragmentKtx)
  implementation(libs.androidx.material)
  implementation(libs.androidx.navigation.uiKtx)
  implementation(libs.androidx.preferenceKtx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.androidx.room.ktx)
  implementation(libs.dagger.hilt.android)
  implementation(libs.onesignal)

  kapt(libs.androidx.room.compiler)
  kapt(libs.dagger.hilt.compiler)
}

secrets {
  propertiesFileName = "secrets.properties"
}