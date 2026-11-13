package com.example.playlistmaker

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val tvArtworkUrl100: ImageView = itemView.findViewById(R.id.tvArtworkUrl100)
    private val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)
    private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)

    fun bind(item: Track) {
        tvArtistName.text = item.artistName
        if (item.trackTimeMillis==null){
            tvTrackTime.text = ""
        }
        else if (3600000L > item.trackTimeMillis.toInt()){
            tvTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis.toLong())
        }
        else{
            tvTrackTime.text = ((SimpleDateFormat("hh", Locale.getDefault()).format(item.trackTimeMillis.toLong()).toInt()*60)+SimpleDateFormat("mm", Locale.getDefault()).format(item.trackTimeMillis.toLong()).toInt()).toString()+':'+SimpleDateFormat("ss", Locale.getDefault()).format(item.trackTimeMillis.toLong())
        }
        tvTrackName.text = item.trackName
        Glide.with(itemView.context)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(dpToPx(2.toFloat(),itemView)))
            .into(tvArtworkUrl100)
    }

    fun dpToPx(dp: Float, context: View): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}

