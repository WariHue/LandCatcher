package com.github.warihue.landcatcher.weapon

import com.github.warihue.landcatcher.core.damage.DamageSupport.lCatchDamage
import com.github.warihue.landcatcher.core.damage.DamageType
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import io.github.monun.tap.fake.*
import io.github.monun.tap.math.normalizeAndLength
import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector

open class Bullet(
    private val shooter: Player
): FakeProjectile(1200, 64.0) {
    private lateinit var bulletArmor: FakeEntity<ArmorStand>

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

    private fun ArmorStand.updateMetaThrowing(direction: Vector): ArmorStand = apply {
        headPose = EulerAngle(-direction.x, direction.y - direction.y * 45/180, direction.z)
    }

    final override fun onMove(movement: Movement) {
        val to = movement.to
        bulletArmor.apply { moveTo(to.clone().apply { y -= 1.62 }) }

        bulletArmor.updateMetadata { onCustomMoving() }
    }

    final override fun onTrail(trail: Trail) {
        trail.velocity?.let { v ->
            val from = trail.from
            val world = from.world

            val length = v.normalizeAndLength()

            if(length == 0.0) return

            world.rayTrace(
                from,
                v,
                range,
                FluidCollisionMode.NEVER,
                true,
                1.0,
                null
            )?.let { result ->
                val hitEntity = result.hitEntity
                if(hitEntity != null && hitEntity !is LivingEntity) {
                    bulletArmor.updateMetadata {
                        val hitLocation = result.hitPosition.toLocation(world)
                        (hitEntity as LivingEntity).lCatchDamage(DamageType.RANGED, 10.0, shooter, shooter.location, 0.2)
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
                    }
                }
            }
        }
    }
    final override fun onRemove() {
        val armorStand = bulletArmor.apply { remove() }
    }

    open fun ArmorStand.onCustomMoving() {}
}