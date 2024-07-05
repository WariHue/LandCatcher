package com.github.warihue.landcatcher.plugin

import com.github.warihue.landcatcher.Team
import com.github.warihue.landcatcher.core.util.ChunkManager
import com.github.warihue.landcatcher.core.util.DataManager
import com.github.warihue.landcatcher.test
import com.github.warihue.landcatcher.weapon.Bullet
import org.bukkit.plugin.java.JavaPlugin
import io.github.monun.tap.event.EntityEventManager
import io.github.monun.tap.fake.FakeEntityServer
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.World
import org.bukkit.WorldType
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap


class LandCatcherPlugin: JavaPlugin() {
    var players: HashMap<Player ,Team> = HashMap()
    var teams: EnumMap<Team, MutableList<Player>> = EnumMap(Team::class.java)
    var chunks: EnumMap<Team, MutableList<Pair<Int, Int>>> = EnumMap(Team::class.java)
    companion object {
        lateinit var instance: LandCatcherPlugin

        lateinit var fakeServer: FakeEntityServer

        lateinit var overWorld: World
    }

    override fun onEnable() {
        instance = this

        fakeServer = FakeEntityServer.create(this)

        overWorld = server.getWorld("minecraft:overworld")!!

        server.scheduler.runTaskTimer(this, fakeServer::update, 0L, 1L)

        server.scheduler.runTaskTimer(this, Bullet.manager::update, 0L, 1L)

        server.pluginManager.registerEvents(EventListener(), this)

        server.pluginManager.registerEvents(test(), this)

        init()
//        logger.info(DataManager.convertChunkDataToJson(chunks))
    }

    override fun onDisable() {
        server.scheduler.cancelTasks(this)
    }
    private fun init(){
        teams[Team.NONE] = mutableListOf()
        teams[Team.BLUE] = mutableListOf()
        teams[Team.RED] = mutableListOf()
        teams[Team.GREEN] = mutableListOf()
        teams[Team.YELLOW] = mutableListOf()
        chunks[Team.NONE] = mutableListOf()
        chunks[Team.BLUE] = mutableListOf()
        chunks[Team.RED] = mutableListOf()
        chunks[Team.GREEN] = mutableListOf()
        chunks[Team.YELLOW] = mutableListOf()
    }
}