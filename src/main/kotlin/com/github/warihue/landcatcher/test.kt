package com.github.warihue.landcatcher

import com.github.warihue.landcatcher.weapon.Bullet
import io.github.monun.tap.fake.FakeProjectile
import io.github.monun.tap.fake.FakeProjectileManager
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.Vector

class test: Listener {
    @EventHandler
    fun playerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK && player.inventory.itemInMainHand.type == Material.NETHERITE_HOE) {
            val location = player.eyeLocation
            val loc = player.location
            val subProjectile = Bullet(
                player
            )
            val bullet = subProjectile.setToLaunch()

            val a =  Bullet.manager.launch(loc, bullet)
            bullet.velocity = player.eyeLocation.direction.multiply(1000)
        }
    }
}