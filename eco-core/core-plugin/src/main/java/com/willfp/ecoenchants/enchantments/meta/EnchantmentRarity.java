package com.willfp.ecoenchants.enchantments.meta;

import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.config.RarityYml;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class EnchantmentRarity {
    /**
     * All registered rarities.
     */
    private static final Set<EnchantmentRarity> REGISTERED = new HashSet<>();

    static {
        update(EcoEnchantsPlugin.getInstance());
    }

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
     *
     * @param plugin Instance of EcoEnchants.
     */
    @ConfigUpdater
    public static void update(@NotNull final EcoEnchantsPlugin plugin) {
        RarityYml rarityYml = plugin.getRarityYml();
        List<String> raritiesNames = rarityYml.getRarities();
        raritiesNames.forEach(rarity -> {
            double probability = rarityYml.getDouble("rarities." + rarity + ".table-probability");
            int minimumLevel = rarityYml.getInt("rarities." + rarity + ".minimum-level");
            double villagerProbability = rarityYml.getDouble("rarities." + rarity + ".villager-probability");
            double lootProbability = rarityYml.getDouble("rarities." + rarity + ".loot-probability");
            String customColor = null;
            if (rarityYml.getBool("rarities." + rarity + ".custom-color.enabled")) {
                customColor = rarityYml.getString("rarities." + rarity + ".custom-color.color", false);
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

    /**
     * Register rarity.
     */
    private void register() {
        Optional<EnchantmentRarity> matching = REGISTERED.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        matching.ifPresent(REGISTERED::remove);

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
        if (!(o instanceof EnchantmentRarity that)) {
            return false;
        }
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
