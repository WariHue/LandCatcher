package com.github.warihue.landcatcher.weapon

import com.github.warihue.landcatcher.Job
import com.github.warihue.landcatcher.core.correctChecker
import com.github.warihue.landcatcher.core.daggerSword
import com.github.warihue.landcatcher.core.util.TargetFilter
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.monun.heartbeat.coroutines.Suspension
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Dagger: Listener {
    private val hidePlayers: MutableList<Player> = mutableListOf()
    @EventHandler
    fun playerUseHideSkill(e: PlayerInteractEvent){
        val player = e.player
        if(LandCatcherPlugin.instance.players[player]!!.job != Job.MELEE) return
        if(e.action.isRightClick && player.inventory.itemInMainHand.correctChecker(daggerSword())){
            e.isCancelled = true
            if(player.hasCooldown(Material.NETHERITE_SWORD)) { player.sendMessage(text("쿨다운 중 입니다!").color(NamedTextColor.RED)); return }
            for (data in LandCatcherPlugin.instance.players.keys){
                player.setCooldown(Material.NETHERITE_SWORD, 400)
                data.hidePlayer(LandCatcherPlugin.instance, player)
                hidePlayers.add(player)
                player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 80, 3, false, false, false))
                player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 80, 2, false, false, false))
                HeartbeatScope().launch {
                    val suspension = Suspension()
                    suspension.delay(6000)
                    data.showPlayer(LandCatcherPlugin.instance, player)
                    hidePlayers.remove(player)
                    player.sendActionBar(text(""))
                }
            }
        }
    }
    @EventHandler
    fun rideEvent(event: PlayerInteractEntityEvent) {
        val player = event.player
        if(LandCatcherPlugin.instance.players[player]!!.job != Job.MELEE) return
        if(player.inventory.itemInMainHand.correctChecker(daggerSword())){
            event.isCancelled = true
            if(player.hasCooldown(Material.NETHERITE_SWORD)) { player.sendMessage(text("쿨다운 중 입니다!").color(NamedTextColor.RED)); return }
            for (data in LandCatcherPlugin.instance.players.keys){
                player.setCooldown(Material.NETHERITE_SWORD, 200)
                data.hidePlayer(LandCatcherPlugin.instance, player)
                hidePlayers.add(player)
                player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 120, 3, false, false, false))
                player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 120, 2, false, false, false))
                HeartbeatScope().launch {
                    val suspension = Suspension()
                    suspension.delay(6000)
                    data.showPlayer(LandCatcherPlugin.instance, player)
                    hidePlayers.remove(player)
                    player.sendActionBar(text(""))
                }
            }
        }
    }
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent){
        if(event.damager is Player && event.entity is LivingEntity) {
            if (TargetFilter(event.damager as Player, LandCatcherPlugin.instance.players[event.damager]!!.team)) {
                event.isCancelled = true; event.damager.sendMessage("아군은 공격할수 업습니다!")
            }
            else{
                val entity = event.entity as LivingEntity
                val player = event.damager as Player
                if(!player.inventory.itemInMainHand.correctChecker(daggerSword()) || player.inventory.itemInMainHand.isEmpty) return
                if(hidePlayers.any { p -> p == player }){
                    event.damage += (player.inventory.itemInMainHand.itemMeta.persistentDataContainer.get(LandCatcherPlugin.itemKey, PersistentDataType.INTEGER)!! * 2).toLong()
                    if(event.damage >= entity.health)
                        player.setCooldown(Material.NETHERITE_SWORD, 0)
                    hidePlayers.remove(player)
                    for (data in LandCatcherPlugin.instance.players.keys){
                        data.showPlayer(LandCatcherPlugin.instance, player)
                    }
                    player.sendActionBar(text(""))
                }
            }
        }
    }
    @EventHandler
    fun playerMove(event: PlayerMoveEvent){
        if(hidePlayers.contains(event.player))
            event.player.sendActionBar(text("당신은 은신중 입니다").color(NamedTextColor.AQUA))
        else
            event.player.sendActionBar(text(""))
    }
}