package com.willfp.ecoenchants.biomes.enchants.defensive;

import com.willfp.ecoenchants.biomes.BiomesEnchantment;
import org.bukkit.block.Biome;

import java.util.Arrays;

public class Oceanic extends BiomesEnchantment {
    public Oceanic() {
        super("oceanic", EnchantmentType.NORMAL);
    }

    @Override
    public boolean isValid(Biome biome) {
        return Arrays.stream(new String[]{"ocean"}).anyMatch(biome.name().toLowerCase()::contains);
    }
}
