package com.hfad.playlistmaker

import android.app.Application
import android.app.Application.MODE_PRIVATE
import android.media.MediaPlayer
import com.hfad.playlistmaker.data.ActionHandlerRepositoryImpl
import com.hfad.playlistmaker.data.AppThemeRepositoryImpl
import com.hfad.playlistmaker.data.PlayerRepositoryImpl
import com.hfad.playlistmaker.data.SearchHistoryRepositoryImpl
import com.hfad.playlistmaker.data.TracksRepositoryImpl
import com.hfad.playlistmaker.data.local.LocalDataSourceImpl
import com.hfad.playlistmaker.data.local.LocalDataSourceImpl.Companion.PLAYLIST_MAKER_PREFERENCES
import com.hfad.playlistmaker.data.network.ITunesAPI
import com.hfad.playlistmaker.data.network.RetrofitClient
import com.hfad.playlistmaker.data.network.RetrofitClient.Companion.BASE_URL
import com.hfad.playlistmaker.domain.api.ActionHandlerInteractor
import com.hfad.playlistmaker.domain.api.ActionHandlerRepository
import com.hfad.playlistmaker.domain.api.AppThemeInteractor
import com.hfad.playlistmaker.domain.api.AppThemeRepository
import com.hfad.playlistmaker.domain.api.LocalDataSource
import com.hfad.playlistmaker.domain.api.PlayerInteractor
import com.hfad.playlistmaker.domain.api.PlayerRepository
import com.hfad.playlistmaker.domain.api.SearchHistoryInteractor
import com.hfad.playlistmaker.domain.api.SearchHistoryRepository
import com.hfad.playlistmaker.domain.api.TracksInteractor
import com.hfad.playlistmaker.domain.api.TracksRepository
import com.hfad.playlistmaker.domain.impl.ActionHandlerInteractorImpl
import com.hfad.playlistmaker.domain.impl.AppThemeInteractorImpl
import com.hfad.playlistmaker.domain.impl.PlayerInteractorImpl
import com.hfad.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.hfad.playlistmaker.domain.impl.TracksInteractorImpl
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

    private fun provideLocalDataSource(): LocalDataSource {
        return LocalDataSourceImpl(
            application.getSharedPreferences(
                PLAYLIST_MAKER_PREFERENCES,
                MODE_PRIVATE
            )
        )
    }

    private fun getAppThemeRepository(): AppThemeRepository {
        return AppThemeRepositoryImpl(provideLocalDataSource(), application)
    }

    fun provideAppThemeInteractor(): AppThemeInteractor {
        return AppThemeInteractorImpl(getAppThemeRepository())
    }

    private fun getActionHandlerRepository(): ActionHandlerRepository {
        return ActionHandlerRepositoryImpl(application)
    }

    fun provideActionHandlerInteractor(): ActionHandlerInteractor {
        return ActionHandlerInteractorImpl(getActionHandlerRepository())
    }

    private fun getGson(): Gson {
        return Gson()
    }

    private fun getApiService(): ITunesAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val iTunesService: ITunesAPI by lazy {
            retrofit.create(ITunesAPI::class.java)
        }

        return iTunesService
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitClient(getApiService()))
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(provideLocalDataSource(), getGson())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    private fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(getMediaPlayer())
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
}