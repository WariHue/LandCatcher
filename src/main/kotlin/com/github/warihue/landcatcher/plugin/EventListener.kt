package com.github.warihue.landcatcher.plugin

import com.github.warihue.landcatcher.core.LandCatcher.launchProjectile
import com.github.warihue.landcatcher.core.LandCatcher.spawnFakeEntity
import com.github.warihue.landcatcher.weapons.Gun.BulletProjectile
import io.github.monun.tap.fake.FakeEntity
import io.github.monun.tap.fake.FakeEntityServer
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack

class EventListener(
    private val fakeEntityServer: FakeEntityServer,
    private val plugin: LandCatcherPlugin
) : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        plugin.players.add(player)
        fakeEntityServer.addPlayer(player)
    }
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        plugin.players.remove(player)
        fakeEntityServer.removePlayer(player)
    }
    @EventHandler
    fun playerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            event.item?.let { item ->
                if(item.type == Material.NETHERITE_HOE){
                    var location = player.eyeLocation
                    player.setCooldown(item.type, 20)
                    val projectile = BulletProjectile(player).apply {
                        bullet =
                            plugin.fakeEntityServer.spawnEntity(location, ArmorStand::class.java).apply {
                                updateMetadata {
                                    isVisible = false
                                    isMarker = true
                                }
                                updateEquipment {
                                    helmet = ItemStack(Material.IRON_BLOCK)
                                }
                            }
                    }
                    launchProjectile(location, projectile)
                    projectile.velocity = location.direction.multiply(4)

                }
            }
        }
    }
}