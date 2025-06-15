package com.hfad.playlistmaker.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.domain.api.ActionHandlerRepository

class ActionHandlerRepositoryImpl(private val context: Context) : ActionHandlerRepository {
    override fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.link_course))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun contactSupport() {
        val sendMailToSupport = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.email)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_title))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_message))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(sendMailToSupport)
    }

    override fun showLicense() {
        val showLicenseInBrowser = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(context.getString(R.string.link_agreement))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(showLicenseInBrowser)
    }
}