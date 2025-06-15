package com.hfad.playlistmaker.domain.api

interface AppThemeInteractor {
    fun getCurrentTheme(): Boolean
    fun applyTheme()
    fun switchTheme(darkThemeEnabled: Boolean)
}