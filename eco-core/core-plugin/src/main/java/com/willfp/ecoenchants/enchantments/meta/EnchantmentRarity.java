package com.willfp.ecoenchants.enchantments.meta;

import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.eco.util.integrations.placeholder.PlaceholderEntry;
import com.willfp.eco.util.integrations.placeholder.PlaceholderManager;
import com.willfp.ecoenchants.config.EcoEnchantsConfigs;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class EnchantmentRarity {
    /**
     * All registered rarities.
     */
    private static final Set<EnchantmentRarity> REGISTERED = new HashSet<>();

    /**
     * The name of the rarity.
     */
    @Getter
    private final String name;

    /**
     * The probability of getting an enchantment with this rarity from an enchanting table.
     */
    @Getter
    private final double tableProbability;

    /**
     * The minimum xp level to get an enchantment of this rarity from an enchanting table.
     */
    @Getter
    private final int minimumLevel;

    /**
     * The probability of a villager obtaining an enchantment with this rarity.
     */
    @Getter
    private final double villagerProbability;

    /**
     * The probability of an item in a loot chest having an enchantment with this rarity.
     */
    @Getter
    private final double lootProbability;

    /**
     * The custom display color, or null if not enabled.
     */
    @Getter
    private final String customColor;

    /**
     * Create new EnchantmentRarity.
     *
     * @param name                The name of the rarity
     * @param tableProbability    The probability of getting an enchantment with this rarity from an enchanting table.
     * @param minimumLevel        The minimum xp level
     * @param villagerProbability The probability of a villager obtaining an enchantment with this rarity
     * @param lootProbability     The probability of an item in a loot chest having an enchantment with this rarity
     * @param customColor         The custom display color, or null if not enabled
     */
    public EnchantmentRarity(@NotNull final String name,
                             final double tableProbability,
                             final int minimumLevel,
                             final double villagerProbability,
                             final double lootProbability,
                             @Nullable final String customColor) {
        this.name = name;
        this.tableProbability = tableProbability;
        this.minimumLevel = minimumLevel;
        this.villagerProbability = villagerProbability;
        this.lootProbability = lootProbability;
        this.customColor = customColor;
    }

    public void register() {
        Optional<EnchantmentRarity> matching = REGISTERED.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        matching.ifPresent(REGISTERED::remove);

        PlaceholderManager.registerPlaceholder(new PlaceholderEntry(
                "rarity_" + this.getName() + "_probability",
                player -> NumberUtils.format(this.tableProbability)
        ));
        PlaceholderManager.registerPlaceholder(new PlaceholderEntry(
                "rarity_" + this.getName() + "_minlevel",
                player -> NumberUtils.format(this.minimumLevel)
        ));
        PlaceholderManager.registerPlaceholder(new PlaceholderEntry(
                "rarity_" + this.getName() + "_villagerprobability",
                player -> NumberUtils.format(this.villagerProbability)
        ));
        PlaceholderManager.registerPlaceholder(new PlaceholderEntry(
                "rarity_" + this.getName() + "_lootprobability",
                player -> NumberUtils.format(this.lootProbability)
        ));
        PlaceholderManager.registerPlaceholder(new PlaceholderEntry(
                "rarity_" + this.getName() + "_color",
                player -> this.customColor
        ));

        REGISTERED.add(this);
    }

    /**
     * Is custom color enabled.
     *
     * @return If has enabled custom color.
     */
    public boolean hasCustomColor() {
        return this.customColor != null;
    }

    @Override
    public boolean equals(@NotNull final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnchantmentRarity)) {
            return false;
        }
        EnchantmentRarity that = (EnchantmentRarity) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    /**
     * Get EnchantmentRarity matching name.
     *
     * @param name The name to search for.
     * @return The matching EnchantmentRarity, or null if not found.
     */
    public static EnchantmentRarity getByName(@NotNull final String name) {
        Optional<EnchantmentRarity> matching = REGISTERED.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        return matching.orElse(null);
    }

    /**
     * Update all rarities.
     */
    @ConfigUpdater
    public static void update() {
        Set<String> raritiesNames = EcoEnchantsConfigs.RARITY.getRarities();
        raritiesNames.forEach(rarity -> {
            double probability = EcoEnchantsConfigs.RARITY.getDouble("rarities." + rarity + ".table-probability");
            int minimumLevel = EcoEnchantsConfigs.RARITY.getInt("rarities." + rarity + ".minimum-level");
            double villagerProbability = EcoEnchantsConfigs.RARITY.getDouble("rarities." + rarity + ".villager-probability");
            double lootProbability = EcoEnchantsConfigs.RARITY.getDouble("rarities." + rarity + ".loot-probability");
            String customColor = null;
            if (EcoEnchantsConfigs.RARITY.getBool("rarities." + rarity + ".custom-color.enabled")) {
                customColor = StringUtils.translate(EcoEnchantsConfigs.RARITY.getString("rarities." + rarity + ".custom-color.color"));
            }

            new EnchantmentRarity(rarity, probability, minimumLevel, villagerProbability, lootProbability, customColor).register();
        });
    }

    /**
     * Get all rarities.
     *
     * @return A set of all rarities.
     */
    public static Set<EnchantmentRarity> values() {
        return REGISTERED;
    }

    static {
        update();
    }
}
