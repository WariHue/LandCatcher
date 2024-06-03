package com.github.warihue.landcatcher.plugin

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import com.github.warihue.landcatcher.core.damage.DamageSupport.lCatchDamage
import com.github.warihue.landcatcher.core.damage.DamageType
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class EventListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        LandCatcherPlugin.fakeServer.addPlayer(event.player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        LandCatcherPlugin.fakeServer.removePlayer(event.player)
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if(event.damager !is Player) return;
        if(event.entity !is LivingEntity) return;
        val player:Player = event.damager as Player
        if(player.inventory.itemInMainHand.type != Material.NETHERITE_AXE) return;
        event.entity.world.spawnParticle(Particle.EXPLOSION_HUGE, player.location, 3, 0.0, 0.0, 0.0, 2.0)
        event.entity.world.playSound(player.location, Sound.ENTITY_GHAST_SHOOT, 6f, 1f)
        val entitiesTemp:List<Entity> = event.entity.getNearbyEntities(3.0,3.0,3.0)
        (event.entity as LivingEntity).lCatchDamage(DamageType.MELEE, event.damage, player, player.location, 2.0)
        val entities:List<Entity> = entitiesTemp.filter { it != player }
        for(en:Entity in entities){
            if(en is LivingEntity)
                en.lCatchDamage(DamageType.MELEE, event.damage, player, event.entity.location, 2.0)
        }
    }
}