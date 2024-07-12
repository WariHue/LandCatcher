package com.github.warihue.landcatcher

import com.github.warihue.landcatcher.core.damageGun
import com.github.warihue.landcatcher.core.healGun
import com.github.warihue.landcatcher.core.masterLandCatcher
import com.github.warihue.landcatcher.core.util.ChunkManager
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import com.github.warihue.landcatcher.weapon.Bullet
import com.github.warihue.landcatcher.weapon.NeedleShot
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class GunsEventListener: Listener {
    @EventHandler
    fun playerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if(player.inventory.itemInMainHand.itemMeta == masterLandCatcher(1).itemMeta)
                event.player.sendMessage(LandCatcherPlugin.instance.chunks.toString() +  ChunkManager.addChunkMaster(LandCatcherPlugin.instance.players[event.player]!!, event.player.world.getChunkAt(event.player.location)).toString())
            if(player.inventory.itemInMainHand.itemMeta == damageGun().itemMeta) {
                val location = player.eyeLocation
                val loc = player.location
                val subProjectile = Bullet(
                    player
                )
                val bullet = subProjectile.setToLaunch()

                Bullet.manager.launch(loc, bullet)
                bullet.velocity = location.direction.multiply(3)
                player.world.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2.0F, .5F)
            }
            if(player.inventory.itemInMainHand.itemMeta == healGun().itemMeta){
                val location = player.eyeLocation
                val loc = player.location
                val subProjectile = NeedleShot(
                    player
                )
                val bullet = subProjectile.setToLaunch()

                NeedleShot.manager.launch(loc, bullet)
                bullet.velocity = location.direction.multiply(2)
                player.world.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2.0F, .5F)
            }
        }
    }
}