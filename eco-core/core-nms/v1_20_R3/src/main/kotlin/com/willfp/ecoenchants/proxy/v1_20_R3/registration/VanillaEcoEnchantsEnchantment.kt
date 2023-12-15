package com.willfp.ecoenchants.proxy.v1_20_R3.registration

import com.willfp.eco.core.Prerequisite
import com.willfp.eco.util.toComponent
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import io.papermc.paper.adventure.PaperAdventure
import net.minecraft.network.chat.Component
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobType
import net.minecraft.world.inventory.AnvilMenu
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
        /*

        This is the mother of all jank solutions.

        Because I want the EcoEnchants anvil code to handle all custom enchantment logic,
        I need to prevent the NMS anvil code from processing the EcoEnchants enchantments.

        However, there's no API method that I can use to do this - **however**,
        this method is called once in the NMS anvil code, and if it returns false then
        the anvil will not allow this enchantment to be applied to the item.

        So, I can check if the calling method is the anvil merge method, and if it is,
        I can return false.

         */

        val caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).callerClass

        if (caller.name == AnvilMenu::class.java.name) {
            return false
        }

        // End disgusting bodge

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

    override fun getFullname(level: Int): Component {
        val enchant = this.enchant
        return if (Prerequisite.HAS_PAPER.isMet && enchant != null) {
            PaperAdventure.asVanilla(enchant.getFormattedName(level).toComponent())
        } else {
            super.getFullname(level)
        }
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
