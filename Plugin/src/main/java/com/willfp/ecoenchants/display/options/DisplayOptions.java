package com.willfp.ecoenchants.display.options;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.display.options.sorting.AlphabeticSorter;
import com.willfp.ecoenchants.display.options.sorting.EnchantmentSorter;
import com.willfp.ecoenchants.display.options.sorting.LengthSorter;
import com.willfp.ecoenchants.display.options.sorting.TypeAlphabeticSorter;
import com.willfp.ecoenchants.display.options.sorting.TypeLengthSorter;
import com.willfp.ecoenchants.enchantments.EcoEnchant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DisplayOptions {
    private EnchantmentSorter sorter;
    private final DescriptionOptions descriptionOptions = new DescriptionOptions();
    private final NumbersOptions numbersOptions = new NumbersOptions();
    private final ShrinkOptions shrinkOptions = new ShrinkOptions();
    private final List<EcoEnchant.EnchantmentType> sortedTypes = new ArrayList<>();

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

    public List<EcoEnchant.EnchantmentType> getSortedTypes() {
        return sortedTypes;
    }

    public EnchantmentSorter getSorter() {
        return sorter;
    }

    public void update() {
        descriptionOptions.update();
        numbersOptions.update();
        shrinkOptions.update();

        sortedTypes.clear();
        sortedTypes.addAll(ConfigManager.getConfig().getStrings("lore.type-ordering").stream()
                .map(typeName -> EcoEnchant.EnchantmentType.values().stream().filter(type -> type.getName().equalsIgnoreCase(typeName)).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        sortedTypes.addAll(EcoEnchant.EnchantmentType.values().stream().filter(enchantmentType -> !sortedTypes.contains(enchantmentType)).collect(Collectors.toList()));

        boolean byType = ConfigManager.getConfig().getBool("lore.sort-by-type");
        boolean byLength = ConfigManager.getConfig().getBool("lore.sort-by-length");
        if (byType && byLength) sorter = new TypeLengthSorter();
        if (byType && !byLength) sorter = new TypeAlphabeticSorter();
        if (!byType && byLength) sorter = new LengthSorter();
        if (!byType && !byLength) sorter = new AlphabeticSorter();
    }
}
