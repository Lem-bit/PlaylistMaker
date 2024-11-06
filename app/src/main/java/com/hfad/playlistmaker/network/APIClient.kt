package com.hfad.playlistmaker.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object APIClient {
    private const val BASE_URL = "https://itunes.apple.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val iTunesAPI: ITunesAPI by lazy {
        retrofit.create(ITunesAPI::class.java)
    }
}