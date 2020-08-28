package com.willfp.ecoenchants.v1_16_R1;

import net.minecraft.server.v1_16_R1.Enchantment;
import org.bukkit.craftbukkit.v1_16_R1.enchantments.CraftEnchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class CraftEnchantWrapper extends CraftEnchantment {
    private org.bukkit.enchantments.Enchantment bukkit;

    public CraftEnchantWrapper(Enchantment target, org.bukkit.enchantments.Enchantment bukkit) {
        super(target);
        this.bukkit = bukkit;
    }

    @Override
    public String getName() {
        return this.bukkit.getName();
    }

    @Override
    public int getMaxLevel() {
        return this.bukkit.getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return this.bukkit.getStartLevel();
    }

    @Override
    @Deprecated
    public EnchantmentTarget getItemTarget() {
        return this.bukkit.getItemTarget();
    }

    @Override
    @Deprecated
    public boolean isTreasure() {
        return this.bukkit.isTreasure();
    }

    @Override
    @Deprecated
    public boolean isCursed() {
        return this.bukkit.isCursed();
    }

    @Override
    public boolean conflictsWith(org.bukkit.enchantments.Enchantment enchantment) {
        return this.bukkit.conflictsWith(enchantment);
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return this.bukkit.canEnchantItem(itemStack);
    }
}
