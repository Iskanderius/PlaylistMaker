package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

private lateinit var TittleSetting: MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val themeSwitcher = findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.themeSwitcher)
        val shareApp = findViewById<android.widget.TextView>(R.id.shareApp)
        val writeToSupport = findViewById<android.widget.TextView>(R.id.WriteToSupport)
        val userContract = findViewById<android.widget.TextView>(R.id.UserContract)

        TittleSetting = findViewById<MaterialToolbar>(R.id.TittleSetting)
        TittleSetting.setNavigationOnClickListener{
            finish()
        }

        /*val isDark = resources.configuration.uiMode  and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        switchTheme.isChecked = isDark
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            val mode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
            recreate()
        }*/
        val prefs = getSharedPreferences("user_token", MODE_PRIVATE)
        themeSwitcher.isChecked = prefs.getBoolean("dark_theme_key", false)
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            getSharedPreferences("user_token", MODE_PRIVATE)
                .edit()
                .putBoolean("dark_theme_key", checked)
                .apply()
            (application as App).switchTheme(checked)
            recreate() // Если нужна немедленная перерисовка текущей активности
        }



        shareApp.setOnClickListener {

            val message = getString(R.string.urlProfile)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = getString(R.string.settingIntentType)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            Intent.createChooser(shareIntent, getString(R.string.share_url))
            startActivity(shareIntent)
        }

        writeToSupport.setOnClickListener {
            val message = getString(R.string.textMessage)
            val theme = getString(R.string.textTheme)
            val writeIntent = Intent(Intent.ACTION_SENDTO)
            writeIntent.type = getString(R.string.settingIntentType)
            writeIntent.data = Uri.parse(getString(R.string.settingIntentWrType))
            writeIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            writeIntent.putExtra(Intent.EXTRA_TEXT, message)
            writeIntent.putExtra(Intent.EXTRA_SUBJECT, theme)
            startActivity(writeIntent)
        }

        userContract.setOnClickListener {
            val intentContract = Intent(Intent.ACTION_VIEW)
            intentContract.data = Uri.parse(getString(R.string.urlDocument))
            startActivity(intentContract)
        }
    }
}

