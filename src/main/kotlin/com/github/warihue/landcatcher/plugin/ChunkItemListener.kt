package com.github.warihue.landcatcher.plugin

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class ChunkItemListener: Listener {
    @EventHandler
    fun listenClickItem(event: PlayerInteractEvent) {
        val player = event.player
    }
}