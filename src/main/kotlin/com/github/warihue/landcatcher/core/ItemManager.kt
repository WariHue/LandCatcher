package com.github.warihue.landcatcher.core

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

fun masterLandCatcher(amount: Int): ItemStack {
    val startLandCatcher = ItemStack(Material.PAPER, amount)
    val itemMeta: ItemMeta = startLandCatcher.itemMeta
    itemMeta.setCustomModelData(2)
    itemMeta.displayName(text("시작용 땅 문서").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
    itemMeta.lore(listOf<Component>(
        text("시작할 때 땅을 살수 있다").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
    ))
    startLandCatcher.itemMeta = itemMeta
    return startLandCatcher
}

fun damageGun(): ItemStack {
    val damageGun = ItemStack(Material.DIAMOND_HORSE_ARMOR)
    val itemMeta: ItemMeta = damageGun.itemMeta
    itemMeta.setCustomModelData(2)
    itemMeta.displayName(text("AWP").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
    itemMeta.lore(listOf<Component>(
        text("그냥 평범한 탄 발사기").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("RangedDamage(10)").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
    ))
    damageGun.itemMeta = itemMeta
    return damageGun
}

fun healGun(): ItemStack {
    val healGun = ItemStack(Material.DIAMOND_HORSE_ARMOR)
    val itemMeta: ItemMeta = healGun.itemMeta
    itemMeta.setCustomModelData(3)
    itemMeta.displayName(text("Tavor").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
    itemMeta.lore(listOf<Component>(
        text("생체 독 발사기, 아군에겐 약이다").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("Heal(6)").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false)
    ))
    healGun.itemMeta = itemMeta
    return healGun
}