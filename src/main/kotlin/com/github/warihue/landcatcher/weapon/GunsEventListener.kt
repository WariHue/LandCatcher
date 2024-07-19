package com.github.warihue.landcatcher.weapon

import com.github.warihue.landcatcher.Job
import com.github.warihue.landcatcher.core.*
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class GunsEventListener: Listener {
    @EventHandler
    fun playerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action
        if(action.isRightClick) {
            if(player.inventory.itemInMainHand.correctChecker(damageGun())) {
                if (LandCatcherPlugin.instance.players[player]!!.job != Job.GUNNER) return
                event.isCancelled = true
                if(player.inventory.slotFounder(ItemStack(Material.IRON_NUGGET)) == -1){player.world.playSound(player.location, Sound.BLOCK_CHAIN_PLACE, 0.8F, 1.5F); return}
                if(player.hasCooldown(Material.DIAMOND_HORSE_ARMOR)) {player.world.playSound(player.location, Sound.BLOCK_CHAIN_PLACE, 0.8F, 1.5F); return}
                player.inventory.getItem(player.inventory.slotFounder(ItemStack(Material.IRON_NUGGET)))!!.amount -= 1
                val location = player.eyeLocation
                val loc = player.location
                val subProjectile = Bullet(
                    player,
                    (player.inventory.itemInMainHand.itemMeta.persistentDataContainer.get(LandCatcherPlugin.itemKey, PersistentDataType.INTEGER)!! * 2).toDouble()
                )
                val bullet = subProjectile.setToLaunch()

                Bullet.manager.launch(loc, bullet)
                bullet.velocity = location.direction.multiply(4)
                player.world.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2.0F, .5F)
                player.setCooldown(Material.DIAMOND_HORSE_ARMOR, 8)
            }
            if(player.inventory.itemInMainHand.correctChecker(healGun())){
                if (LandCatcherPlugin.instance.players[player]!!.job != Job.HEALER) return
                event.isCancelled = true
                if(player.inventory.slotFounder(ItemStack(Material.GOLD_NUGGET)) == -1){player.world.playSound(player.location, Sound.BLOCK_CHAIN_PLACE, 0.8F, 1.5F); return}
                if(player.hasCooldown(Material.DIAMOND_HORSE_ARMOR)){player.world.playSound(player.location, Sound.BLOCK_CHAIN_PLACE, 0.8F, 1.5F); return}
                player.inventory.getItem(player.inventory.slotFounder(ItemStack(Material.GOLD_NUGGET)))!!.amount -= 1
                val location = player.eyeLocation
                val loc = player.location
                val subProjectile = NeedleShot(
                    player,
                    player.inventory.itemInMainHand.itemMeta.persistentDataContainer.get(LandCatcherPlugin.itemKey, PersistentDataType.INTEGER)!!.toDouble()
                )
                val bullet = subProjectile.setToLaunch()

                NeedleShot.manager.launch(loc, bullet)
                bullet.velocity = location.direction.multiply(3)
                player.world.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2.0F, .5F)
                player.setCooldown(Material.DIAMOND_HORSE_ARMOR, 12)
            }
            if(player.inventory.itemInMainHand.correctChecker(bombLauncher())){
                if (LandCatcherPlugin.instance.players[player]!!.job != Job.BOMBER) return
                event.isCancelled = true
                if(player.inventory.slotFounder(ItemStack(Material.GUNPOWDER)) == -1){player.world.playSound(player.location, Sound.BLOCK_CHAIN_PLACE, 0.8F, 1.5F); return}
                if(player.hasCooldown(Material.DIAMOND_HORSE_ARMOR)){player.world.playSound(player.location, Sound.BLOCK_CHAIN_PLACE, 0.8F, 1.5F); return}
                player.inventory.getItem(player.inventory.slotFounder(ItemStack(Material.GUNPOWDER)))!!.amount -= 1
                val location = player.eyeLocation
                val loc = player.location
                val bombPro = Bomb(
                    player,
                    player.inventory.itemInMainHand.itemMeta.persistentDataContainer.get(LandCatcherPlugin.itemKey, PersistentDataType.INTEGER)!!.toDouble()
                )
                val bomb = bombPro.setToLaunch()
                Bomb.manager.launch(loc, bomb)
                bomb.velocity = location.direction.multiply(1)
                player.world.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2.0F, .1F)
                player.setCooldown(Material.DIAMOND_HORSE_ARMOR, 100)
            }
        }
    }
    @EventHandler
    fun rideEvent(event: PlayerInteractEntityEvent) {
        val player = event.player
        if (player.inventory.itemInMainHand.correctChecker(damageGun()) || player.inventory.itemInMainHand.correctChecker(healGun()) || player.inventory.itemInMainHand.correctChecker(daggerSword()))
            event.isCancelled = true
            if(player.inventory.itemInMainHand.correctChecker(damageGun())) {
                event.isCancelled = true
                if(player.inventory.slotFounder(ItemStack(Material.IRON_NUGGET)) == -1){player.world.playSound(player.location, Sound.BLOCK_CHAIN_PLACE, 0.8F, 1.5F); return}
                if(player.hasCooldown(Material.DIAMOND_HORSE_ARMOR)) {player.world.playSound(player.location, Sound.BLOCK_CHAIN_PLACE, 0.8F, 1.5F); return}
                player.inventory.getItem(player.inventory.slotFounder(ItemStack(Material.IRON_NUGGET)))!!.amount -= 1
                val location = player.eyeLocation
                val loc = player.location
                val subProjectile = Bullet(
                    player,
                    (player.inventory.itemInMainHand.itemMeta.persistentDataContainer.get(LandCatcherPlugin.itemKey, PersistentDataType.INTEGER)!! * 2).toDouble()
                )
                val bullet = subProjectile.setToLaunch()

                Bullet.manager.launch(loc, bullet)
                bullet.velocity = location.direction.multiply(4)
                player.world.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2.0F, .5F)
                player.setCooldown(Material.DIAMOND_HORSE_ARMOR, 8)
            }
            if(player.inventory.itemInMainHand.correctChecker(healGun())){
                event.isCancelled = true
                if(player.inventory.slotFounder(ItemStack(Material.GOLD_NUGGET)) == -1){player.world.playSound(player.location, Sound.BLOCK_CHAIN_PLACE, 0.8F, 1.5F); return}
                if(player.hasCooldown(Material.DIAMOND_HORSE_ARMOR)){player.world.playSound(player.location, Sound.BLOCK_CHAIN_PLACE, 0.8F, 1.5F); return}
                player.inventory.getItem(player.inventory.slotFounder(ItemStack(Material.GOLD_NUGGET)))!!.amount -= 1
                val location = player.eyeLocation
                val loc = player.location
                val subProjectile = NeedleShot(
                    player,
                    player.inventory.itemInMainHand.itemMeta.persistentDataContainer.get(LandCatcherPlugin.itemKey, PersistentDataType.INTEGER)!!.toDouble()
                )
                val bullet = subProjectile.setToLaunch()

                NeedleShot.manager.launch(loc, bullet)
                bullet.velocity = location.direction.multiply(3)
                player.world.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2.0F, .5F)
                player.setCooldown(Material.DIAMOND_HORSE_ARMOR, 12)
            }

    }
}