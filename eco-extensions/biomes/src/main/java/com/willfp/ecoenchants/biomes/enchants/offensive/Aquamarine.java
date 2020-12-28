package com.willfp.ecoenchants.biomes.enchants.offensive;

import com.willfp.ecoenchants.biomes.BiomesEnchantment;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Aquamarine extends BiomesEnchantment {
    /**
     * Instantiate enchantment.
     */
    public Aquamarine() {
        super("aquamarine", EnchantmentType.NORMAL);
    }

    @Override
    public boolean isValid(@NotNull final Biome biome) {
        return Arrays.stream(new String[]{"ocean"}).anyMatch(biome.name().toLowerCase()::contains);
    }
}
