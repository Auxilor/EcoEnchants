package com.willfp.ecoenchants.proxy.v1_20_R3.registration

import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentCategory
import org.bukkit.craftbukkit.v1_20_R3.enchantments.CraftEnchantment
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack
import java.util.Objects

class VanillaEcoEnchantsEnchantment(
    private val id: String
) : Enchantment(
    Rarity.VERY_RARE,
    EnchantmentCategory.VANISHABLE,
    emptyArray()
) {
    private val enchant: EcoEnchant?
        get() = EcoEnchants[id]

    override fun canEnchant(stack: ItemStack): Boolean {
        val item = CraftItemStack.asCraftMirror(stack)
        return enchant?.canEnchantItem(item) ?: false
    }

    override fun doPostAttack(user: LivingEntity, target: Entity, level: Int) {
        // Do nothing
    }

    override fun doPostHurt(user: LivingEntity, attacker: Entity, level: Int) {
        // Do nothing
    }

    override fun getDamageBonus(level: Int, group: MobType): Float {
        return 0f
    }

    override fun getDamageProtection(level: Int, source: DamageSource): Int {
        return 0
    }

    override fun getMinLevel(): Int {
        return 1
    }

    override fun getMaxLevel(): Int {
        return enchant?.maximumLevel ?: 1
    }

    override fun isCurse(): Boolean {
        return false
    }

    override fun isDiscoverable(): Boolean {
        return false
    }

    override fun isTradeable(): Boolean {
        return false
    }

    override fun isTreasureOnly(): Boolean {
        return true
    }

    override fun checkCompatibility(other: Enchantment): Boolean {
        val bukkit = CraftEnchantment.minecraftToBukkit(other)

        if (enchant != null) {
            return !enchant!!.conflictsWith(bukkit)
        }

        return false
    }

    override fun getSlotItems(entity: LivingEntity): MutableMap<EquipmentSlot, ItemStack> {
        return mutableMapOf()
    }

    override fun toString(): String {
        return "VanillaEcoEnchantsEnchantment(id='$id')"
    }

    override fun equals(other: Any?): Boolean {
        return other is VanillaEcoEnchantsEnchantment && other.id == this.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}
