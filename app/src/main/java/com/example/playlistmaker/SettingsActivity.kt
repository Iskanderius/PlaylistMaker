package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


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

        val backSettings = findViewById<android.widget.LinearLayout>(R.id.backSettings)
        val shareApp = findViewById<android.widget.TextView>(R.id.shareApp)
        val WriteToSupport = findViewById<android.widget.TextView>(R.id.WriteToSupport)
        val UserContract = findViewById<android.widget.TextView>(R.id.UserContract)

        backSettings.setOnClickListener {
            finish()
        }



        shareApp.setOnClickListener {

            val message = "https://practicum.yandex.ru/profile/android-developer/"
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            Intent.createChooser(shareIntent, "Поделиться ссылкой")
            startActivity(shareIntent)
        }



        WriteToSupport.setOnClickListener {
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val theme = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val WriteIntent = Intent(Intent.ACTION_SENDTO)
            WriteIntent.type = "text/plain"
            WriteIntent.data = Uri.parse("mailto:")
            WriteIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("egortsev92@yandex.ru"))
            WriteIntent.putExtra(Intent.EXTRA_TEXT, message)
            WriteIntent.putExtra(Intent.EXTRA_SUBJECT, theme)
            startActivity(WriteIntent)
        }

            UserContract.setOnClickListener {
                val IntentContract = Intent(Intent.ACTION_VIEW)
                IntentContract.data = Uri.parse("https://yandex.ru/legal/practicum_offer/ru/")
                startActivity(IntentContract)
            }





    }
}