package com.willfp.ecoenchants.biomes.enchants.offensive;

import com.willfp.ecoenchants.biomes.BiomesEnchantment;
import org.bukkit.block.Biome;

import java.util.Arrays;

public class Altitude extends BiomesEnchantment {
    public Altitude() {
        super("altitude", EnchantmentType.NORMAL);
    }

    @Override
    public boolean isValid(Biome biome) {
        return Arrays.stream(new String[]{"mountain", "hill"}).anyMatch(biome.name().toLowerCase()::contains);
    }
}
