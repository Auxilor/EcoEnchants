package com.willfp.ecoenchants.enchantments;

import com.willfp.ecoenchants.config.ConfigManager;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Class for storing all enchantment rarities
 */
public class EnchantmentRarity {
    private static final Set<EnchantmentRarity> rarities = new HashSet<>();

    private final String name;
    private final double probability;
    private final int minimumLevel;
    private final double villagerProbability;
    private final double lootProbability;

    /**
     * Create new EnchantmentRarity
     * @param name The name of the rarity
     * @param probability The probability
     * @param minimumLevel The minimum xp level
     * @param villagerProbability The probability of a villager obtaining an enchantment with this rarity
     * @param lootProbability The probability of an item in a loot chest having an enchantment with this rarity
     */
    public EnchantmentRarity(String name, double probability, int minimumLevel, double villagerProbability, double lootProbability) {
        Optional<EnchantmentRarity> matching = rarities.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        matching.ifPresent(rarities::remove);

        this.name = name;
        this.probability = probability;
        this.minimumLevel = minimumLevel;
        this.villagerProbability = villagerProbability;
        this.lootProbability = lootProbability;

        rarities.add(this);
    }

    /**
     * Get the name of the rarity
     * @return The name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the probability of obtaining enchantment with this rarity from an enchanting table
     * @return The probability as a percentage
     */
    public double getProbability() {
        return this.probability;
    }

    /**
     * Get the probability of obtaining enchantment with this rarity from a villager
     * @return The probability as a percentage
     */
    public double getVillagerProbability() {
        return this.villagerProbability;
    }

    /**
     * Get the probability of obtaining enchantment with this rarity from a loot chest
     * @return The probability as a percentage
     */
    public double getLootProbability() {
        return this.lootProbability;
    }

    /**
     * Get the minimum level required to obtain enchantment with this rarity from an enchanting table
     * @return The minimum level
     */
    public int getMinimumLevel() {
        return this.minimumLevel;
    }

    /**
     * Get EnchantmentRarity matching name
     * @param name The name to search for
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
    public static void update() {
        Set<String> raritiesNames = ConfigManager.getConfig().getRarities();
        raritiesNames.forEach((rarity) -> {
            String name = rarity;
            double probability = ConfigManager.getConfig().getDouble("obtaining.rarities." + rarity + ".table-probability");
            int minimumLevel = ConfigManager.getConfig().getInt("obtaining.rarities." + rarity + ".minimum-level");
            double villagerProbability = ConfigManager.getConfig().getDouble("obtaining.rarities." + rarity + ".villager-probability");
            double lootProbability = ConfigManager.getConfig().getDouble("obtaining.rarities." + rarity + ".loot-probability");

            new EnchantmentRarity(name, probability, minimumLevel, villagerProbability, lootProbability);
        });
    }

    /**
     * Get all rarities
     * @return A set of all rarities
     */
    public static Set<EnchantmentRarity> getAll() {
        return rarities;
    }
}
