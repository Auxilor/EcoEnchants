package com.willfp.ecoenchants.display.options.sorting;

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
                .orElse(new AlphabeticSorter());
    }

    static {
        REGISTERED.add(new AlphabeticSorter());
        REGISTERED.add(new LengthSorter());
        REGISTERED.add(new TypeAlphabeticSorter());
        REGISTERED.add(new TypeLengthSorter());
        REGISTERED.add(new RarityAlphabeticSorter());
        REGISTERED.add(new RarityLengthSorter());
        REGISTERED.add(new RarityTypeAlphabeticSorter());
        REGISTERED.add(new RarityTypeLengthSorter());
    }
}
