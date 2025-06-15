package com.hfad.playlistmaker

import android.app.Application
import com.hfad.playlistmaker.domain.api.AppThemeInteractor

class App : Application() {

    private lateinit var appThemeInteractor: AppThemeInteractor

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        appThemeInteractor = Creator.provideAppThemeInteractor()
        appThemeInteractor.applyTheme()
    }
}