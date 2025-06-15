package com.hfad.playlistmaker.network

import com.hfad.playlistmaker.domain.models.Track

data class SearchResponse(
    val resultCount: Int,
    val results: List<Track>
)
