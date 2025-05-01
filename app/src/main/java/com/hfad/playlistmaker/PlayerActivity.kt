package com.hfad.playlistmaker

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.hfad.playlistmaker.data.Track
import com.hfad.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Error binding ActivityPlayerBinding is null!")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mediaToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val track = Gson().fromJson(intent.getStringExtra("track"), Track::class.java)

        with(binding) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            country.text = track.country
            genre.text = track.primaryGenreName
            duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
            year.text = if (track.releaseDate.isNullOrEmpty()) "" else track.releaseDate.substring(0, 4)

            if (track.collectionName.isNullOrEmpty()) albumDetailsGroup.visibility = View.GONE else album.text = track.collectionName

            Glide.with(albumCover.context)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.ic_placeholder)
                .transform(RoundedCorners(dpToPx(8f, albumCover.context)))
                .into(albumCover)
        }
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

}