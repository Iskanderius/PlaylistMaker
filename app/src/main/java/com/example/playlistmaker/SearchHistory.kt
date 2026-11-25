package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

// Используем константы из SearchActivity, чтобы не дублировать их
class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun loadHistory(): List<Track> {
        // Берем ключ из SearchActivity.Companion
        val jsonString = sharedPreferences.getString(SearchActivity.HISTORY_TRACKS_KEY, null)

        return if (jsonString != null) {
            val gson = Gson()
            // Парсим JSON в список треков
            gson.fromJson(jsonString, Array<Track>::class.java).toList()
        } else {
            emptyList()
        }
    }

    fun saveHistory(tracks: List<Track>) {
        val gson = Gson()
        val jsonString = gson.toJson(tracks)

        sharedPreferences.edit()
            .putString(SearchActivity.HISTORY_TRACKS_KEY, jsonString)
            .apply()
    }

    fun addTrack(existingList: List<Track>, track: Track): List<Track> {
        val mutableList = existingList.toMutableList()

        // 1. Удаляем старый трек по trackId (если он уже есть в истории)
        mutableList.removeIf { it.trackId == track.trackId }

        // 2. Добавляем новый трек в самое начало списка
        mutableList.add(0, track)

        // 3. Обрезаем список, если он стал больше 10 элементов
        if (mutableList.size > SearchActivity.MAX_HISTORY_SIZE) {
            return mutableList.subList(0, SearchActivity.MAX_HISTORY_SIZE)
        }

        return mutableList
    }
}