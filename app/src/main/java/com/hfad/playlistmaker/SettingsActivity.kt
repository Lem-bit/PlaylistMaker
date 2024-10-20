package com.hfad.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.hfad.playlistmaker.databinding.ActivityMainBinding
import com.hfad.playlistmaker.databinding.ActivitySettingsBinding
import java.util.Locale

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.topAppBar.setNavigationOnClickListener{ finish() }
        binding.buttonShare.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.link_course))
            }

            startActivity(Intent.createChooser(intent, getString(R.string.share_message)))
        }

        binding.buttonUser.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.link_agreement))
            }

            startActivity(intent)
        }

        binding.buttonSupport.setOnClickListener{
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(String.format(locale = Locale.getDefault(), "mailto:%s?subject=%s&body=%s",
                    Uri.encode(getString(R.string.email)),
                    Uri.encode(getString(R.string.email_title)),
                    Uri.encode(getString(R.string.email_message))
                ))
            }

            startActivity(intent)
        }

    }
}