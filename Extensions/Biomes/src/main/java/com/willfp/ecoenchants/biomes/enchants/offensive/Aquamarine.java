package com.willfp.ecoenchants.biomes.enchants.offensive;

import com.willfp.ecoenchants.biomes.BiomesEnchantment;
import org.bukkit.block.Biome;

import java.util.Arrays;

public class Aquamarine extends BiomesEnchantment {
    public Aquamarine() {
        super("aquamarine", EnchantmentType.NORMAL);
    }

    @Override
    public boolean isValid(Biome biome) {
        return Arrays.stream(new String[]{"ocean"}).anyMatch(biome.name().toLowerCase()::contains);
    }
}
