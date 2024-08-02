package com.github.warihue.landcatcher.core.inventory

import com.github.warihue.landcatcher.core.obsidianPotion
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import org.bukkit.Material
import org.bukkit.inventory.FurnaceRecipe

object RecipeManager {
    fun obsidianPotionRecipe() {
        val recipe: FurnaceRecipe = FurnaceRecipe(obsidianPotion(), Material.CRYING_OBSIDIAN)
        LandCatcherPlugin.instance.server.addRecipe(recipe)
    }
}