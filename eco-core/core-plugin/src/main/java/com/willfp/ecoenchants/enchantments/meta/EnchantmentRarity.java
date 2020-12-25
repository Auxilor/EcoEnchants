package com.willfp.ecoenchants.enchantments.meta;

import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.config.annotations.ConfigUpdater;
import com.willfp.eco.util.integrations.placeholder.PlaceholderEntry;
import com.willfp.eco.util.integrations.placeholder.PlaceholderManager;
import com.willfp.eco.util.interfaces.Registerable;
import com.willfp.eco.util.interfaces.Updatable;
import com.willfp.ecoenchants.config.EcoEnchantsConfigs;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Class for storing all enchantment rarities
 */
public class EnchantmentRarity implements Registerable, Updatable {
    private static final Set<EnchantmentRarity> rarities = new HashSet<>();

    private final String name;
    private final double probability;
    private final int minimumLevel;
    private final double villagerProbability;
    private final double lootProbability;
    private final String customColor;

    /**
     * Create new EnchantmentRarity
     *
     * @param name                The name of the rarity
     * @param probability         The probability
     * @param minimumLevel        The minimum xp level
     * @param villagerProbability The probability of a villager obtaining an enchantment with this rarity
     * @param lootProbability     The probability of an item in a loot chest having an enchantment with this rarity
     * @param customColor         The custom display color, or null if not enabled
     */
    public EnchantmentRarity(String name, double probability, int minimumLevel, double villagerProbability, double lootProbability, String customColor) {
        this.name = name;
        this.probability = probability;
        this.minimumLevel = minimumLevel;
        this.villagerProbability = villagerProbability;
        this.lootProbability = lootProbability;
        this.customColor = customColor;
    }

    @Override
    public void register() {
        Optional<EnchantmentRarity> matching = rarities.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        matching.ifPresent(rarities::remove);

        PlaceholderManager.registerPlaceholder(new PlaceholderEntry(
                "rarity_" + this.getName() + "_probability",
                (player) -> NumberUtils.format(this.probability)
        ));
        PlaceholderManager.registerPlaceholder(new PlaceholderEntry(
                "rarity_" + this.getName() + "_minlevel",
                (player) -> NumberUtils.format(this.minimumLevel)
        ));
        PlaceholderManager.registerPlaceholder(new PlaceholderEntry(
                "rarity_" + this.getName() + "_villagerprobability",
                (player) -> NumberUtils.format(this.villagerProbability)
        ));
        PlaceholderManager.registerPlaceholder(new PlaceholderEntry(
                "rarity_" + this.getName() + "_lootprobability",
                (player) -> NumberUtils.format(this.lootProbability)
        ));
        PlaceholderManager.registerPlaceholder(new PlaceholderEntry(
                "rarity_" + this.getName() + "_color",
                (player) -> this.customColor
        ));

        rarities.add(this);
    }

    /**
     * Get the name of the rarity
     *
     * @return The name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Is custom color enabled
     *
     * @return If has enabled custom color
     */
    public boolean hasCustomColor() {
        return this.customColor != null;
    }

    /**
     * Get custom color
     *
     * @return The custom color
     */
    public String getCustomColor() {
        return this.customColor;
    }

    /**
     * Get the probability of obtaining enchantment with this rarity from an enchanting table
     *
     * @return The probability as a percentage
     */
    public double getProbability() {
        return this.probability;
    }

    /**
     * Get the probability of obtaining enchantment with this rarity from a villager
     *
     * @return The probability as a percentage
     */
    public double getVillagerProbability() {
        return this.villagerProbability;
    }

    /**
     * Get the probability of obtaining enchantment with this rarity from a loot chest
     *
     * @return The probability as a percentage
     */
    public double getLootProbability() {
        return this.lootProbability;
    }

    /**
     * Get the minimum level required to obtain enchantment with this rarity from an enchanting table
     *
     * @return The minimum level
     */
    public int getMinimumLevel() {
        return this.minimumLevel;
    }

    /**
     * Get EnchantmentRarity matching name
     *
     * @param name The name to search for
     *
     * @return The matching EnchantmentRarity, or null if not found
     */
    public static EnchantmentRarity getByName(String name) {
        Optional<EnchantmentRarity> matching = rarities.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        return matching.orElse(null);
    }

    /**
     * Update all rarities
     * Called on /ecoreload
     */
    @ConfigUpdater
    public static void update() {
        Set<String> raritiesNames = EcoEnchantsConfigs.RARITY.getRarities();
        raritiesNames.forEach((rarity) -> {
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
     * Get all rarities
     *
     * @return A set of all rarities
     */
    public static Set<EnchantmentRarity> values() {
        return rarities;
    }

    static {
        update();
    }
}
