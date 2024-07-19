package com.github.warihue.landcatcher.core.inventory

import com.github.warihue.landcatcher.core.*
import com.github.warihue.landcatcher.core.util.EnchantItem.enchantItem
import io.github.monun.invfx.InvFX.frame
import io.github.monun.invfx.openFrame
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import io.github.monun.invfx.openFrame as invfxOpenFrame

object EnchantGUI {
    private val gItem =
        ItemStack(Material.BARRIER).apply {
            itemMeta = itemMeta.apply {
                displayName(
                    text().color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
                        .decorate(TextDecoration.BOLD).content("아이템 빼기").build()
                )
            }
        }
    fun openEnchantTable(player: Player) {
        val enchantFrame = frame(2, text("\uEBBB\uECCC", TextColor.color(255, 255, 255))) {
            onClickBottom { clickEvent ->
                slot(4, 0) {
                    if(clickEvent.currentItem!!.correctChecker(damageGun()) || clickEvent.currentItem!!.correctChecker(healGun()) || clickEvent.currentItem!!.correctChecker(damageGun()) ||clickEvent.currentItem!!.correctChecker(hammerAxe()) || clickEvent.currentItem!!.correctChecker(daggerSword()) || clickEvent.currentItem!!.correctChecker(bombLauncher())) return@slot
                    else {
                        this.item = clickEvent.currentItem
                        onClick { clickEvents ->
                            player.inventory.getItem(player.inventory.slotFounder(ItemStack(Material.LAPIS_LAZULI)))!!.amount -= 1
                            clickEvents.currentItem = enchantItem(clickEvents.currentItem!!, player)
                            clickEvent.currentItem = clickEvents.currentItem
                        }
                    }
                }
            }

            slot(8, 0) {
                item = gItem
                onClick { openEnchantTable(player) }
            }
        }
        player.invfxOpenFrame(enchantFrame)
    }
}