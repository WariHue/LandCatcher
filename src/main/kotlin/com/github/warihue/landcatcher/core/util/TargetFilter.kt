package com.github.warihue.landcatcher.core.util

import com.github.warihue.landcatcher.Team
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.function.Predicate

fun TargetFilter(player: Player, team: Team): Boolean {
    if(LandCatcherPlugin.instance.teams[team]!!.find { lCatchPlayer -> lCatchPlayer.player == player } != null){
        return false
    }
    return true
}