package com.github.warihue.landcatcher.weapon

import com.github.warihue.landcatcher.core.damage.DamageSupport.lCatchDamage
import com.github.warihue.landcatcher.core.damage.DamageType
import com.github.warihue.landcatcher.core.util.TargetFilter
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import io.github.monun.tap.fake.*
import io.github.monun.tap.math.normalizeAndLength
import net.kyori.adventure.text.Component.text
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector

open class Bullet(
    private val shooter: Player
): FakeProjectile(1200, 64.0) {
    private lateinit var bulletArmor: FakeEntity<ArmorStand>

    val wand: ItemStack = ItemStack(Material.NETHERITE_HOE).apply {
        var meta: ItemMeta = this.itemMeta
        meta.displayName(text("총"))
//        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE)
//        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier())
    }

    companion object{
        val manager = FakeProjectileManager()
    }

    private fun summonBulletArmor(loc: Location): FakeEntity<ArmorStand> = LandCatcherPlugin.fakeServer.spawnEntity(loc.clone(), ArmorStand::class.java).apply {
        updateMetadata {
//            updateMetaThrowing(direction)
            isVisible = false
            isMarker = true
            updateEquipment {
                helmet = ItemStack(Material.IRON_BLOCK)
            }
        }
    }

    fun setToLaunch(): Bullet {
        return apply { bulletArmor = summonBulletArmor(shooter.location) }
    }

    final override fun onMove(movement: Movement) {
        val to = movement.to
        bulletArmor.apply { moveTo(to.clone()) }

        bulletArmor.updateMetadata { onCustomMoving() }

//        shooter.world.spawnParticle(Particle.DUST_COLOR_TRANSITION, bulletArmor.location.add(0.0,1.68,0.0), 1, 0.01, 0.01, 0.01, Particle.DustTransition(Color.BLACK, Color.BLACK, 1f))
    }

    final override fun onTrail(trail: Trail) {
        trail.velocity?.let { v ->
            val from = trail.from.add(Vector(0.0, 1.68, 0.0))
            val world = from.world

            val length = v.normalizeAndLength()

            if(length == 0.0) return

            world.rayTrace(
                trail.from,
                v,
                range,
                FluidCollisionMode.NEVER,
                true,
                1.0,
                null
            )?.let { result ->
                val hitEntity = result.hitEntity
                if(hitEntity != null && hitEntity is LivingEntity) {
                    if(hitEntity is Player && TargetFilter(hitEntity, LandCatcherPlugin.instance.players[shooter]!!))
                    bulletArmor.updateMetadata {
                        val hitLocation = result.hitPosition.toLocation(world)
                        hitEntity.lCatchDamage(DamageType.RANGED, 10.0, shooter, shooter.location, 0.2)
                        world.spawnParticle(
                            Particle.BLOCK_DUST,
                            hitLocation,
                            32,
                            0.0,
                            0.0,
                            0.0,
                            4.0,
                            Material.IRON_BLOCK.createBlockData(),
                            true
                        )
                        world.playSound(hitLocation, Sound.BLOCK_ANVIL_LAND, 2.0F, 2.0F)
                        remove()
                    }
                }
            }
        }
    }
    final override fun onRemove() {
        bulletArmor.apply { remove() }
    }

    open fun ArmorStand.onCustomMoving() {}
}