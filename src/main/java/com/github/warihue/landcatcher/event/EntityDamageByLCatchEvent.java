package com.github.warihue.landcatcher.event;

import com.github.warihue.landcatcher.core.damage.DamageType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityDamageByLCatchEvent extends EntityDamageByEntityEvent {
    private final DamageType damageType;

    private Location knockbackSource;

    private double knockbackForce;

    public EntityDamageByLCatchEvent(
            @NotNull Entity damager,
            @NotNull Entity damagee,
            double damage,
            @NotNull DamageType damageType,
            @Nullable Location knockbackSource,
            double knockbackForce
            ){
        super(damager, damagee, DamageCause.ENTITY_ATTACK, damage);
        this.damageType = damageType;
        this.knockbackSource = knockbackSource;
        this.knockbackForce = knockbackForce;
    }
    @NotNull
    public DamageType getDamageType() {
        return damageType;
    }

    @NotNull
    public Location getKnockbackSource() {
        return knockbackSource;
    }

    public void setKnockbackSource(@Nullable Location knockbackSource) {
        this.knockbackSource = knockbackSource;
    }

    public double getKnockbackForce() {
        return knockbackForce;
    }

    public void setKnockbackForce(double knockbackForce) {
        this.knockbackForce = knockbackForce;
    }
}
