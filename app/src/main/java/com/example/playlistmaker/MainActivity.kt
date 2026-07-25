package com.example.playlistmaker
import android.view.View
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val SearchButton = findViewById<android.widget.Button>(R.id.button1)
        val MediaButton = findViewById<android.widget.Button>(R.id.button2)
        val SettingsButton = findViewById<android.widget.Button>(R.id.button3)

        SearchButton.setOnClickListener {
            val SearchIntent = Intent(this, SearchActivity::class.java)
            startActivity(SearchIntent)
        }
        MediaButton.setOnClickListener {
            val MediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(MediaIntent)
        }
        SettingsButton.setOnClickListener {
            val SettingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(SettingsIntent)
        }


    //    shareButton.setOnClickListener {
     //       // тут будет второй способ
     //   }



    }
}
