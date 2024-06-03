package com.github.warihue.landcatcher.plugin

import com.github.warihue.landcatcher.test
import org.bukkit.plugin.java.JavaPlugin
import io.github.monun.tap.event.EntityEventManager
import io.github.monun.tap.fake.FakeEntityServer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*


class LandCatcherPlugin: JavaPlugin() {
    var players: MutableList<Player> = mutableListOf()
    companion object {
        lateinit var instance: LandCatcherPlugin

        lateinit var fakeServer: FakeEntityServer
    }

    override fun onEnable() {
        instance = this

        fakeServer = FakeEntityServer.create(this)

        server.scheduler.runTaskTimer(this, fakeServer::update, 0L, 1L)

        server.pluginManager.registerEvents(EventListener(), this)

        server.pluginManager.registerEvents(test(), this)
    }

    override fun onDisable() {
        server.scheduler.cancelTasks(this)
    }
}