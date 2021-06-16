package com.willfp.ecoenchants.proxy.v1_16_R1.enchants;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.support.vanilla.VanillaEnchantmentMetadata;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import net.minecraft.server.v1_16_R1.Enchantment;
import org.bukkit.craftbukkit.v1_16_R1.enchantments.CraftEnchantment;
import org.jetbrains.annotations.NotNull;

public class EcoCraftEnchantment extends CraftEnchantment {
    private final VanillaEnchantmentMetadata metadata;

    public EcoCraftEnchantment(@NotNull final Enchantment target,
                               @NotNull final VanillaEnchantmentMetadata metadata) {
        super(target);
        this.metadata = metadata;
    }

    @Override
    public int getMaxLevel() {
        return metadata.maxLevel() == null ? this.getHandle().getMaxLevel() : metadata.maxLevel();
    }

    @Override
    public boolean conflictsWith(@NotNull final org.bukkit.enchantments.Enchantment other) {
        return EcoEnchants.getFromEnchantment(other) == null ? super.conflictsWith(other) : other.conflictsWith(other);
    }

    public void register() {
        EnchantmentUtils.register(this);
    }
}
