package com.hfad.playlistmaker.domain.api

import com.hfad.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun getList(): MutableList<Track>
    fun add(track: Track)
    fun checkConditionsAndAdd(track: Track, tracks: MutableList<Track>) : MutableList<Track>
    fun save(tracks: MutableList<Track>)
    fun clear()
}