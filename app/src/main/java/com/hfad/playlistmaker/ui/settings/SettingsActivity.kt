package com.hfad.playlistmaker.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hfad.playlistmaker.Creator
import com.hfad.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private var _binding: ActivitySettingsBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivitySettingsBinding must not be null!")

    private val appThemeInteractor = Creator.provideAppThemeInteractor()
    private val actionHandler = Creator.provideActionHandlerInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        with (binding.settingsTheme) {
            isChecked = appThemeInteractor.getCurrentTheme()
            setOnCheckedChangeListener { _, isChecked ->
                appThemeInteractor.switchTheme(isChecked)
            }
        }

        binding.settingsShare.setOnClickListener {
            actionHandler.shareApp()
        }

        binding.settingsSupport.setOnClickListener {
            actionHandler.contactSupport()
        }

        binding.settingsLicense.setOnClickListener {
            actionHandler.showLicense()
        }
    }
}