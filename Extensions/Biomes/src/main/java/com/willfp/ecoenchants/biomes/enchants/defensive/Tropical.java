package com.willfp.ecoenchants.biomes.enchants.defensive;

import com.willfp.ecoenchants.biomes.BiomesEnchantment;
import org.bukkit.block.Biome;

import java.util.Arrays;

public class Tropical extends BiomesEnchantment {
    public Tropical() {
        super("tropical", EnchantmentType.NORMAL);
    }

    @Override
    public boolean isValid(Biome biome) {
        return Arrays.stream(new String[]{"jungle"}).anyMatch(biome.name().toLowerCase()::contains);
    }
}
