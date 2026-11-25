package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TracksAdapter(
    private val initialClick: (Track) -> Unit = {} // Приватное начальное значение
) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    // ПУБЛИЧНОЕ свойство, которое можно менять из Activity
    var clickListener: (Track) -> Unit = initialClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        // ВАЖНО: Передаем в ViewHolder именно clickListener, а не initialClick
        return TrackViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int = tracks.size

    fun refresh() {
        notifyDataSetChanged()
    }
}