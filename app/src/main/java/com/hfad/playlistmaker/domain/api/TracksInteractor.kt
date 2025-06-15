package com.hfad.playlistmaker.domain.api

import com.hfad.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun search(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }
}