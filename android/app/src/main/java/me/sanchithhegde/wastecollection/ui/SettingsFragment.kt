package me.sanchithhegde.wastecollection.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.onesignal.OneSignal
import me.sanchithhegde.wastecollection.R

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

        val locationKey = getString(R.string.pref_location)
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

        val themeKey = getString(R.string.pref_theme)
        val themePreference = findPreference<ListPreference>(themeKey)

        themePreference?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                AppCompatDelegate.setDefaultNightMode((newValue as String).toInt())

                true
            }
    }
}
