package com.willfp.ecoenchants.biomes.enchants.defensive;


import com.willfp.ecoenchants.biomes.BiomesEnchantment;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Elevation extends BiomesEnchantment {
    public Elevation() {
        super("elevation", EnchantmentType.NORMAL);
    }

    @Override
    public boolean isValid(@NotNull final Biome biome) {
        return Arrays.stream(new String[]{"mountain", "hill"}).anyMatch(biome.name().toLowerCase()::contains);
    }
}
