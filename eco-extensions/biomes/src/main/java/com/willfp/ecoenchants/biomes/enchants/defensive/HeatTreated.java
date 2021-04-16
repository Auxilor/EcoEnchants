package com.willfp.ecoenchants.biomes.enchants.defensive;


import com.willfp.ecoenchants.biomes.BiomesEnchantment;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class HeatTreated extends BiomesEnchantment {
    public HeatTreated() {
        super("heat_treated", EnchantmentType.NORMAL);
    }

    @Override
    public boolean isValid(@NotNull final Biome biome) {
        return Arrays.stream(new String[]{"desert", "badlands", "savanna"}).anyMatch(biome.name().toLowerCase()::contains);
    }
}
