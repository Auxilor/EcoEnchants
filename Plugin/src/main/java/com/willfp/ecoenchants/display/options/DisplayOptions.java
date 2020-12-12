package com.willfp.ecoenchants.display.options;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.display.options.sorting.*;

public class DisplayOptions {
    private EnchantmentSorter sorter;
    private final DescriptionOptions descriptionOptions = new DescriptionOptions();
    private final NumbersOptions numbersOptions = new NumbersOptions();
    private final ShrinkOptions shrinkOptions = new ShrinkOptions();

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

    public EnchantmentSorter getSorter() {
        return sorter;
    }

    public void update() {
        descriptionOptions.update();
        numbersOptions.update();
        shrinkOptions.update();

        boolean byType = ConfigManager.getConfig().getBool("lore.sort-by-type");
        boolean byLength = ConfigManager.getConfig().getBool("lore.sort-by-length");
        if (byType && byLength) sorter = new TypeLengthSorter();
        if (byType && !byLength) sorter = new TypeAlphabeticSorter();
        if (!byType && byLength) sorter = new LengthSorter();
        if (!byType && !byLength) sorter = new AlphabeticSorter();
    }
}
