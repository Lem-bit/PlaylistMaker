package com.hfad.playlistmaker.domain.api

interface AppThemeRepository {
    fun getCurrentTheme(): Boolean
    fun applyTheme()
    fun switchTheme(darkThemeEnabled: Boolean)
}