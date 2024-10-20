package com.hfad.playlistmaker

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hfad.playlistmaker.databinding.ActivitySearchBinding

class SearchActivity: AppCompatActivity() {

    companion object {
        const val DEFAULT_SEARCH_DATA = ""
        const val KEY_SEARCH_DATA = "KEY_SEARCH_DATA"
    }

    var searchData: String = DEFAULT_SEARCH_DATA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.inputSearch.setText(searchData)
        binding.searchCancelButton.visibility = View.GONE

        binding.topAppBar.setNavigationOnClickListener{ finish() }
        binding.searchCancelButton.setOnClickListener{ binding.inputSearch.setText("") }

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

        binding.inputSearch.addTextChangedListener(textWatcher)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_SEARCH_DATA, searchData)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchData = savedInstanceState.getString(KEY_SEARCH_DATA, DEFAULT_SEARCH_DATA)
    }
}