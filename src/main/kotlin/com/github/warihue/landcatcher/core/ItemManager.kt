package com.github.warihue.landcatcher.core

import com.github.warihue.landcatcher.Job
import com.github.warihue.landcatcher.Team
import com.github.warihue.landcatcher.core.damage.DamageSupport.ensureNonNegative
import com.github.warihue.landcatcher.plugin.LandCatcherPlugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*


fun masterLandCatcher(): ItemStack {
    val startLandCatcher = ItemStack(Material.PAPER)
    val itemMeta: ItemMeta = startLandCatcher.itemMeta
    itemMeta.setCustomModelData(2)
    itemMeta.displayName(text("시작용 땅 문서").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
    itemMeta.lore(listOf<Component>(
        text("시작할 때 땅을 살수 있다").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
    ))
    startLandCatcher.itemMeta = itemMeta
    return startLandCatcher
}

fun occupyLandCatcher(): ItemStack {
    val startLandCatcher = ItemStack(Material.PAPER)
    val itemMeta: ItemMeta = startLandCatcher.itemMeta
    itemMeta.setCustomModelData(4)
    itemMeta.displayName(text("빈 땅 매매 증서").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
    itemMeta.lore(listOf<Component>(
        text("점령한 땅 근처의 땅을 점령한다").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
    ))
    startLandCatcher.itemMeta = itemMeta
    return startLandCatcher
}

fun stealLandCatcher(team: Team): ItemStack {
    val startLandCatcher = ItemStack(Material.PAPER)
    val itemMeta: ItemMeta = startLandCatcher.itemMeta
    when(team){
        Team.BLUE -> {
            itemMeta.setCustomModelData(5)
            itemMeta.displayName(text("파랑팀 땅 인수 증서").decorate(TextDecoration.BOLD).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
            itemMeta.lore(listOf<Component>(
                text("점령한 땅 근처의 파랑팀의 땅을 점령한다").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
            ))
            startLandCatcher.itemMeta = itemMeta
            return startLandCatcher
        }
        Team.RED -> {
            itemMeta.setCustomModelData(6)
            itemMeta.displayName(text("빨강팀 땅 인수 증서").decorate(TextDecoration.BOLD).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
            itemMeta.lore(listOf<Component>(
                text("점령한 땅 근처의 레드팀의 땅을 점령한다").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
            ))
            startLandCatcher.itemMeta = itemMeta
            return startLandCatcher
        }
        else -> {
            itemMeta.setCustomModelData(3)
            itemMeta.displayName(text("백지 땅 인수 증서").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
            itemMeta.lore(listOf<Component>(
                text("점령한 땅 근처의 남의 땅을 점령한다(사실은 마(불)법)").color(NamedTextColor.WHITE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
            ))
            startLandCatcher.itemMeta = itemMeta
            return startLandCatcher
        }
    }

}

fun damageGun(level: Int = 1): ItemStack {
    val damageGun = ItemStack(Material.DIAMOND_HORSE_ARMOR)
    val itemMeta: ItemMeta = damageGun.itemMeta
    itemMeta.persistentDataContainer.set(LandCatcherPlugin.itemKey, PersistentDataType.INTEGER, level)
    itemMeta.setCustomModelData(2)
    itemMeta.displayName(text("AWP").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
    itemMeta.lore(listOf<Component>(
        text("그냥 평범한 탄 발사기").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("RangedDamage(${level * 2})").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("발사 1.0s 마다").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
    ))
    damageGun.itemMeta = itemMeta
    return damageGun
}

fun healGun(level: Int = 1): ItemStack {
    val healGun = ItemStack(Material.DIAMOND_HORSE_ARMOR)
    val itemMeta: ItemMeta = healGun.itemMeta
    itemMeta.persistentDataContainer.set(LandCatcherPlugin.itemKey, PersistentDataType.INTEGER, level)
    itemMeta.setCustomModelData(3)
    itemMeta.displayName(text("Tavor").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
    itemMeta.lore(listOf<Component>(
        text("생체 독 발사기, 아군에겐 약이다").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("Heal(${level})").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("적 타격 시 생체 독 3s").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("발사 0.6s 마다").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),

    ))
    healGun.itemMeta = itemMeta
    return healGun
}

fun bombLauncher(level: Int = 1): ItemStack {
    val healGun = ItemStack(Material.DIAMOND_HORSE_ARMOR)
    val itemMeta: ItemMeta = healGun.itemMeta
    itemMeta.persistentDataContainer.set(LandCatcherPlugin.itemKey, PersistentDataType.INTEGER, level)
    itemMeta.setCustomModelData(4)
    itemMeta.displayName(text("캐논").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
    itemMeta.lore(listOf<Component>(
        text("폭탄을 쏜다").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("추가 대미지(${level / 2 + 5})").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("폭발 크기(${level / 2})").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("발사 4s 마다").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        ))
    healGun.itemMeta = itemMeta
    return healGun
}

fun daggerSword(level: Int = 1): ItemStack {
    val daggerSword = ItemStack(Material.NETHERITE_SWORD)
    val itemMeta: ItemMeta = daggerSword.itemMeta
    itemMeta.persistentDataContainer.set(LandCatcherPlugin.itemKey, PersistentDataType.INTEGER, level)
    itemMeta.setCustomModelData(2)
    itemMeta.displayName(text("펜니르의 단검").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
    itemMeta.lore(listOf<Component>(
        text("SKILL: 은신 6s (cooldown: 20s, 적 치치 시 초기화)").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("은신 시 대미지(${level * 2 + 5}), 방어력 무시(${(level - 2).ensureNonNegative()})").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false)
    ))
    itemMeta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE)
    itemMeta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED)
    val gs1 = AttributeModifier(
        UUID.randomUUID(),
        "generic.attack_damage",
        level.toDouble(),
        AttributeModifier.Operation.ADD_NUMBER,
        EquipmentSlot.HAND
    )
    val gs2 = AttributeModifier(
        UUID.randomUUID(),
        "generic.attack_speed",
        -2.4,
        AttributeModifier.Operation.ADD_NUMBER,
        EquipmentSlot.HAND
    )
    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, gs1)
    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, gs2)
    itemMeta.isUnbreakable = true
    daggerSword.itemMeta = itemMeta
    return daggerSword
}

fun hammerAxe(level: Int = 1): ItemStack {
    val hammerAxe = ItemStack(Material.NETHERITE_AXE)
    val itemMeta: ItemMeta = hammerAxe.itemMeta
    itemMeta.persistentDataContainer.set(LandCatcherPlugin.itemKey, PersistentDataType.INTEGER, level)
    itemMeta.setCustomModelData(2)
    itemMeta.displayName(text("터-보 망치").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
    itemMeta.lore(listOf<Component>(
        text("SKILL: 강타!(5s마다 강하게 내리쳐 주위의 적에게 ${((level /2))}만큼의 대미지를 입힌다)").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("MELEE(${((level /2))})").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("기절 1s").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
        text("방패 파괴 20s").color(NamedTextColor.RED).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false),
    ))
    itemMeta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE)
    itemMeta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED)
    val gs1 = AttributeModifier(
        UUID.randomUUID(),
        "generic.attack_damage",
        (level + 2).toDouble(),
        AttributeModifier.Operation.ADD_NUMBER,
        EquipmentSlot.HAND
    )
    val gs2 = AttributeModifier(
        UUID.randomUUID(),
        "generic.attack_speed",
        -3.0,
        AttributeModifier.Operation.ADD_NUMBER,
        EquipmentSlot.HAND
    )
    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, gs1)
    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, gs2)
    itemMeta.isUnbreakable = true
    hammerAxe.itemMeta = itemMeta
    return hammerAxe
}

