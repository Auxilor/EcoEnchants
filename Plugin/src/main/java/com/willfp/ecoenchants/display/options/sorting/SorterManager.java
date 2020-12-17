package com.willfp.ecoenchants.display.options.sorting;

import com.willfp.ecoenchants.display.options.sorting.implementations.AlphabeticSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.LengthSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.RarityAlphabeticSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.RarityLengthSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.RarityTypeAlphabeticSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.RarityTypeLengthSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.TypeAlphabeticSorter;
import com.willfp.ecoenchants.display.options.sorting.implementations.TypeLengthSorter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SorterManager {
    private static final Set<EnchantmentSorter> values = new HashSet<>();

    public static EnchantmentSorter getSorter(SortParameters... parameters) {
        return values.stream()
                .filter(enchantmentSorter -> Arrays.asList(enchantmentSorter.getParameters()).containsAll(Arrays.asList(parameters)) && enchantmentSorter.getParameters().length == parameters.length)
                .findFirst()
                .orElse(new AlphabeticSorter());
    }

    static {
        values.add(new AlphabeticSorter());
        values.add(new LengthSorter());
        values.add(new TypeAlphabeticSorter());
        values.add(new TypeLengthSorter());
        values.add(new RarityAlphabeticSorter());
        values.add(new RarityLengthSorter());
        values.add(new RarityTypeAlphabeticSorter());
        values.add(new RarityTypeLengthSorter());
    }
}
