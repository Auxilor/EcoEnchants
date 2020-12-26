package com.willfp.ecoenchants.display.options;

import com.willfp.eco.util.config.Configs;
import com.willfp.ecoenchants.display.options.sorting.EnchantmentSorter;
import com.willfp.ecoenchants.display.options.sorting.SortParameters;
import com.willfp.ecoenchants.display.options.sorting.SorterManager;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DisplayOptions {
    private EnchantmentSorter sorter;
    private final DescriptionOptions descriptionOptions = new DescriptionOptions();
    private final NumbersOptions numbersOptions = new NumbersOptions();
    private final ShrinkOptions shrinkOptions = new ShrinkOptions();
    private final List<EnchantmentType> sortedTypes = new ArrayList<>();
    private final List<EnchantmentRarity> sortedRarities = new ArrayList<>();

    public DisplayOptions() {
        update();
    }

    public String getDescriptionColor() {
        return descriptionOptions.getColor();
    }

    public int getNumbersThreshold() {
        return numbersOptions.getThreshold();
    }

    public boolean isUseNumerals() {
        return numbersOptions.useNumerals();
    }

    public int getDescribeThreshold() {
        return descriptionOptions.getThreshold();
    }

    public boolean isUseDescribe() {
        return descriptionOptions.isEnabled();
    }

    public int getShrinkThreshold() {
        return shrinkOptions.getThreshold();
    }

    public int getShrinkPerLine() {
        return shrinkOptions.getShrinkPerLine();
    }

    public boolean isUseShrink() {
        return shrinkOptions.isEnabled();
    }

    public List<EnchantmentType> getSortedTypes() {
        return sortedTypes;
    }

    public List<EnchantmentRarity> getSortedRarities() {
        return sortedRarities;
    }

    public EnchantmentSorter getSorter() {
        return sorter;
    }

    public void update() {
        descriptionOptions.update();
        numbersOptions.update();
        shrinkOptions.update();

        sortedTypes.clear();
        sortedTypes.addAll(Configs.CONFIG.getStrings("lore.type-ordering").stream()
                .map(typeName -> EnchantmentType.values().stream().filter(type -> type.getName().equalsIgnoreCase(typeName)).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        sortedTypes.addAll(EnchantmentType.values().stream().filter(enchantmentType -> !sortedTypes.contains(enchantmentType)).collect(Collectors.toList()));

        sortedRarities.clear();
        sortedRarities.addAll(Configs.CONFIG.getStrings("lore.rarity-ordering").stream()
                .map(rarityName -> EnchantmentRarity.values().stream().filter(rarity -> rarity.getName().equalsIgnoreCase(rarityName)).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        sortedRarities.addAll(EnchantmentRarity.values().stream().filter(enchantmentRarity -> !sortedRarities.contains(enchantmentRarity)).collect(Collectors.toList()));

        boolean byType = Configs.CONFIG.getBool("lore.sort-by-type");
        boolean byLength = Configs.CONFIG.getBool("lore.sort-by-length");
        boolean byRarity = Configs.CONFIG.getBool("lore.sort-by-rarity");
        Set<SortParameters> params = new HashSet<>();
        if (byType) {
            params.add(SortParameters.TYPE);
        }
        if (byLength) {
            params.add(SortParameters.LENGTH);
        }
        if (byRarity) {
            params.add(SortParameters.RARITY);
        }

        sorter = SorterManager.getSorter(params.toArray(new SortParameters[]{}));
    }
}
