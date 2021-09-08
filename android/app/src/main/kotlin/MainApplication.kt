package me.sanchithhegde.wastecollection

import android.app.Application
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    instance = this

    // Logging set to help debug issues, remove before releasing your app.
    OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

    // OneSignal Initialization
    OneSignal.initWithContext(this)
    OneSignal.setAppId(BuildConfig.ONESIGNAL_APP_ID)
  }

  companion object {
    lateinit var instance: MainApplication
      private set
  }
}
