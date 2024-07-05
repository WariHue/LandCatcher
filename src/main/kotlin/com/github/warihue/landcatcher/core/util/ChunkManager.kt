package com.github.warihue.landcatcher.core.util

import com.github.warihue.landcatcher.Team
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import org.bukkit.Chunk

object ChunkManager {
    private fun addChunk(team: Team, chunk: Chunk):Boolean {
        if(!checkNearChunk(chunk, team))
            return false
        removeChunk(chunk)
        LandCatcherPlugin.instance.chunks[team]!!.add(Pair(chunk.x, chunk.z))
        return true
    }

    private fun checkNearChunk(chunk: Chunk, team: Team): Boolean {
        if(LandCatcherPlugin.instance.chunks[team]!!.none { pair: Pair<Int, Int> -> pair == Pair(chunk.x, chunk.z) }){
            if(LandCatcherPlugin.instance.chunks[team]!!.any { pair: Pair<Int, Int> -> pair == Pair(chunk.x + 1, chunk.z) }) return true
            if(LandCatcherPlugin.instance.chunks[team]!!.any { pair: Pair<Int, Int> -> pair == Pair(chunk.x, chunk.z + 1) }) return true
            if(LandCatcherPlugin.instance.chunks[team]!!.any { pair: Pair<Int, Int> -> pair == Pair(chunk.x - 1, chunk.z) }) return true
            if(LandCatcherPlugin.instance.chunks[team]!!.any { pair: Pair<Int, Int> -> pair == Pair(chunk.x, chunk.z - 1) }) return true
            return false
        }else{
            return false
        }
    }

    private fun checkChunkTaking(chunk: Chunk): Boolean {
        return LandCatcherPlugin.instance.chunks.values.any { pairs: MutableList<Pair<Int, Int>>? -> pairs!!.any{ pair: Pair<Int, Int> -> pair == Pair(chunk.x, chunk.z) } }
    }

    private fun removeChunk(chunk: Chunk) {
        LandCatcherPlugin.instance.chunks[Team.NONE]!!.remove(Pair(chunk.x, chunk.z))
        LandCatcherPlugin.instance.chunks[Team.BLUE]!!.remove(Pair(chunk.x, chunk.z))
        LandCatcherPlugin.instance.chunks[Team.GREEN]!!.remove(Pair(chunk.x, chunk.z))
        LandCatcherPlugin.instance.chunks[Team.YELLOW]!!.remove(Pair(chunk.x, chunk.z))
        LandCatcherPlugin.instance.chunks[Team.RED]!!.remove(Pair(chunk.x, chunk.z))
    }

    fun addChunkOnBlank(team: Team, chunk: Chunk): Boolean {
        if(!checkChunkTaking(chunk)){
            return addChunk(team, chunk)
        }
        return false
    }

    fun addChunkOnAnother(team: Team, chunk: Chunk): Boolean {
        return addChunk(team, chunk)
    }
}