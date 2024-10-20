package com.hfad.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindActivity(R.id.btn_search, SearchActivity::class)
        bindActivity(R.id.btn_media, MediaActivity::class)
        bindActivity(R.id.btn_settings, SettingsActivity::class)

    }

    fun bindActivity(id: Int, cls: KClass<out AppCompatActivity>) {
        findViewById<Button>(id).setOnClickListener{ startActivity(Intent(this, cls.java)) }
    }

}