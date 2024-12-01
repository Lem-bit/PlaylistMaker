package com.hfad.playlistmaker.network

import com.hfad.playlistmaker.data.Track

data class SearchResponse(
    val resultCount: Int,
    val results: List<Track>
)
