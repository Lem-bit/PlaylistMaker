package com.hfad.playlistmaker.domain.impl

import com.hfad.playlistmaker.domain.api.AppThemeInteractor
import com.hfad.playlistmaker.domain.api.AppThemeRepository

class AppThemeInteractorImpl(private val theme: AppThemeRepository): AppThemeInteractor {
    override fun getCurrentTheme(): Boolean {
        return theme.getCurrentTheme()
    }

    override fun applyTheme() {
        theme.applyTheme()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        theme.switchTheme(darkThemeEnabled)
    }
}