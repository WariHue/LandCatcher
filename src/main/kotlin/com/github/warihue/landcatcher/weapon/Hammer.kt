package com.github.warihue.landcatcher.weapon

import com.github.warihue.landcatcher.Job
import com.github.warihue.landcatcher.core.correctChecker
import com.github.warihue.landcatcher.core.damage.DamageSupport.lCatchDamage
import com.github.warihue.landcatcher.core.damage.DamageType
import com.github.warihue.landcatcher.core.hammerAxe
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Hammer: Listener {
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if(event.damager !is Player) return;
        if(event.entity !is LivingEntity) return;
        if(LandCatcherPlugin.instance.players[event.damager]!!.job != Job.HAMMER) return
        val player: Player = event.damager as Player
        if(player.hasCooldown(Material.NETHERITE_AXE)) return
        val level = LandCatcherPlugin.instance.players[player]!!.level
        if(!player.inventory.itemInMainHand.correctChecker(hammerAxe())) return;
        event.entity.world.spawnParticle(Particle.EXPLOSION_HUGE, event.entity.location, 3, 0.0, 0.0, 0.0, 2.0)
        event.entity.world.playSound(player.location, Sound.ENTITY_GHAST_SHOOT, 6f, 1f)
        val entitiesTemp:List<Entity> = event.entity.getNearbyEntities(3.0,3.0,3.0)
        (event.entity as LivingEntity).lCatchDamage(DamageType.MELEE, ((level * 2) + 5).toDouble(), player, player.location, 8.0)
        val entities:List<Entity> = entitiesTemp.filter { it != player }
        for(en: Entity in entities){
            if(en is LivingEntity) {
                en.lCatchDamage(DamageType.MELEE, ((level * 2) + 5).toDouble(), player, event.entity.location, 8.0)
                en.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 3, 254, false, false, false))
                en.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 3, 10, false, false, false))
                if(en is Player)
                    en.setCooldown(Material.SHIELD, 200)
            }
        }
        player.setCooldown(Material.NETHERITE_AXE, 100)
    }
}