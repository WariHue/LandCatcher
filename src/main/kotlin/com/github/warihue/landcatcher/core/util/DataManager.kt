package com.github.warihue.landcatcher.core.util

import com.google.gson.Gson
import org.bukkit.Chunk

object DataManager {
    fun convertChunkDataToJson(data: HashMap<Pair<Int, Int>, Chunk>): String {
        return Gson().toJson(data.keys)
    }
}