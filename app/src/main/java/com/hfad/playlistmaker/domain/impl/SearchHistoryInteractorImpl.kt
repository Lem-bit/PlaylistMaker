package com.hfad.playlistmaker.domain.impl

import com.hfad.playlistmaker.domain.api.SearchHistoryInteractor
import com.hfad.playlistmaker.domain.api.SearchHistoryRepository
import com.hfad.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun getList(): MutableList<Track> {
        return repository.getList()
    }

    override fun add(track: Track) {
        repository.add(track)
    }

    override fun checkConditionsAndAdd(
        track: Track,
        tracks: MutableList<Track>
    ): MutableList<Track> {
        return repository.checkConditionsAndAdd(track, tracks)
    }

    override fun save(tracks: MutableList<Track>) {
        repository.save(tracks)
    }

    override fun clear() {
        repository.clear()
    }
}