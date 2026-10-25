package com.example.playlistmaker

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val tvArtworkUrl100: ImageView = itemView.findViewById(R.id.tvArtworkUrl100)
    private val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)

    fun bind(item: Track) {
        tvArtistName.text = item.artistName
        tvTrackTime.text = item.trackTime
        tvTrackName.text = item.trackName
        Glide.with(itemView.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(dpToPx(2.toFloat(),itemView)))
            .skipMemoryCache(true) // Отключаем кэш в памяти
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Отключаем кэш на диске
            .into(tvArtworkUrl100)
    }

    fun dpToPx(dp: Float, context: View): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}

