package com.hfad.playlistmaker.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("search")
    fun search(
        @Query("term") term: String,
        @Query("entity") entity: String = "song"
    ): Call<SearchResponse>
}