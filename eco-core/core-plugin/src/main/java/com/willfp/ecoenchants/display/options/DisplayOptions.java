package com.willfp.ecoenchants.display.options;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.ecoenchants.display.options.sorting.EnchantmentSorter;
import com.willfp.ecoenchants.display.options.sorting.SortParameters;
import com.willfp.ecoenchants.display.options.sorting.SorterManager;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DisplayOptions extends PluginDependent<EcoPlugin> {
    /**
     * The description options being used.
     */
    @Getter
    private final DescriptionOptions descriptionOptions = new DescriptionOptions(this.getPlugin());
    /**
     * The enchantment level options being used.
     */
    @Getter
    private final NumbersOptions numbersOptions = new NumbersOptions(this.getPlugin());
    /**
     * The shrink options being used.
     */
    @Getter
    private final ShrinkOptions shrinkOptions = new ShrinkOptions(this.getPlugin());
    /**
     * The max level options being used.
     */
    @Getter
    private final MaxLevelOptions maxLevelOptions = new MaxLevelOptions(this.getPlugin());
    /**
     * The requirements options being used.
     */
    @Getter
    private final RequirementsOptions requirementsOptions = new RequirementsOptions(this.getPlugin());
    /**
     * The enchantment types, sorted according to config.
     */
    @Getter
    private final List<EnchantmentType> sortedTypes = new ArrayList<>();
    /**
     * The enchantment rarities, sorted according to config.
     */
    @Getter
    private final List<EnchantmentRarity> sortedRarities = new ArrayList<>();
    /**
     * The enchantment sorter being used.
     */
    @Getter
    private EnchantmentSorter sorter;

    /**
     * If item must be a target.
     */
    @Getter
    private boolean requireTarget = true;

    /**
     * If enchants should be displayed above lore.
     */
    @Getter
    private boolean aboveLore = true;

    /**
     * Instantiate new display options.
     *
     * @param plugin EcoEnchants.
     */
    @ApiStatus.Internal
    public DisplayOptions(@NotNull final EcoPlugin plugin) {
        super(plugin);
        update();
    }

    /**
     * Update all options.
     */
    public void update() {
        descriptionOptions.update();
        numbersOptions.update();
        shrinkOptions.update();
        maxLevelOptions.update();
        requirementsOptions.update();

        sortedTypes.clear();
        sortedTypes.addAll(this.getPlugin().getConfigYml().getStrings("lore.type-ordering").stream()
                .map(typeName -> EnchantmentType.values().stream().filter(type -> type.getName().equalsIgnoreCase(typeName)).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        sortedTypes.addAll(EnchantmentType.values().stream().filter(enchantmentType -> !sortedTypes.contains(enchantmentType)).collect(Collectors.toList()));

        sortedRarities.clear();
        sortedRarities.addAll(this.getPlugin().getConfigYml().getStrings("lore.rarity-ordering").stream()
                .map(rarityName -> EnchantmentRarity.values().stream().filter(rarity -> rarity.getName().equalsIgnoreCase(rarityName)).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        sortedRarities.addAll(EnchantmentRarity.values().stream().filter(enchantmentRarity -> !sortedRarities.contains(enchantmentRarity)).collect(Collectors.toList()));

        requireTarget = this.getPlugin().getConfigYml().getBool("lore.require-target");
        aboveLore = this.getPlugin().getConfigYml().getBool("lore.above-other-lore");

        boolean byType = this.getPlugin().getConfigYml().getBool("lore.sort-by-type");
        boolean byLength = this.getPlugin().getConfigYml().getBool("lore.sort-by-length");
        boolean byRarity = this.getPlugin().getConfigYml().getBool("lore.sort-by-rarity");
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
