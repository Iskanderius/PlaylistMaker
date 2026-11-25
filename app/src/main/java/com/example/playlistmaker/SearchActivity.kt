package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.text.Editable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback
import java.net.URLEncoder


class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var TittleSearch: MaterialToolbar
    private lateinit var clearHistiry: MaterialButton

    private lateinit var  mesBreakConnect : LinearLayout
    private lateinit var  mesBreakSearch: LinearLayout
    private lateinit var rvTrack :RecyclerView
    private lateinit var rvTrackHistory :RecyclerView
    private lateinit var  searchBlock: ConstraintLayout
    private lateinit var  historyBlock: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchHistoryHelper: SearchHistory
    private var searchInput: String = AMOUNT_DEF
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PRODUCT_AMOUNT, searchInput)
    }

    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
        const val AMOUNT_DEF = ""
        const val HISTORY_TRACKS_KEY = "history_tracks"  // ключ для списка треков
        const val MAX_HISTORY_SIZE = 10  // максимум треков в истории
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

    private val tracks = ArrayList<Track>()
    private val adapter = TracksAdapter { selectedTrack ->
        addTrackToHistory(selectedTrack) // Сохраняем только выбранный трек
    }
    private var tracksHistory = ArrayList<Track>()
    private val adapterHistoryList = TracksAdapter { selectedTrack ->
        //Toast.makeText(this, "Выбран трек: ${selectedTrack.trackName}", Toast.LENGTH_SHORT).show()
    }

    private fun loadHistoryFromPreferences() {
        val loadedList = searchHistoryHelper.loadHistory()
        tracksHistory.clear()
        tracksHistory.addAll(loadedList)
        adapterHistoryList.tracks = tracksHistory
        adapterHistoryList.notifyDataSetChanged()
    }

    private fun addTrackToHistory(track: Track) {
        val updatedList = searchHistoryHelper.addTrack(tracksHistory, track)
        tracksHistory.clear()
        tracksHistory.addAll(updatedList)
        adapterHistoryList.notifyDataSetChanged()
        searchHistoryHelper.saveHistory(tracksHistory)
        }

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

        rvTrack = findViewById<RecyclerView>(R.id.rvTrack)
        adapter.tracks = tracks
        rvTrack.adapter = adapter

        rvTrackHistory = findViewById<RecyclerView>(R.id.historyList)
        adapterHistoryList.tracks = tracksHistory
        rvTrackHistory.adapter = adapterHistoryList // без повторного создания адаптера
        sharedPreferences  = getSharedPreferences("history_tracks", MODE_PRIVATE)
        searchHistoryHelper = SearchHistory(sharedPreferences)
        loadHistoryFromPreferences()

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
                        tracks.clear()
                        adapter.notifyDataSetChanged()
                        rvTrack.visibility= View.GONE
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        mesBreakConnect = findViewById<LinearLayout>(R.id.BreakConnect)
        mesBreakSearch = findViewById<LinearLayout>(R.id.BreakSearch)
        val buttonUpdate = findViewById<MaterialButton>(R.id.Update)
        searchBlock = findViewById<ConstraintLayout>(R.id.searchBlock)
        historyBlock= findViewById<LinearLayout>(R.id.historyBlock)
        clearHistiry= findViewById<MaterialButton>(R.id.clearHistiry)
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

         clearHistiry.setOnClickListener {
            tracksHistory.clear()
            adapterHistoryList.notifyDataSetChanged()
            searchHistoryHelper.saveHistory(emptyList())
            historyBlock.visibility = View.GONE
         }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchInput = s.toString()
                if (s.isNullOrEmpty()) {
                    inputEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.loupe, 0, 0, 0)
                    if ((inputEditText.hasFocus()) && !tracks.isEmpty()) {
                        historyBlock.visibility = View.VISIBLE
                        searchBlock.visibility = View.GONE
                    } else {
                        historyBlock.visibility = View.GONE
                        searchBlock.visibility = View.VISIBLE
                    }
                } else {
                    inputEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.loupe, 0, R.drawable.ic_menu_close_clear_cancel, 0)
                        historyBlock.visibility = View.GONE
                        searchBlock.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        // логика по работе с введённым значением
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setText(searchInput)

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            loadHistoryFromPreferences()
            var bbb = tracks.isEmpty()
            historyBlock.visibility = if (hasFocus && inputEditText.text.isEmpty() && (!tracksHistory.isEmpty())) View.VISIBLE else View.GONE
            searchBlock.visibility = if (historyBlock.visibility == View.GONE) View.VISIBLE else View.GONE
        }
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
                                    //adapter.notifyDataSetChanged()
                                }
                                //adapter.notifyDataSetChanged()
                                mesBreakConnect.visibility= View.GONE
                                mesBreakSearch.visibility= View.GONE
                                rvTrack.visibility= View.VISIBLE
                            }
                            if (tracks.isEmpty()) {
                                //adapter.notifyDataSetChanged()
                                mesBreakConnect.visibility= View.GONE
                                mesBreakSearch.visibility= View.VISIBLE
                            }
                        } else {
                            tracks.clear()
                            //adapter.notifyDataSetChanged()
                            mesBreakConnect.visibility= View.VISIBLE
                            mesBreakSearch.visibility= View.GONE

                        }
                        adapter.notifyDataSetChanged()
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