package com.github.warihue.landcatcher

import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.UUID

data class LCatchPlayer(
    val team: Team,
    val job: Job,
    var level: Int
)

enum class Team {
    NONE,
    BLUE,
    RED,
}

enum class Job {
    NONE,
    MELEE,
    GUNNER,
    HEALER,
    BOMBER,
    HAMMER,
}

fun spawnLocation(team: Team, player: Player): Location{
    return when(team){
        Team.BLUE -> {
            Location(LandCatcherPlugin.overWorld, 507.5, 67.0, 507.5)
        }
        Team.RED -> {
            Location(LandCatcherPlugin.overWorld, -507.5, 62.0, -507.5)
        }
        else -> {
            Location(LandCatcherPlugin.overWorld, 0.5, 0.0, 0.5)
        }
    }
}