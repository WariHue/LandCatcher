package com.github.warihue.landcatcher.weapon

import com.github.warihue.landcatcher.core.damage.DamageSupport.calculateAngle
import com.github.warihue.landcatcher.core.damage.DamageSupport.lCatchHeal
import com.github.warihue.landcatcher.core.util.TargetFilter
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import io.github.monun.tap.fake.*
import io.github.monun.tap.math.normalizeAndLength
import net.kyori.adventure.text.Component.text
import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector

open class NeedleShot(
    private val shooter: Player,
    private val heal: Double
): FakeProjectile(1200, 64.0) {
    private lateinit var needleArmor: FakeEntity<ArmorStand>

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
                helmet = ItemStack(Material.GOLD_NUGGET)
            }
        }
    }

    fun setToLaunch(): NeedleShot {
        return apply { needleArmor = summonBulletArmor(shooter.location) }
    }

    final override fun onMove(movement: Movement) {
        val to = movement.to
        needleArmor.apply { moveTo(to.clone()) }

        needleArmor.updateMetadata { onCustomMoving() }

        shooter.world.spawnParticle(Particle.DUST_COLOR_TRANSITION, needleArmor.location.add(0.0,1.68,0.0), 1, 0.01, 0.01, 0.01, Particle.DustTransition(Color.GREEN, Color.GREEN, 1f))
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
                length,
                FluidCollisionMode.NEVER,
                true,
                1.0,
                null
            )?.let { result ->
                val hitEntity = result.hitEntity
                if(hitEntity == shooter) return
                if(hitEntity != null && hitEntity is LivingEntity) {
                    if(hitEntity is Player && !TargetFilter(hitEntity, LandCatcherPlugin.instance.players[shooter]!!.team)) {
                        val hitLocation = result.hitPosition.toLocation(world)
                        hitEntity.lCatchHeal(heal);
                        world.spawnParticle(Particle.HEART, hitLocation, 3, 0.2, 0.2, 0.2, 4.0)
                        world.playSound(hitLocation, Sound.ENTITY_SPLASH_POTION_BREAK, 2.0F, 2.0F)
                        remove()
                    }
                    needleArmor.updateMetadata {
                        val hitLocation = result.hitPosition.toLocation(world)
                        if(hitEntity.activeItem.type == Material.SHIELD) {
                            if (calculateAngle(
                                    hitLocation.x - hitEntity.x,
                                    hitLocation.z - hitEntity.z,
                                    (hitEntity.yaw + 90).toDouble()
                                ) < 45
                            ) {
                                hitEntity.activeItem.itemMeta.apply {
                                    damage(heal - this@NeedleShot.heal)
                                }
                                return@updateMetadata
                            }
                        }
                        hitEntity.addPotionEffect(PotionEffect(PotionEffectType.POISON, 90, 3, false, false, false))
                        world.spawnParticle(
                            Particle.SLIME,
                            hitLocation,
                            5,
                            1.0,
                            1.0,
                            1.0,
                            4.0
                        )
                        world.playSound(hitLocation, Sound.ENTITY_SPLASH_POTION_BREAK, 2.0F, 2.0F)
                        remove()
                    }
                }
                val hitLocation = result.hitPosition.toLocation(world)
                world.spawnParticle(
                    Particle.SLIME,
                    hitLocation,
                    5,
                    0.2,
                    0.2,
                    0.2,
                    4.0
                )
                world.playSound(hitLocation, Sound.ENTITY_SPLASH_POTION_BREAK, 2.0F, 2.0F)
                remove()
            }
        }
    }
    final override fun onRemove() {
        needleArmor.apply { remove() }
    }

    open fun ArmorStand.onCustomMoving() {}
}