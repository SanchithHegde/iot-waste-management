package me.sanchithhegde.wastecollection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onesignal.OneSignal

const val ONESIGNAL_APP_ID = BuildConfig.ONESIGNAL_APP_ID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}