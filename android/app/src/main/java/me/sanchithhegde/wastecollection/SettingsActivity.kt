package me.sanchithhegde.wastecollection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.onesignal.OneSignal

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val locationKey = R.string.pref_location.toString()
            val locationPreference =
                findPreference<EditTextPreference>(locationKey)

            locationPreference?.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->
                    OneSignal.getTags { jsonObject ->
                        if (jsonObject == null ||
                            jsonObject.getString(locationKey) != newValue.toString()
                        ) {
                            OneSignal.sendTag(locationKey, newValue.toString())
                        }
                    }

                    true
                }
        }
    }
}
