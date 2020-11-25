package com.willfp.ecoenchants.biomes.enchants.defensive;

import com.willfp.ecoenchants.biomes.BiomesEnchantment;
import org.bukkit.block.Biome;

import java.util.Arrays;

public class Glacial extends BiomesEnchantment {
    public Glacial() {
        super("glacial", EnchantmentType.NORMAL);
    }

    @Override
    public boolean isValid(Biome biome) {
        return Arrays.stream(new String[]{"snowy", "ice", "frozen"}).anyMatch(biome.name().toLowerCase()::contains);
    }
}
