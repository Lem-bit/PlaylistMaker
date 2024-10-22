package com.hfad.playlistmaker.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.data.Track
import com.hfad.playlistmaker.databinding.ActivityTrackRowBinding

class SearchTrackHolder(private val binding: ActivityTrackRowBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(track: Track) {

        binding.TrackRowLbTrackName.text = track.trackName
        binding.TrackRowLbTrackArtist.text = String.format("%s • %s", track.artistName, track.trackTime)

        Glide.with(binding.TrackRowLayout.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_no_image)
            .fitCenter()
            .centerCrop()
            .into(binding.imageView)

    }
}