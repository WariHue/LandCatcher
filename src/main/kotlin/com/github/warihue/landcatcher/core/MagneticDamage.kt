package com.github.warihue.landcatcher.core

import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import io.papermc.paper.util.Tick
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class MagneticDamage(
) {
    private val players : MutableList<Player> = mutableListOf()

    fun addFiredPlayer(player: Player) { if(!players.contains(player)) players.add(player) }

    fun removeFiredPlayer(player: Player) { players.remove(player) }

    fun update() {
        updateManager()
    }

    private fun updateManager() {
        LandCatcherPlugin.instance.logger.info(players.toString())
        causeDamageToPlayer()
    }

    private fun causeDamageToPlayer(){
        for (player in players){
            player.fireTicks = 20
        }
    }
}