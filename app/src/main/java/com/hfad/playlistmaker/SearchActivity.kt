package com.hfad.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.hfad.playlistmaker.adapters.SearchTrackAdapter
import com.hfad.playlistmaker.data.SearchHistory
import com.hfad.playlistmaker.data.Track
import com.hfad.playlistmaker.databinding.ActivitySearchBinding
import com.hfad.playlistmaker.network.APIClient
import com.hfad.playlistmaker.network.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity: AppCompatActivity() {

    private companion object {
        const val KEY_SEARCH_DATA = "KEY_SEARCH_DATA"
    }

    private var searchData: String = ""
    private val trackList: MutableList<Track> = mutableListOf()
    private lateinit var binding: ActivitySearchBinding
    private val sharedPreferences by lazy {
        getSharedPreferences(AppConst.PREFS_NAME, MODE_PRIVATE)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        enableEdgeToEdge()

        SearchHistory.init(sharedPreferences)
        SearchHistory.load()

        trackList.addAll(SearchHistory.get())

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
            }

            override fun afterTextChanged(s: Editable?) {
                searchData.plus(s)
            }
        }

        with(binding) {
            topAppBar.title = resources.getString(R.string.activitysearch_title)

            recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            recyclerView.adapter = SearchTrackAdapter(trackList) { track ->
                SearchHistory.add(track)
                //onClickTrack()
            }

            inputSearch.setText(searchData)
            searchCancelButton.visibility = View.GONE

            buttonClearHistory.setOnClickListener{
                clearHistory()
            }

            topAppBar.setNavigationOnClickListener{ finish() }
            searchCancelButton.setOnClickListener{
                binding.inputSearch.setText("")
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(binding.searchCancelButton.windowToken, 0)
                onClickTrack()
                binding.recyclerView.adapter?.notifyDataSetChanged()
            }

            buttonRefresh.setOnClickListener{
                searchTracks(inputSearch.text.toString())
            }

            inputSearch.addTextChangedListener(textWatcher)
            inputSearch.setOnEditorActionListener{_, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchTracks(inputSearch.text.toString())
                    binding.buttonClearHistory.visibility = View.GONE
                    topAppBar.title = resources.getString(R.string.activitysearch_search_text)
                    true
                }
                false
            }

            updateStateTrackList()
            hideError()
        }
    }

    private fun onClickTrack() {
        hideError()
        trackList.clear()
        trackList.addAll(SearchHistory.get())
        binding.topAppBar.title = resources.getString(R.string.activitysearch_title)
        updateStateTrackList()
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
    private fun searchTracks(term: String) {
        trackList.clear()
        binding.recyclerView.adapter?.notifyDataSetChanged()

        APIClient.iTunesAPI.search(term).enqueue(object: Callback<SearchResponse> {

            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {

                if (response.isSuccessful) {
                    val searchResponse = response.body()

                    if (searchResponse?.resultCount!! > 0) {
                        hideError()
                        trackList.addAll(searchResponse.results)
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                    } else {
                        showErrorTrackNotFound()
                    }

                }

            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                showErrorInternetNotFound()
                binding.recyclerView.adapter?.notifyDataSetChanged()
                updateStateTrackList()
            }
        })


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearHistory() {
        trackList.clear()
        SearchHistory.clear()
        SearchHistory.save()
        binding.recyclerView.adapter?.notifyDataSetChanged()
        updateStateTrackList()
    }

    private fun updateStateTrackList() {
        binding.buttonClearHistory.visibility = if (trackList.isEmpty()) View.GONE else View.VISIBLE
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
}