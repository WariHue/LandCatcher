package com.github.warihue.landcatcher.core

import org.bukkit.entity.Player
import java.lang.ref.WeakReference
import java.util.*

class Esper(
    player: Player
) {
    val player: Player
        get() = requireNotNull(playerRef.get()) { "Cannot get reference as it has already been Garbage Collected" }

    private val playerRef = WeakReference(player)

    private val attributeUniqueId: UUID

    val isOnline
        get() = playerRef.get() != null

    init {
        val uniqueId = player.uniqueId

        attributeUniqueId = UUID(uniqueId.leastSignificantBits.inv(), uniqueId.mostSignificantBits.inv())
    }
}