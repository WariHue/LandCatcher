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

    private fun checkChuckOwner(chunk: Chunk): Team {
        if(LandCatcherPlugin.instance.chunks[Team.BLUE]!!.contains(Pair(chunk.x, chunk.z))) return Team.BLUE
        if(LandCatcherPlugin.instance.chunks[Team.RED]!!.contains(Pair(chunk.x, chunk.z))) return Team.RED
        if(LandCatcherPlugin.instance.chunks[Team.YELLOW]!!.contains(Pair(chunk.x, chunk.z))) return Team.YELLOW
        if(LandCatcherPlugin.instance.chunks[Team.GREEN]!!.contains(Pair(chunk.x, chunk.z))) return Team.GREEN
        return Team.NONE
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

    fun addChunkMaster(team: Team, chunk: Chunk):Boolean {
        if(checkChunkTaking(chunk))
            return false
        removeChunk(chunk)
        LandCatcherPlugin.instance.chunks[team]!!.add(Pair(chunk.x, chunk.z))
        return true
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

    fun addChunkOnAnother(team: Team, owner: Team, chunk: Chunk): Boolean {
        val lander = checkChuckOwner(chunk)
        if (lander == Team.NONE) return false
        if(lander == owner)
            return addChunk(team, chunk)
        return false
    }
}