package com.hfad.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

    var darkTheme = false
    val sharedPreferences by lazy {
        this.getSharedPreferences(AppConst.PREFS_NAME, MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()

        if (sharedPreferences.contains(AppConst.KEY_THEME))
            darkTheme = sharedPreferences.getBoolean(AppConst.KEY_THEME, false)
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
        sharedPreferences.edit().putBoolean(AppConst.KEY_THEME, darkThemeEnabled).apply()
    }

    fun isDarkTheme(): Boolean {
        return darkTheme
    }

}