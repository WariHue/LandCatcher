package com.github.warihue.landcatcher.core

import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import io.github.monun.tap.fake.FakeEntity
import io.github.monun.tap.fake.FakeEntityServer
import io.github.monun.tap.fake.FakeProjectile
import io.github.monun.tap.fake.FakeProjectileManager
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.logging.Logger

object LandCatcher {
    lateinit var plugin: LandCatcherPlugin
        private set

    lateinit var logger: Logger
        private set

    lateinit var fakeEntityServer: FakeEntityServer
        private set

    private lateinit var projectiles: FakeProjectileManager

    internal fun initialize(
        plugin: LandCatcherPlugin,
        logger: Logger,
        fakeEntityServer: FakeEntityServer
    ) {
        this.plugin = plugin
        this.logger = logger
        this.fakeEntityServer = fakeEntityServer
        projectiles = FakeProjectileManager()
    }
    fun launchProjectile(location: Location, projectile: WeaponProjectile) {
        logger.warning("a")
        projectile.landCatcher = this
        projectiles.launch(location, projectile)
    }
    fun spawnFakeEntity(location: Location, entityClass: Class<ArmorStand>): FakeEntity<ArmorStand> {

        val fakeEntity = plugin.fakeEntityServer.spawnEntity(location, entityClass)

        return fakeEntity
    }
}