package com.github.warihue.landcatcher.weapons.Gun

import com.github.warihue.landcatcher.core.WeaponConcept
import com.github.warihue.landcatcher.core.WeaponProjectile
import com.github.warihue.landcatcher.core.damage.DamageSupport.lCatchDamage
import com.github.warihue.landcatcher.core.damage.DamageType
import com.github.warihue.landcatcher.core.util.TargetFilter
import io.github.monun.tap.fake.FakeEntity
import io.github.monun.tap.fake.FakeProjectile
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import io.github.monun.tap.fake.Movement
import io.github.monun.tap.fake.Trail
import io.github.monun.tap.math.normalizeAndLength
import org.bukkit.FluidCollisionMode
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action

class WeaponConceptGun: WeaponConcept(){
    var bulletSpeed = 8.0

    var bulletGravity = 0.001

    var bulletSize = 1

    var item: ItemStack = ItemStack(Material.NETHERITE_HOE);
    init {
        damage = 5.0
        range =  64.0
        cooldownTime = 200L
        knockback = 0.05
        wand = item
    }
}
class BulletProjectile(player: Player):WeaponProjectile(1200, 128.0){
    val concept = WeaponConceptGun()
    lateinit var bullet: FakeEntity<ArmorStand>
    val playerI = player
    override fun onPreUpdate() {
        velocity = velocity.apply { y -= concept.bulletGravity }
    }
    override fun onMove(movement: Movement) {
        bullet.moveTo(movement.to.clone().apply { y -= 1.62 })
    }
    override fun onTrail(trail: Trail) {
        trail.velocity?.let { v ->
            val length = v.normalizeAndLength()

            if (length > 0.0) {
                val start = trail.from
                val world = start.world

                world.rayTrace(
                    start, v, length, FluidCollisionMode.NEVER, true, concept.bulletSize / 2.0,
                    TargetFilter(player = playerI)
                )?.let { result ->
                    remove()

                    val hitLocation = result.hitPosition.toLocation(world)
                    world.spawnParticle(
                        Particle.BLOCK_DUST,
                        hitLocation,
                        32,
                        0.0,
                        0.0,
                        0.0,
                        4.0,
                        Material.COBBLESTONE.createBlockData(),
                        true
                    )

                    result.hitEntity?.let { entity ->
                        if (entity is LivingEntity) {
                            val knockback = if (entity.isOnGround) concept.knockback else 0.0
                            entity.damage(10.0)
                        }
                    }

                    world.playSound(hitLocation, Sound.BLOCK_STONE_BREAK, 2.0F, 2.0F)
                }
            }
        }
    }

}
