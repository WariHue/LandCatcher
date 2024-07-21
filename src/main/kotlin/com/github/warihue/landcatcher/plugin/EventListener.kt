package com.github.warihue.landcatcher.plugin

import com.destroystokyo.paper.event.player.PlayerSetSpawnEvent
import com.github.warihue.landcatcher.Job
import com.github.warihue.landcatcher.LCatchPlayer
import com.github.warihue.landcatcher.Team
import com.github.warihue.landcatcher.core.*
import com.github.warihue.landcatcher.core.SaveManager.existsPlayerData
import com.github.warihue.landcatcher.core.SaveManager.readPlayerData
import com.github.warihue.landcatcher.core.SaveManager.writePlayerData
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import com.github.warihue.landcatcher.core.damage.DamageSupport.lCatchDamage
import com.github.warihue.landcatcher.core.damage.DamageType
import com.github.warihue.landcatcher.core.inventory.EnchantGUI.openEnchantTable
import com.github.warihue.landcatcher.core.inventory.UpgradeAnvil.openUpgradeTable
import com.github.warihue.landcatcher.core.util.ChunkManager
import com.github.warihue.landcatcher.core.util.ChunkManager.isChunkEnemy
import com.github.warihue.landcatcher.core.util.ChunkManager.isChunkNotBelongTeam
import com.github.warihue.landcatcher.spawnLocation
import io.papermc.paper.event.player.PlayerBedFailEnterEvent
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.Cancellable
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.*
import org.bukkit.inventory.ItemStack
import kotlin.math.cos

