package com.hfad.playlistmaker.domain.api

import com.hfad.playlistmaker.domain.models.Track

interface TracksRepository {
    fun search(expression: String): List<Track>
}