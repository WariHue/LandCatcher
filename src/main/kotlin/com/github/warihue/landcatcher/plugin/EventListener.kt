package com.github.warihue.landcatcher.plugin

import com.github.warihue.landcatcher.Team
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import com.github.warihue.landcatcher.core.damage.DamageSupport.lCatchDamage
import com.github.warihue.landcatcher.core.damage.DamageType
import com.github.warihue.landcatcher.core.util.ChunkManager
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Item
import org.bukkit.event.Cancellable
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack

class EventListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        LandCatcherPlugin.fakeServer.addPlayer(event.player)
        LandCatcherPlugin.instance.players[event.player] = Team.BLUE
        LandCatcherPlugin.instance.teams[Team.BLUE]!!.add(event.player)

    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        LandCatcherPlugin.fakeServer.removePlayer(event.player)
    }

//    @EventHandler
//    fun playerInteract(event: PlayerInteractEvent) {
//        if(event.action != Action.RIGHT_CLICK_AIR || event.action != Action.LEFT_CLICK_AIR || event.action != Action.LEFT_CLICK_AIR) event.isCancelled = true
//    }

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

    @EventHandler
    fun onPlayerSwapHandItemsEvent(event: PlayerSwapHandItemsEvent){
        if(event.mainHandItem != null && event.mainHandItem!!.type == Material.FILLED_MAP && event.mainHandItem!!.itemMeta.displayName() == text("점령 표시기").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false)){
            event.isCancelled = true
            if(event.isCancelled) {
                event.player.inventory.setItemInOffHand(null)
            }
        }
    }
}