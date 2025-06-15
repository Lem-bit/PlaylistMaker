package com.hfad.playlistmaker.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.domain.models.Track
import com.hfad.playlistmaker.databinding.ActivityTrackRowBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TracksViewHolder(private val binding: ActivityTrackRowBinding): RecyclerView.ViewHolder(binding.root) {

    private fun MillisToMins(duration: Long): String {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        return dateFormat.format(duration)
    }

    fun bind(track: Track) {

        binding.TrackRowLbTrackName.text = track.trackName
        binding.TrackRowLbTrackArtist.text = String.format("%s • %s", track.artistName, MillisToMins(track.trackTime))

        Glide.with(binding.TrackRowLayout.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .fitCenter()
            .centerCrop()
            .into(binding.imageView)


    }
}