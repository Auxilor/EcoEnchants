package com.willfp.ecoenchants.display.options.sorting;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.display.options.sorting.implementations.AlphabeticSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.LengthSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.RarityAlphabeticSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.RarityLengthSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.RarityTypeAlphabeticSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.RarityTypeLengthSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.TypeAlphabeticSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.TypeLengthSorter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class SorterManager {
    /**
     * All registered enchantment sorters.
     */
    private static final Set<EnchantmentSorter> REGISTERED = new HashSet<>();

    static {
        EcoEnchantsPlugin instance = EcoEnchantsPlugin.getInstance(); // Really dirty and janky.
        REGISTERED.add(new AlphabeticSorter(instance));
        REGISTERED.add(new LengthSorter(instance));
        REGISTERED.add(new TypeAlphabeticSorter(instance));
        REGISTERED.add(new TypeLengthSorter(instance));
        REGISTERED.add(new RarityAlphabeticSorter(instance));
        REGISTERED.add(new RarityLengthSorter(instance));
        REGISTERED.add(new RarityTypeAlphabeticSorter(instance));
        REGISTERED.add(new RarityTypeLengthSorter(instance));
    }

    /**
     * Get a sorter based off of parameters.
     * <p>
     * Any combination of parameters is valid.
     *
     * @param parameters The parameters to find a sorter from.
     * @return The matching sorter.
     */
    public static EnchantmentSorter getSorter(@NotNull final SortParameters... parameters) {
        return REGISTERED.stream()
                .filter(enchantmentSorter -> Arrays.asList(enchantmentSorter.getParameters()).containsAll(Arrays.asList(parameters)) && enchantmentSorter.getParameters().length == parameters.length)
                .findFirst()
                .orElse(new AlphabeticSorter(EcoEnchantsPlugin.getInstance()));
    }
}
