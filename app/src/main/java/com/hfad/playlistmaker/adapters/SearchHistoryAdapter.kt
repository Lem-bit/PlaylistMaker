package com.hfad.playlistmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hfad.playlistmaker.data.Track
import com.hfad.playlistmaker.databinding.ActivityTrackRowBinding

class SearchHistoryAdapter(private val trackList: List<Track>): RecyclerView.Adapter<SearchTrackHolder>() {

    override fun onBindViewHolder(holder: SearchTrackHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackHolder {
        val binding = ActivityTrackRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchTrackHolder(binding)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}