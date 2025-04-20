package com.hfad.playlistmaker.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hfad.playlistmaker.AppConst

object SearchHistory {

    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private var trackList: MutableList<Track> = mutableListOf()

    fun init(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }

    fun load() {
        val json = sharedPreferences.getString(AppConst.KEY_TRACKS, null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Track>>() {}.type
            trackList = gson.fromJson(json, type) ?: mutableListOf()
        }
    }

    fun save() {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(trackList)
        editor.putString(AppConst.KEY_TRACKS, json)
        editor.apply()
    }

    fun get(): List<Track> {
        return trackList.toList()
    }

    fun add(track: Track) {
        trackList.removeAll { it.trackId == track.trackId }
        trackList.add(0, track)
        if (trackList.size > AppConst.MAX_TRACKS) {
            trackList.removeAt(trackList.size - 1)
        }
        save()
    }

}