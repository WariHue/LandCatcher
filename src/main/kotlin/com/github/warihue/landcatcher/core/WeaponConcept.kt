package com.github.warihue.landcatcher.core

import org.bukkit.inventory.ItemStack

open class WeaponConcept {
    var cooldownTime = 0L
        protected set
    var cost = 0.0
        protected set
    var castingTime = 0L
        protected set
    var range = 0.0
        protected set
    var damage: Double? = null
        protected set
    var knockback = 0.0
    var healing: Double? = null
        protected set
    private var _wand: ItemStack? = null
    internal val internalWand
        get() = _wand

    /**
     * 능력과 상호작용하는 [ItemStack]
     */
    var wand
        get() = _wand?.clone()
        protected set(value) {
            _wand = value?.clone()
        }
}