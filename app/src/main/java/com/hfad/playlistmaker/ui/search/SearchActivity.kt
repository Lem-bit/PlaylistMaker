package com.hfad.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.hfad.playlistmaker.Creator
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.databinding.ActivitySearchBinding
import com.hfad.playlistmaker.domain.api.TracksInteractor
import com.hfad.playlistmaker.domain.models.Track
import com.hfad.playlistmaker.ui.player.PlayerActivity

class SearchActivity: AppCompatActivity() {

    private companion object {
        const val KEY_SEARCH_DATA = "KEY_SEARCH_DATA"
        const val CLICK_DEBOUNCE_DELAY = 1_000L
        const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

    private var searchData: String = ""
    private val trackList: MutableList<Track> = mutableListOf()
    private lateinit var binding: ActivitySearchBinding
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTracks(searchData) }

    private val tracksInteractor = Creator.provideTracksInteractor()
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchCancelButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                if (s.isNullOrEmpty()) {
                    showHistoryList()
                } else {
                    searchData = binding.inputSearch.text.toString()
                    binding.buttonClearHistory.visibility = View.GONE
                    binding.textYouSearch.visibility = View.GONE
                    searchDebounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchData.plus(s)
            }
        }

        with(binding) {
            searchToolbar.title = resources.getString(R.string.activitysearch_search_text)

            recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            recyclerView.adapter = TrackAdapter(trackList) { track ->
                searchHistoryInteractor.add(track)
                onTrackClick(track)
            }

            inputSearch.setText(searchData)
            searchCancelButton.visibility = View.GONE

            buttonClearHistory.setOnClickListener{
                clearHistory()
            }

            searchToolbar.setNavigationOnClickListener{ finish() }
            searchCancelButton.setOnClickListener{
                binding.inputSearch.setText("")
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(binding.searchCancelButton.windowToken, 0)
                showHistoryList()
            }

            buttonRefresh.setOnClickListener{
                searchTracks(inputSearch.text.toString())
            }

            inputSearch.addTextChangedListener(textWatcher)
            inputSearch.setOnEditorActionListener{_, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchTracks(inputSearch.text.toString())
                    binding.buttonClearHistory.visibility = View.GONE
                    binding.textYouSearch.visibility = View.GONE
                }
                false
            }

        }

        showHistoryList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistoryList() {
        hideError()
        trackList.clear()
        trackList.addAll(searchHistoryInteractor.getList())
        binding.buttonClearHistory.visibility = if (trackList.isEmpty()) View.GONE else View.VISIBLE
        binding.textYouSearch.visibility = if (trackList.isEmpty()) View.GONE else View.VISIBLE
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_SEARCH_DATA, searchData)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchData = savedInstanceState.getString(KEY_SEARCH_DATA, "")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra("track", Gson().toJson(track))
            }
            startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchTracks(term: String) {
        trackList.clear()
        binding.recyclerView.adapter?.notifyDataSetChanged()
        binding.progressBar.visibility = View.VISIBLE
        hideError()

        if (!isNetworkCheck()) {
            showErrorInternetNotFound()
            binding.recyclerView.adapter?.notifyDataSetChanged()
            binding.buttonClearHistory.visibility = if (trackList.isEmpty()) View.GONE else View.VISIBLE
            binding.progressBar.visibility = View.GONE
            return
        }

        tracksInteractor.search(term, object: TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>) {
                handler.post {
                    binding.progressBar.visibility = View.GONE

                    trackList.clear()
                    if (foundTracks.isNotEmpty()) {
                        hideError()
                        trackList.addAll(foundTracks)
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                    } else {
                        showErrorTrackNotFound()
                    }
                }
            }
        })

    }

    private fun isNetworkCheck(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.activeNetwork?.let { connectivityManager.getNetworkCapabilities(it) }
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearHistory() {
        searchHistoryInteractor.clear()
        showHistoryList()
    }

    private fun showErrorTrackNotFound() {
        showError(R.drawable.ic_search_not_found, R.string.search_error_tracks_not_found, false)
    }

    private fun showErrorInternetNotFound() {
        showError(R.drawable.ic_internet_not_found, R.string.search_error_internet_not_found, true)
    }

    private fun showError(imgResource: Int, textResource: Int, showButton: Boolean) {
        binding.imageState.setImageResource(imgResource)
        binding.textState.text = getString(textResource)

        binding.imageState.visibility = View.VISIBLE
        binding.textState.visibility = View.VISIBLE
        binding.buttonRefresh.visibility = if (showButton) View.VISIBLE else View.GONE
    }

    private fun hideError() {
        binding.imageState.visibility = View.GONE
        binding.textState.visibility = View.GONE
        binding.buttonRefresh.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    private fun clickDebounce(): Boolean {
        val currentClickState = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return currentClickState
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

}