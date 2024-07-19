package com.github.warihue.landcatcher.core.util

import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

object EnchantItem {
    fun enchantItem(itemStack: ItemStack, p: Player): ItemStack {
        var lineLevel: Int = itemStack.enchantments.size
        if(lineLevel == 0) lineLevel = Random.nextInt(1, 3)
        if(Random.nextFloat() > 0.98 && lineLevel < 5) lineLevel++
        for (i in 0 until Enchantment.values().size){
            itemStack.removeEnchantment(Enchantment.values()[i])
        }
        var i = 0
        val enchantments: HashMap<Enchantment, Int> = HashMap()
        RUN@while(i < lineLevel){
            val randEnchant: Enchantment = Enchantment.values()[Random.nextInt(Enchantment.values().size)]
            val randLevel: Int = Random.nextInt(randEnchant.maxLevel) + 1
            if(enchantments[randEnchant] != null) continue@RUN
            itemStack.addUnsafeEnchantment(randEnchant, randLevel)
            enchantments[randEnchant] = randLevel
            i++
        }
        p.location.world.playSound(p.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f)
        return itemStack
    }
}