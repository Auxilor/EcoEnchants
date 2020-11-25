package com.willfp.ecoenchants.biomes.enchants.defensive;

import com.willfp.ecoenchants.biomes.BiomesEnchantment;
import org.bukkit.block.Biome;

import java.util.Arrays;

public class HeatTreated extends BiomesEnchantment {
    public HeatTreated() {
        super("heat_treated", EnchantmentType.NORMAL);
    }

    @Override
    public boolean isValid(Biome biome) {
        return Arrays.stream(new String[]{"desert", "badlands", "savanna"}).anyMatch(biome.name().toLowerCase()::contains);
    }
}
