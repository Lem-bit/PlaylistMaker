package com.hfad.playlistmaker.domain.impl

import com.hfad.playlistmaker.domain.api.ActionHandlerInteractor
import com.hfad.playlistmaker.domain.api.ActionHandlerRepository

class ActionHandlerInteractorImpl(private val action: ActionHandlerRepository): ActionHandlerInteractor {
    override fun shareApp() {
        action.shareApp()
    }

    override fun contactSupport() {
        action.contactSupport()
    }

    override fun showLicense() {
        action.showLicense()
    }
}