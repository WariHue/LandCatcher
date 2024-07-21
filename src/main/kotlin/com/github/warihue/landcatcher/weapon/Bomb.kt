package com.github.warihue.landcatcher.weapon;

import com.github.warihue.landcatcher.core.damage.DamageSupport.calculateAngle
import com.github.warihue.landcatcher.core.damage.DamageSupport.lCatchDamage
import com.github.warihue.landcatcher.core.damage.DamageType
import com.github.warihue.landcatcher.core.util.TargetFilter
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import io.github.monun.tap.fake.*
import io.github.monun.tap.math.normalizeAndLength
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

open class Bomb(
    private val shooter: Player,
    private val damage: Double
):FakeProjectile(1200, 128.0) {
    private lateinit var bombArmor: FakeEntity<ArmorStand>

    companion object{
        val manager = FakeProjectileManager()
    }

    private fun summonBulletArmor(loc: Location): FakeEntity<ArmorStand> = LandCatcherPlugin.fakeServer.spawnEntity(loc.clone(), ArmorStand::class.java).apply {
        updateMetadata {
//            updateMetaThrowing(direction)
            isVisible = false
            isMarker = true
            updateEquipment {
                helmet = ItemStack(Material.TNT)
            }
        }
    }

    fun setToLaunch(): Bomb {
        return apply { bombArmor = summonBulletArmor(shooter.location) }
    }

    override fun onPreUpdate() {
        velocity = velocity.apply { y -= 0.07 }
    }

    final override fun onMove(movement: Movement) {
        bombArmor.moveTo(movement.to.clone().apply { y -= 1.62 })

        shooter.world.spawnParticle(Particle.DUST_COLOR_TRANSITION, bombArmor.location.add(0.0,1.68,0.0), 1, 0.01, 0.01, 0.01, Particle.DustTransition(Color.BLACK, Color.BLACK, 1f))
    }

    final override fun onTrail(trail: Trail) {
        trail.velocity?.let { v ->
            val from = trail.from
            val world = from.world

            val length = v.normalizeAndLength()

            if(length > 0.0) {
                world.rayTrace(
                    trail.from,
                    v,
                    length,
                    FluidCollisionMode.NEVER,
                    true,
                    1.0,
                    null
                )?.let { result ->
                    if (result.hitEntity == shooter) return
                    val hitLocation = result.hitPosition.toLocation(world)
                    val ex = calculateExplosionRadius(damage / 2)
                    for (data in TargetFilter(
                        hitLocation.getNearbyEntities(ex, ex, ex).filterIsInstance<LivingEntity>(),
                        LandCatcherPlugin.instance.players[shooter]!!.team
                    )) {
                        data.lCatchDamage(DamageType.BLAST, damage / 2  + 5, shooter)
                    }
                    hitLocation.world.createExplosion(hitLocation, (damage / 2).toFloat(), false, true)
                    remove()

                }
            }
        }
    }
    final override fun onRemove() {
        bombArmor.apply { remove() }
    }

    fun calculateExplosionRadius(explosionPower: Double, stepLength: Double = 1.0): Double {
        return (1.3 * explosionPower / (stepLength * 0.75)) / stepLength
    }
}
