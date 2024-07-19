package com.github.warihue.landcatcher

import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
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
    GREEN,
    YELLOW
}

enum class Job {
    NONE,
    MELEE,
    GUNNER,
    HEALER,
    BOMBER,
    HAMMER,
}