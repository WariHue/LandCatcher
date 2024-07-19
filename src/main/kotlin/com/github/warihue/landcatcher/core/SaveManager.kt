package com.github.warihue.landcatcher.core

import com.github.warihue.landcatcher.Job
import com.github.warihue.landcatcher.LCatchPlayer
import com.github.warihue.landcatcher.Team
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import com.google.gson.Gson
import org.bukkit.entity.Player
import java.io.File
import java.util.UUID

object SaveManager {
    private val playerDataFolder = File("${LandCatcherPlugin.instance.dataFolder}/players")

    fun Player.existsPlayerData(): Boolean {
        return File(playerDataFolder, "${uniqueId}.json").exists()
    }

    fun readPlayerData(uuid:UUID): LCatchPlayer {
        if(!playerDataFolder.exists()) playerDataFolder.mkdirs()
        if(!File(playerDataFolder, "${uuid}.json").exists()) {
            File(playerDataFolder, "${uuid}.json").createNewFile()
            File(playerDataFolder, "${uuid}.json").writeText(Gson().toJson(LCatchPlayer(Team.NONE, Job.NONE, 1)))
        }
        val data = File(playerDataFolder, "${uuid}.json").readText()
        return Gson().fromJson(data, LCatchPlayer::class.java)
    }

    fun writePlayerData(uuid:UUID, player:LCatchPlayer): Boolean {
        if(!playerDataFolder.exists()) playerDataFolder.mkdirs()
        if(!File(playerDataFolder, "${uuid}.json").exists()) {
            File(playerDataFolder, "${uuid}.json").createNewFile()
            File(playerDataFolder, "${uuid}.json").writeText(Gson().toJson(LCatchPlayer(Team.NONE, Job.NONE, 1)))
        }
        try {
            File(playerDataFolder, "${uuid}.json").writeText(Gson().toJson(player))
        } catch (e: Exception) {
            return false
        }
        return true
    }
}