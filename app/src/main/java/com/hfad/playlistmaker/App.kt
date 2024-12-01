package com.hfad.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

    var darkTheme = false
    private val sharedPreferences by lazy {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()

        if (sharedPreferences.contains(PREFS_KEY_THEME))
            darkTheme = sharedPreferences.getBoolean(PREFS_KEY_THEME, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit().putBoolean(PREFS_KEY_THEME, darkThemeEnabled).apply()
    }

    fun isDarkTheme(): Boolean {
        return darkTheme
    }

    companion object {
        const val PREFS_NAME = "playlist_prefs"
        const val PREFS_KEY_THEME = "theme"
    }
}