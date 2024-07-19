package com.github.warihue.landcatcher.core.inventory

import com.github.warihue.landcatcher.Team
import com.github.warihue.landcatcher.core.*
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import io.github.monun.heartbeat.coroutines.HeartbeatScope
import io.github.monun.heartbeat.coroutines.Suspension
import io.github.monun.invfx.InvFX.frame
import io.github.monun.invfx.openFrame
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import io.github.monun.invfx.openFrame as invfxOpenFrame

object MenuGUI {
    fun openMenu(player: Player) {
        val lPlayer = LandCatcherPlugin.instance.players[player]!!
        val menuFrame = frame(1, text("\uEBBB\uEDDD", TextColor.color(255, 255, 255))) {
            onClickBottom { clickEvent ->
                if(clickEvent.currentItem == null) return@onClickBottom
                if(!isLCItem(clickEvent.currentItem!!)) return@onClickBottom
                player.inventory.removeItem(clickEvent.currentItem!!)
                player.sendMessage("Aa")
            }
            slot(1, 0){
                val item = changeLCatchToItem(lPlayer.job, lPlayer.level)
                val meta: ItemMeta = item.itemMeta
                meta.displayName(text("아이템 다시 얻기").decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD))
                meta.lore(
                    listOf(
                        text("여기서 인벤토리의 무기를 누르면 삭제")
                    )
                )
                this.item = item
                onClick {
                    player.inventory.addItem(item)
                }
            }
            slot(3,0){
                val item = ItemStack(Material.ENDER_PEARL)
                val meta: ItemMeta = item.itemMeta
                meta.displayName(text("귀환").decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD))
                meta.lore(
                    listOf(
                        text("30초간 움직이지 않으면 스폰으로 귀환합니다")
                    )
                )
                this.item = item
                onClick {
                    val location = player.location
                    player.closeInventory()
                    player.sendActionBar(text("30초간 움직이지 않으면 스폰으로 귀환합니다").color(NamedTextColor.BLUE))
                    HeartbeatScope().launch {
                        val suspension = Suspension()
                        suspension.delay(30000L)
                        if(location == player.location)
                            player.teleport(player.bedSpawnLocation!!)
                        else
                            player.sendActionBar(text("귀환 실패").color(NamedTextColor.RED))
                    }
                }
            }
            slot(5,0){
                val item = occupyLandCatcher()
                val meta: ItemMeta = item.itemMeta
                meta.displayName(text("빈 땅 매매증서 구매").decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD))
                meta.lore(
                    listOf(
                        text("철 주괴 x 3")
                    )
                )
                this.item = item
                onClick {
                    if(player.inventory.slotFounder(ItemStack(Material.IRON_INGOT)) != -1) {
                        if (player.inventory.getItem(player.inventory.slotFounder(ItemStack(Material.IRON_INGOT)))!!.amount >= 3) {
                            player.inventory.removeItem(ItemStack(Material.IRON_INGOT, 3))
                            player.inventory.addItem(occupyLandCatcher())
                        }
                    }
                }
            }
            slot(7,0){
                val item = stealLandCatcher(Team.NONE)
                val meta: ItemMeta = item.itemMeta
                meta.displayName(text("백지 땅 인수증서 구매").decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD))
                meta.lore(
                    listOf(
                        text("다이아몬드 x 2")
                    )
                )
                this.item = item
                onClick {
                    if(player.inventory.slotFounder(ItemStack(Material.DIAMOND)) != -1) {
                        if (player.inventory.getItem(player.inventory.slotFounder(ItemStack(Material.DIAMOND)))!!.amount >= 2) {
                            player.inventory.removeItem(ItemStack(Material.IRON_INGOT, 2))
                            player.inventory.addItem(stealLandCatcher(Team.NONE))
                        }
                    }
                }
            }
        }
        player.invfxOpenFrame(menuFrame)
    }
}