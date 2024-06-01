package com.github.warihue.landcatcher.plugin

import com.github.warihue.landcatcher.core.LandCatcher
import org.bukkit.plugin.java.JavaPlugin
import io.github.monun.tap.event.EntityEventManager
import io.github.monun.tap.fake.FakeEntityServer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*


class LandCatcherPlugin: JavaPlugin() {
    lateinit var fakeEntityServer: FakeEntityServer
        private set

    lateinit var entityEventManager: EntityEventManager
        private set

    var players: MutableList<Player> = mutableListOf()

    override fun onEnable() {
        fakeEntityServer = FakeEntityServer.create(this)
        loadModules()
        LandCatcher.initialize(this, logger, fakeEntityServer)
    }
    private fun loadModules() {
        entityEventManager = EntityEventManager(this)
        server.apply {
            pluginManager.registerEvents(
                EventListener(
                    fakeEntityServer,
                    this@LandCatcherPlugin
                ), this@LandCatcherPlugin
            )
            scheduler.runTaskTimer(this@LandCatcherPlugin, SchedulerTask(fakeEntityServer), 0L, 1L)
        }
    }

}