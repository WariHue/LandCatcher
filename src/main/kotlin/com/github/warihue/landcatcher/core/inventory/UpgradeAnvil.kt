package com.github.warihue.landcatcher.core.inventory

import com.github.warihue.landcatcher.Job
import com.github.warihue.landcatcher.LCatchPlayer
import com.github.warihue.landcatcher.core.*
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import io.github.monun.invfx.InvFX.frame
import io.github.monun.invfx.openFrame
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import io.github.monun.invfx.openFrame as invfxOpenFrame

object UpgradeAnvil {
    private fun upgradeItemString() : List<List<Component>> = listOf(
        listOf(text("철 주괴 x 6").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)),
        listOf(text("금 주괴 x 4").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), text("청금석 x 16").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)),
        listOf(text("다이아몬드 x 3").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), text("흑요석 x 2").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)),
        listOf(text("책 x 4").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), text("흑요석 x 3").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), text("다이아몬드 x 5").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)),
        listOf(text("가스트 눈물 x 2").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false), text("네더라이트 파편 x 1").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)),
        listOf(text("화염구 x 1").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), text("네더라이트 파편 x 1").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)),
        listOf(text("황금 사과 x 1").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false), text("드래곤 머리 x 1").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)),
        listOf(text("네더라이트 주괴 x 1").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)),
        listOf(text("대장장이 형판(강화) x 1").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false), text("네더라이트 주괴 x 1").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)),
    )

    private fun upgradeItemStack() = listOf(
        listOf(ItemStack(Material.IRON_INGOT, 6)),
        listOf(ItemStack(Material.GOLD_INGOT, 4), ItemStack(Material.LAPIS_LAZULI, 16)),
        listOf(ItemStack(Material.DIAMOND, 3), ItemStack(Material.OBSIDIAN, 2)),
        listOf(ItemStack(Material.BOOK, 4), ItemStack(Material.OBSIDIAN, 3), ItemStack(Material.DIAMOND, 5)),
        listOf(ItemStack(Material.GHAST_TEAR, 2), ItemStack(Material.NETHERITE_SCRAP, 1)),
        listOf(ItemStack(Material.FIRE_CHARGE, 1), ItemStack(Material.NETHERITE_SCRAP, 1)),
        listOf(ItemStack(Material.GOLDEN_APPLE, 1), ItemStack(Material.DRAGON_HEAD, 1)),
        listOf(ItemStack(Material.NETHERITE_INGOT, 1)),
        listOf(ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1), ItemStack(Material.NETHERITE_INGOT, 1))
    )
    fun openUpgradeTable(player: Player){
        val lPlayer = LandCatcherPlugin.instance.players[player]!!
        val anvilFrame = frame(1, text("\uEBBB\uEAAA", TextColor.color(255, 255, 255))){
            if(lPlayer.level >= 10){
                player.sendMessage(text("이미 강화를 완료했습니다."))
                player.closeInventory()
                return@frame
            }
            slot(3,0) {
                this.item = changeLCatchToItem(lPlayer.job, lPlayer.level)
            }
            slot(4,0) {
                val itemS = ItemStack(Material.PINK_TERRACOTTA)
                val meta = itemS.itemMeta!!
                meta.displayName(text("강화 (${lPlayer.level} - ${lPlayer.level + 1})").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false))
                meta.lore(
                    upgradeItemString()[lPlayer.level - 1]
                )
                itemS.itemMeta = meta
                this.item = itemS
                onClick { e ->
                    if(e.currentItem == null) return@onClick;
                    var check = true
                    for (data in upgradeItemStack()[lPlayer.level - 1]){
                        val amount = data.amount
                        val itemStack = data
                        if(player.inventory.slotFounder(itemStack) == -1)
                            check = false
                        if (player.inventory.getItem(player.inventory.slotFounder(data))!!.amount < amount)
                            check = false
                    }
                    if(check){
                        for (data in upgradeItemStack()[lPlayer.level - 1]){
                            val amount = data.amount
                            val itemStack = data
                            itemStack.amount = amount
                            player.inventory.removeItem(itemStack)
                        }
                        LandCatcherPlugin.instance.players[player]!!.level += 1
                        player.inventory.removeJobItem(lPlayer.job, lPlayer.level - 1)
                        player.inventory.addItem(changeLCatchToItem(lPlayer.job, lPlayer.level))
                        player.world.playSound(player ,Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f)
                        openUpgradeTable(player)
                    }
                    else
                        player.world.playSound(player ,Sound.BLOCK_ANVIL_BREAK, 1.0f, 1.0f)
                }
            }
            slot(5,0){
                this.item = changeLCatchToItem(lPlayer.job, lPlayer.level + 1)
            }
        }
        player.invfxOpenFrame(anvilFrame)
    }
}