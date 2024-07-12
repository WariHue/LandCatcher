package com.github.warihue.landcatcher.core

import com.github.warihue.landcatcher.Team
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.map.MapCanvas
import org.bukkit.map.MapCursor
import org.bukkit.map.MapRenderer
import org.bukkit.map.MapView
import java.awt.Color
import kotlin.math.absoluteValue

class ChunkMapRenderer: MapRenderer() {

    private val playerCursors = mutableMapOf<Player, MapCursor>()
    override fun render(mapView: MapView, mapCanvas: MapCanvas, player: Player) {
//        mapView.centerX = 64
//        mapView.centerZ = 64
        for (x in 0 until 128) {
            for (y in 0 until 128) {
                mapCanvas.setPixelColor(x, y, Color.WHITE)
            }
        }
        for (data in LandCatcherPlugin.instance.chunks[Team.BLUE]!!){
            mapCanvas.setPixelColor(data.first + 64, data.second + 64, Color.BLUE)
        }
        for (data in LandCatcherPlugin.instance.chunks[Team.RED]!!){
            mapCanvas.setPixelColor(data.first + 64, data.second + 64, Color.RED)
        }
        for (data in LandCatcherPlugin.instance.chunks[Team.GREEN]!!){
            mapCanvas.setPixelColor(data.first + 64, data.second + 64, Color.GREEN)
        }
        for (data in LandCatcherPlugin.instance.chunks[Team.YELLOW]!!){
            mapCanvas.setPixelColor(data.first + 64, data.second + 64, Color.YELLOW)
        }
        val cursors = mapCanvas.cursors
        val onlinePlayers = Bukkit.getOnlinePlayers()

        // 기존 커서 중 현재 플레이어 목록에 없는 커서 제거
        playerCursors.keys.removeIf { existingPlayer ->
            if (!onlinePlayers.contains(existingPlayer)) {
                val cursor = playerCursors[existingPlayer]
                if (cursor != null) {
                    cursors.removeCursor(cursor)
                }
                true
            } else {
                false
            }
        }

        val playerLocation = player.location

        val cursorX = -((playerLocation.x) / 8).toInt()
        val cursorZ = -((playerLocation.z) / 8).toInt()

        val direction = ((playerLocation.yaw % 360) / 22.5).toInt().let {
            if (it < 0) it + 16 else it
        }.toByte()
        direction + 8
        if(direction >= 16) direction - 16

        val existingCursor = playerCursors[player]
        if (existingCursor != null) {
            existingCursor.x = cursorX.toByte()
            existingCursor.y = cursorZ.toByte()
            existingCursor.direction = direction
        } else {
            val cursor = MapCursor(
                cursorX.toByte(),
                cursorZ.toByte(),
                direction,
                if(LandCatcherPlugin.instance.players[player] == Team.BLUE) MapCursor.Type.BANNER_BLUE
                else if(LandCatcherPlugin.instance.players[player] == Team.RED) MapCursor.Type.BANNER_RED
                else if(LandCatcherPlugin.instance.players[player] == Team.GREEN) MapCursor.Type.BANNER_GREEN
                else if(LandCatcherPlugin.instance.players[player] == Team.YELLOW) MapCursor.Type.BANNER_YELLOW
                else MapCursor.Type.SMALL_WHITE_CIRCLE,
                true
            )
            playerCursors[player] = cursor
            cursors.addCursor(cursor)
        }
    }
}