fun obsidianPotion(): ItemStack {
    val potion = ItemStack(Material.POTION) // 일반 포션 아이템 생성
    val meta = potion.itemMeta as PotionMeta


    // 포션 메타 데이터 설정
    meta.displayName(text("흑요석 포션").decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false).color(
        TextColor.color(255, 127, 0))) // 포션의 이름 설정
    meta.addCustomEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 1), true)// 커스텀 효과 추가

    meta.lore(
        listOf(
            text("적 팀에 들어 갔을 때 자기장에 불 타는 것을 막을 수 있다").color(NamedTextColor.YELLOW),
            text("화염저항은 보너스").color(NamedTextColor.YELLOW),
        )
    )


    potion.setItemMeta(meta)
    return potion
}

fun ItemStack.correctChecker(itemStack: ItemStack): Boolean{
    if(itemMeta == null) return false
    if(itemMeta.hasCustomModelData()) {
        return type == itemStack.type && itemMeta.customModelData == itemStack.itemMeta.customModelData
    }
    return false
}

fun ItemStack.chunkItemCorrectChecker(): Boolean{
    if(itemMeta == null) return false
    if(itemMeta.hasCustomModelData()) {
        return type == Material.PAPER && itemMeta.customModelData >= 5 && itemMeta.customModelData <= 8
    }
    return false
}

fun changeLCatchToItem(job: Job, level: Int):ItemStack {
    return when (job){
        Job.MELEE -> daggerSword(level)
        Job.GUNNER -> damageGun(level)
        Job.HEALER -> healGun(level)
        Job.HAMMER -> hammerAxe(level)
        Job.BOMBER -> bombLauncher(level)
        else -> ItemStack(Material.BROWN_DYE)
    }
}

fun ItemStack.itemTeamChecker(): Team {
    if(itemMeta == null) return Team.NONE
    if(itemMeta.hasCustomModelData()) {
        return when(itemMeta.customModelData){
            5 -> Team.BLUE
            6 -> Team.RED
            else -> Team.NONE
        }
    }
    return Team.NONE
}

fun PlayerInventory.removeJobItem(job: Job, level: Int) {
    when(job){
        Job.MELEE -> removeItem(daggerSword(level))
        Job.GUNNER -> removeItem(damageGun(level))
        Job.HEALER -> removeItem(healGun(level))
        Job.HAMMER -> removeItem(hammerAxe(level))
        Job.BOMBER -> removeItem(bombLauncher(level))
        else -> ItemStack(Material.BROWN_DYE)
    }
}

fun isLCItem(itemStack: ItemStack): Boolean {
    return itemStack.correctChecker(damageGun()) ||
            itemStack.correctChecker(healGun()) ||
            itemStack.correctChecker(damageGun()) ||
            itemStack.correctChecker(hammerAxe()) ||
            itemStack.correctChecker(daggerSword()) ||
            itemStack.correctChecker(bombLauncher())

}

fun PlayerInventory.slotFounder(itemStack: ItemStack):Int {
    for (i:Int in 1..64){
        val a = itemStack
        a.amount = i
        if(first(a) != -1){
            return first(a)
        }
    }
    return -1
}