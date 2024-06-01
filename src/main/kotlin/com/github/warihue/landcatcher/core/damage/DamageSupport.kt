package com.github.warihue.landcatcher.core.damage

import com.github.warihue.landcatcher.event.EntityDamageByLCatchEvent
import net.kyori.adventure.text.Component.text
import org.bukkit.EntityEffect
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.Mob
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min
object DamageSupport {
    fun calculateMinecraftDamage(damage: Double, armor: Double, armorTough: Double, protection: Double): Double {
        return damage * (1.0 - min(
            20.0,
            max(armor / 5.0, armor - damage / (2.0 + armorTough / 4.0))
        ) / 25.0) * (1.0 - protection * 0.04)
    }

    fun ItemStack.getProtection(enchantment: Enchantment): Int {
        var protection = getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)

        if (enchantment != Enchantment.PROTECTION_ENVIRONMENTAL) {
            val enchantmentProtection = getEnchantmentLevel(enchantment) shl 1

            if (protection < enchantmentProtection) {
                protection = enchantmentProtection
            }
        }

        return protection
    }

    fun LivingEntity.getProtection(enchantment: Enchantment): Int {
        val armorContents = equipment?.armorContents ?: return 0
        var protection = 0

        armorContents.asSequence().filterNotNull().forEach lit@{ item ->
            protection += item.getProtection(enchantment)
            if (protection >= 40) return@lit
        }

        return min(40, protection)
    }

    fun LivingEntity.lCatchDamage(
        damageType: DamageType,
        damage: Double,
        damager: Player,
        knockbackSource: Location? = damager.location,
        knockbackForce: Double = 0.0
    ): Double {
        server.broadcast(text("a"))
        return lCatchDamageActual(damageType, damage, damager, knockbackSource, knockbackForce)
    }

    private fun LivingEntity.lCatchDamageActual(
        type: DamageType,
        damage: Double,
        damager: Player,
        knockbackSource: Location? = damager.location,
        knockbackForce: Double = 0.0
    ): Double {
        server.broadcast(text(damage))
        val armor = getAttribute(Attribute.GENERIC_ARMOR)?.value ?: 0.0
        val armorTough = getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)?.value ?: 0.0
        val protection = getProtection(type.protection)
        val actualDamage = calculateMinecraftDamage(damage, armor, armorTough, protection.toDouble())
        server.broadcast(text(actualDamage))
        killer = damager
        if (knockbackSource != null && knockbackForce > 0.0) {
            val targetLocation = location
            var force = knockbackForce * 0.5
            val deltaX = knockbackSource.x - targetLocation.x
            val deltaZ = knockbackSource.z - targetLocation.z

            // 대상 넉백 저항으로 인한 넉백 감소
            getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.run { force *= 1.0 - value }

            if (force > 0.0) {
                val oldVelocity = velocity
                val knockBackVelocity = Vector(deltaX, 0.0, deltaZ).normalize().multiply(force)
                val newVelocity = Vector().apply {
                    // 수평 속도를 절반 줄이고 넉백 속도 적용
                    x = oldVelocity.x / 2.0 - knockBackVelocity.x
                    z = oldVelocity.z / 2.0 - knockBackVelocity.z
                    // 대상이 공중에 있을경우 수직 속도를 절반 줄이고 넉백 힘 만큼 적용
                    y = if (isOnGround) min(0.4, oldVelocity.y / 2.0 + force) else oldVelocity.y
                }

                velocity = newVelocity
            }
        }
        val mode = damager.gameMode
        if (mode == GameMode.SURVIVAL || mode == GameMode.ADVENTURE) {
            if (this is Mob) {
                target = damager
            }
        }
        noDamageTicks = 0
        if (this is EnderDragon) {
            health = max(0.0, health - actualDamage)
            playEffect(EntityEffect.HURT)
            world.playSound(location, Sound.ENTITY_ENDER_DRAGON_HURT, 1.0F, 1.0F)
        } else damage(actualDamage)

        return actualDamage
    }
}