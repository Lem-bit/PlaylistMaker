package com.hfad.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.ui.settings.SettingsActivity
import com.hfad.playlistmaker.databinding.ActivityMainBinding
import com.hfad.playlistmaker.ui.media.MediaActivity
import com.hfad.playlistmaker.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnMedia.setOnClickListener{ startActivity(Intent(this, MediaActivity::class.java)) }
        binding.btnSettings.setOnClickListener{ startActivity(Intent(this, SettingsActivity::class.java)) }
        binding.btnSearch.setOnClickListener{ startActivity(Intent(this, SearchActivity::class.java)) }
    }

}