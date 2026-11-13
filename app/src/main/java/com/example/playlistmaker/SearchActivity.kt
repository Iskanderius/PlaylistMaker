package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback

class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var TittleSearch: MaterialToolbar

    private lateinit var  mesBreakConnect : LinearLayout
    private lateinit var  mesBreakSearch: LinearLayout

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

    private val trackBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val TracksService = retrofit.create(trackApi::class.java)

    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private val tracks = ArrayList<Track>()
    private val adapter = TracksAdapter()

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
        TittleSearch.setNavigationOnClickListener {
            finish()
        }

        inputEditText = findViewById<EditText>(R.id.inputEditText)
        inputEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableRight = inputEditText.compoundDrawables[2]
                if (drawableRight != null) {
                    val clickAreaStart =
                        inputEditText.right - drawableRight.bounds.width() - inputEditText.paddingRight
                    if (event.rawX >= clickAreaStart) {
                        inputEditText.setText("")
                        val keyboardOff = inputEditText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        keyboardOff.hideSoftInputFromWindow(inputEditText.windowToken, 0)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        val rvTrack = findViewById<RecyclerView>(R.id.rvTrack)
        adapter.tracks = tracks
        rvTrack.adapter = adapter

        mesBreakConnect = findViewById<LinearLayout>(R.id.BreakConnect)
        mesBreakSearch = findViewById<LinearLayout>(R.id.BreakSearch)
        val buttonUpdate = findViewById<MaterialButton>(R.id.Update)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                commonSearch()
                true
            }
            false
        }
        buttonUpdate.setOnClickListener {
            commonSearch()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchInput = s.toString()
                if (s.isNullOrEmpty()) {
                    inputEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.loupe, 0, 0, 0)
                } else {
                    inputEditText.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.loupe,
                        0,
                        R.drawable.ic_menu_close_clear_cancel,
                        0
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        // логика по работе с введённым значением
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setText(searchInput)
    }

    private fun commonSearch() {

        if (inputEditText.text.isNotEmpty()) {
            TracksService.findTrack(inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {

                                val results = response.body()?.results
                                if (results != null) {
                                    tracks.addAll(results)
                                    adapter.notifyDataSetChanged()
                                }
                                adapter.notifyDataSetChanged()
                                mesBreakConnect.visibility= View.GONE
                                mesBreakSearch.visibility= View.GONE
                            }
                            if (tracks.isEmpty()) {
                                adapter.notifyDataSetChanged()
                                mesBreakConnect.visibility= View.GONE
                                mesBreakSearch.visibility= View.VISIBLE
                            }
                        } else {
                            tracks.clear()
                            adapter.notifyDataSetChanged()
                            mesBreakConnect.visibility= View.VISIBLE
                            mesBreakSearch.visibility= View.GONE

                        }
                    }
                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        tracks.clear()
                        adapter.notifyDataSetChanged()
                        mesBreakConnect.visibility= View.VISIBLE
                        mesBreakSearch.visibility= View.GONE
                    }

                })
        }

    }

}