package com.hfad.playlistmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.data.Track
import com.hfad.playlistmaker.databinding.ActivityTrackRowBinding

class SearchTrackAdapter(private val trackList: List<Track>): RecyclerView.Adapter<SearchTrackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActivityTrackRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    class ViewHolder(private val binding: ActivityTrackRowBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {

            binding.TrackRowLbTrackName.text = String.format("%s • %s", track.artistName, track.trackTime)
            binding.TrackRowLbArtistName.text = track.artistName

            Glide.with(binding.TrackRowLayout.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.ic_no_image)
                .fitCenter()
                .centerCrop()
                .into(binding.imageView)

        }
    }
}