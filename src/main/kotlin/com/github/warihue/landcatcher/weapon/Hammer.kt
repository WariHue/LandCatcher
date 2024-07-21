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
import org.bukkit.persistence.PersistentDataType
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
        if(!player.inventory.itemInMainHand.correctChecker(hammerAxe())) return;
        val level = player.inventory.itemInMainHand.itemMeta.persistentDataContainer.get(LandCatcherPlugin.itemKey, PersistentDataType.INTEGER)!!
        event.entity.world.spawnParticle(Particle.EXPLOSION_HUGE, event.entity.location, 3, 0.0, 0.0, 0.0, 2.0)
        event.entity.world.playSound(player.location, Sound.ENTITY_GHAST_SHOOT, 6f, 1f)
        val entitiesTemp:List<Entity> = event.entity.getNearbyEntities(3.0,3.0,3.0)
        (event.entity as LivingEntity).lCatchDamage(DamageType.MELEE, ((level /2)).toDouble(), player, 0.0,player.location, 1.0)
        (event.entity as LivingEntity).addPotionEffect(PotionEffect(PotionEffectType.JUMP, 60, 200, false, false, false))
        (event.entity as LivingEntity).addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 60, 127, false, false, false))
        (event.entity as LivingEntity).addPotionEffect(PotionEffect(PotionEffectType.SLOW, 60, 200, false, false, false))
        if(event.entity is Player)
            (event.entity as Player).setCooldown(Material.SHIELD, 400)
        val entities:List<Entity> = entitiesTemp.filter { it != player }
        for(en: Entity in entities){
            if(en is LivingEntity) {
                en.lCatchDamage(DamageType.MELEE, ((level /2)).toDouble(), player, 0.0, event.entity.location, 1.0)
                en.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 20, 200, false, false, false))
                en.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 20, 127, false, false, false))
                en.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20, 200, false, false, false))
                en.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, 20, 200, false, false, false))
                if(en is Player)
                    en.setCooldown(Material.SHIELD, 400)
            }
        }
        player.setCooldown(Material.NETHERITE_AXE, 100)
    }
}