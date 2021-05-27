package me.sanchithhegde.wastecollection

import android.os.Bundle
import android.view.Menu
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.onesignal.OneSignal

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val settingsMenuItem = menu.findItem(R.id.settingsFragment)
        settingsMenuItem.isVisible = false
    }

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
