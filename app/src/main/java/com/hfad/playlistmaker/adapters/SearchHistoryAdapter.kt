package com.hfad.playlistmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hfad.playlistmaker.domain.models.Track
import com.hfad.playlistmaker.databinding.ActivityTrackRowBinding
import com.hfad.playlistmaker.ui.search.TracksViewHolder

class SearchHistoryAdapter(private val trackList: List<Track>): RecyclerView.Adapter<TracksViewHolder>() {

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val binding = ActivityTrackRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TracksViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}