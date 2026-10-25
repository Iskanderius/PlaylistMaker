package com.example.playlistmaker

import android.os.Bundle
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.text.Editable
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar


class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText

    //private lateinit var searchClearIcon: ImageView
    private lateinit var TittleSearch: MaterialToolbar





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        TittleSearch = findViewById<MaterialToolbar>(R.id.TittleSearch)
        TittleSearch.setNavigationOnClickListener{
            finish()
        }

        inputEditText = findViewById<EditText>(R.id.inputEditText)
        inputEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableRight = inputEditText.compoundDrawables[2]
                if (drawableRight != null) {
                    val clickAreaStart = inputEditText.right - drawableRight.bounds.width() - inputEditText.paddingRight
                    if (event.rawX >= clickAreaStart) {
                        inputEditText.setText("")
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchInput = s.toString()
                if (s.isNullOrEmpty()) {
                    inputEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.loupe, 0, 0, 0)
                } else {
                    inputEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.loupe, 0, R.drawable.ic_menu_close_clear_cancel, 0)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
            }
            // логика по работе с введённым значением
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setText(searchInput)

        val trackAdapter = TrackAdapter (
            listOf(
                Track(    getString(R.string.trackName_1), getString(R.string.artistName_1), getString(R.string.trackTime_1),getString(R.string.artworkUrl100_1)),
                Track(    getString(R.string.trackName_2), getString(R.string.artistName_2), getString(R.string.trackTime_2),getString(R.string.artworkUrl100_2)),
                Track(    getString(R.string.trackName_3), getString(R.string.artistName_3), getString(R.string.trackTime_3),getString(R.string.artworkUrl100_3)),
                Track(    getString(R.string.trackName_4), getString(R.string.artistName_4), getString(R.string.trackTime_4),getString(R.string.artworkUrl100_4)),
                Track(    getString(R.string.trackName_5), getString(R.string.artistName_5), getString(R.string.trackTime_5),getString(R.string.artworkUrl100_5)),

            )
        )
        val rvTrack = findViewById<RecyclerView>(R.id.rvTrack)
        rvTrack.adapter = trackAdapter

    }

    private var searchInput: String = AMOUNT_DEF
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PRODUCT_AMOUNT, searchInput)
    }

    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
        const val AMOUNT_DEF = ""
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Вторым параметром мы передаём значение по умолчанию
        searchInput = savedInstanceState.getString(PRODUCT_AMOUNT, AMOUNT_DEF)
    }

}