class EventListener : Listener {
    @EventHandler
    fun onPlayerLogin(event: PlayerLoginEvent) {
        if(!event.player.existsPlayerData()) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, text("당신은 등록되어 있지 않습니다\n개발자에게 문의 하세요").color(NamedTextColor.RED).decorate(TextDecoration.BOLD))
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        LandCatcherPlugin.fakeServer.addPlayer(event.player)
        val data = readPlayerData(event.player.uniqueId)
        LandCatcherPlugin.instance.players[event.player] = data
        LandCatcherPlugin.instance.teams[data.team]!!.add(event.player)
        if(event.player.inventory.isEmpty)
            event.player.teleport(spawnLocation(data.team, event.player))
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        if(event.player.existsPlayerData()) {
            LandCatcherPlugin.fakeServer.removePlayer(event.player)
            writePlayerData(event.player.uniqueId, LandCatcherPlugin.instance.players[event.player]!!)
        }
        LandCatcherPlugin.magneticDamage.removeFiredPlayer(event.player)
    }

    @EventHandler
    fun playerPlaceBlock(event: BlockPlaceEvent) {
        if(isChunkNotBelongTeam(LandCatcherPlugin.instance.players[event.player]!!.team, event.player.chunk)){
            event.isCancelled = true
            event.player.sendMessage(text("다른 팀의 땅에 블록을 설치/파괴할 수 없습니다").decorate(TextDecoration.BOLD).color(NamedTextColor.RED))
        }
    }

    @EventHandler
    fun playerBreakBlock(event: BlockBreakEvent) {
        if(isChunkNotBelongTeam(LandCatcherPlugin.instance.players[event.player]!!.team, event.player.chunk)){
            event.isCancelled = true
            event.player.sendMessage(text("다른 팀의 땅에 블록을 설치/파괴할 수 없습니다").decorate(TextDecoration.BOLD).color(NamedTextColor.RED))
        }
    }

    @EventHandler
    fun playaerInt(event: PlayerInteractEntityEvent){
        if(isChunkNotBelongTeam(LandCatcherPlugin.instance.players[event.player]!!.team, event.player.chunk)){
            if(event.rightClicked.type == EntityType.MINECART_CHEST ||
                event.rightClicked.type == EntityType.MINECART_HOPPER ||
                event.rightClicked.type == EntityType.MINECART_FURNACE ||
                event.rightClicked.type == EntityType.CHEST_BOAT){
                event.isCancelled = true
                event.player.sendMessage(text("다른 팀의 땅 내의 상자 또는 보관 블록에 접근할 수 없습니다").decorate(TextDecoration.BOLD).color(NamedTextColor.RED))
            }
        }
    }

    @EventHandler
    fun playerIn(event: PlayerInteractEvent){
        if(event.action == Action.RIGHT_CLICK_BLOCK && isChunkNotBelongTeam(LandCatcherPlugin.instance.players[event.player]!!.team, event.player.chunk)){
            val block = event.clickedBlock!!
            if(block.type == Material.CHEST ||
                block.type == Material.TRAPPED_CHEST ||
                block.type == Material.BARREL ||
                block.type == Material.ENDER_CHEST ||
                block.type == Material.DISPENSER ||
                block.type == Material.DROPPER ||
                block.type == Material.HOPPER ||
                block.type == Material.FURNACE ||
                block.type == Material.SMOKER ||
                block.type == Material.BLAST_FURNACE ||
                block.type == Material.BREWING_STAND ||
                block.type == Material.JUKEBOX ||
                block.type == Material.LECTERN){
                event.isCancelled = true
                event.player.sendMessage(text("다른 팀의 땅 내의 상자 또는 보관 블록에 접근할 수 없습니다").decorate(TextDecoration.BOLD).color(NamedTextColor.RED))
            }
        }
    }

    @EventHandler
    fun playerSetSpawnEvent(event: PlayerSetSpawnEvent) {
        event.isCancelled = true
        event.player.sendMessage(text("리스폰 지점을 설정할 수 없습니다").decorate(TextDecoration.BOLD).color(NamedTextColor.RED))
    }

    @EventHandler
    fun playerRespawnEvent(event: PlayerRespawnEvent) {
        val data = LandCatcherPlugin.instance.players[event.player]!!
        event.respawnLocation = spawnLocation(data.team, event.player)
    }

    @EventHandler
    fun playerItemClickEvent(event: InventoryClickEvent){
        if(event.whoClicked.gameMode != GameMode.CREATIVE) {
            if(event.inventory.type == InventoryType.CRAFTING) return
            if (event.currentItem != null && event.currentItem!!.correctChecker(damageGun()) ||
                event.currentItem!!.correctChecker(healGun()) ||
                event.currentItem!!.correctChecker(daggerSword()) ||
                event.currentItem!!.correctChecker(hammerAxe()) ||
                event.currentItem!!.correctChecker(bombLauncher())
            ) {
                if(event.whoClicked.openInventory.title() == text("\uEBBB\uEDDD", TextColor.color(255, 255, 255))) return
                event.whoClicked.sendMessage(event.inventory.type.toString())
                event.isCancelled = true
                return
            }
        }
        event.isCancelled = false
    }

    @EventHandler
    fun playerWasteEvent(event: PlayerDropItemEvent) {
        if (event.player.gameMode != GameMode.CREATIVE){
            if (event.itemDrop.itemStack.correctChecker(damageGun()) ||
                event.itemDrop.itemStack.correctChecker(healGun()) ||
                event.itemDrop.itemStack.correctChecker(daggerSword()) ||
                event.itemDrop.itemStack.correctChecker(hammerAxe()) ||
                event.itemDrop.itemStack.correctChecker(bombLauncher())
            ) {
                event.isCancelled = true
                event.player.sendMessage(text("전용 아이템은 버릴 수 없습니다").color(NamedTextColor.RED))
            }
        }
    }

    @EventHandler
    fun playerDeath(event: PlayerDeathEvent){
        val data = LandCatcherPlugin.instance.players[event.player]!!
        event.deathMessage(text("사람이 죽었다.").color(NamedTextColor.RED).decorate(TextDecoration.BOLD))
        event.player.world.dropItemNaturally(event.player.location, stealLandCatcher(data.team))
        val drops = event.drops
        event.drops.clear()
        event.drops.addAll(drops.filter { itemStack -> !isLCItem(itemStack) })
    }

    @EventHandler
    fun opneAnvilEvent(e: InventoryOpenEvent){
        if(e.inventory.type == InventoryType.ANVIL){
            val player = e.player as Player
            openUpgradeTable(player)
            e.isCancelled = true
        }else if(e.inventory.type == InventoryType.ENCHANTING){
            val player = e.player as Player
            openEnchantTable(player)
            e.isCancelled = true
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

    @EventHandler
    fun onMove(event: PlayerMoveEvent){
        if(isChunkEnemy(LandCatcherPlugin.instance.players[event.player]!!.team, event.player.chunk))
            LandCatcherPlugin.magneticDamage.addFiredPlayer(event.player)
        else
            LandCatcherPlugin.magneticDamage.removeFiredPlayer(event.player)
    }
}