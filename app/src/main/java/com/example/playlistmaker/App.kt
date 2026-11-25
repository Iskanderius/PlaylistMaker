package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var  darkTheme : Boolean = false

    override fun onCreate() {
        super.onCreate()

        val prefs = getSharedPreferences("user_token", MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_theme_key", false)
        switchTheme(isDark)
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
    }
}
