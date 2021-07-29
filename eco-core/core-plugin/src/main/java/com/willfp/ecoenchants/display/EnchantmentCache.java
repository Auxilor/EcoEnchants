package com.willfp.ecoenchants.display;

import com.google.common.collect.ImmutableMap;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.eco.core.display.Display;
import com.willfp.eco.util.StringUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
@SuppressWarnings("deprecation")
public class EnchantmentCache {
    /**
     * Instance of EcoEnchants.
     */
    public static final EcoEnchantsPlugin PLUGIN = EcoEnchantsPlugin.getInstance();

    /**
     * The physical cache.
     */
    private static final Map<NamespacedKey, CacheEntry> CACHE = new HashMap<>();

    /**
     * Get the {@link CacheEntry} for a specific enchantment.
     * <p>
     * Returns a default "broken" cache entry if not cached.
     *
     * @param enchantment The enchantment to query.
     * @return The found cache entry.
     */
    public static CacheEntry getEntry(@NotNull final Enchantment enchantment) {
        CacheEntry matching = CACHE.get(enchantment.getKey());
        if (matching != null) {
            return matching;
        } else {
            updateEnchantment(enchantment);
            Bukkit.getLogger().warning(enchantment.getKey() + " (from class " + enchantment.getClass() + ") was not cached! Trying to fix...");
            return getEntry(enchantment);
        }
    }

    /**
     * Get the entire cache.
     *
     * @return An immutable map of the cache.
     */
    public static Map<NamespacedKey, CacheEntry> getCache() {
        return ImmutableMap.copyOf(CACHE);
    }

    /**
     * Update the cache.
     */
    @ConfigUpdater
    public static void update() {
        CACHE.clear();
        Arrays.asList(Enchantment.values()).forEach(EnchantmentCache::updateEnchantment);
    }

    private static void updateEnchantment(@NotNull final Enchantment enchantment) {
        CACHE.remove(enchantment.getKey());

        if (enchantment instanceof EnchantmentWrapper) {
            Bukkit.getLogger().severe("Found erroneous enchantment registration!");
            Bukkit.getLogger().severe("Enchantment " + enchantment.getKey()
                    + " (Found in class " + enchantment.getClass().getName() + ", Path: " + enchantment.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + ")"
            );
            Bukkit.getLogger().severe("Tell the author to lean how enchantments are stored internally.");
            Bukkit.getLogger().severe("Hint: Extend Enchantment instead of EnchantmentWrapper.");
            CACHE.put(enchantment.getKey(), new CacheEntry(
                    enchantment,
                    "&4INVALID ENCHANTMENT",
                    "INVALID",
                    Collections.singletonList(Display.PREFIX + "INVALID ENCHANTMENT: " + enchantment.getClass().getName()),
                    EnchantmentType.NORMAL,
                    EnchantmentRarity.getByName(PLUGIN.getConfigYml().getString("rarity.vanilla-rarity"))
            ));
            return;
        }

        String name;
        String color;
        EnchantmentType type;
        EnchantmentRarity rarity;
        List<String> description;
        if (enchantment instanceof EcoEnchant ecoEnchant) {
            description = ecoEnchant.getWrappedDescription();
            name = ecoEnchant.getDisplayName();
            type = ecoEnchant.getType();
            rarity = ecoEnchant.getRarity();
        } else {
            description = Arrays.asList(
                    WordUtils.wrap(
                            PLUGIN.getLangYml().getString("enchantments." + enchantment.getKey().getKey().toLowerCase() + ".description"),
                            PLUGIN.getConfigYml().getInt("lore.describe.wrap"),
                            "\n", false
                    ).split("\\r?\\n")
            );
            name = PLUGIN.getLangYml().getString("enchantments." + enchantment.getKey().getKey().toLowerCase() + ".name");
            type = enchantment.isCursed() ? EnchantmentType.CURSE : EnchantmentType.NORMAL;
            if (enchantment.isTreasure()) {
                rarity = EnchantmentRarity.getByName(PLUGIN.getConfigYml().getString("rarity.vanilla-treasure-rarity"));
            } else {
                rarity = EnchantmentRarity.getByName(PLUGIN.getConfigYml().getString("rarity.vanilla-rarity"));
            }
        }

        color = type.getColor();

        if (rarity != null && rarity.hasCustomColor() && type != EnchantmentType.CURSE) {
            color = rarity.getCustomColor();
        }

        if (rarity == null) {
            rarity = EnchantmentRarity.getByName(PLUGIN.getConfigYml().getString("rarity.vanilla-rarity"));
        }

        String rawName = name;
        if (color.contains("{}")) {
            name = StringUtils.format(color.replace("{}", name));
        } else {
            name = color + name;
        }
        description.replaceAll(line -> Display.PREFIX + PLUGIN.getDisplayModule().getOptions().getDescriptionOptions().getColor() + line);
        CACHE.put(enchantment.getKey(), new CacheEntry(enchantment, name, rawName, description, type, rarity));
    }

    @ToString
    public static final class CacheEntry {
        /**
         * The enchantment that this cache is for.
         */
        @Getter
        private final Enchantment enchantment;

        /**
         * The formatted name of the enchantment.
         */
        @Getter
        private final String name;

        /**
         * The raw (unformatted) name of the enchantment.
         */
        @Getter
        private final String rawName;

        /**
         * The description, line-wrapped.
         */
        @Getter
        private final List<String> description;

        /**
         * The description, not line-wrapped or colorized.
         */
        @Getter
        private final String stringDescription;

        /**
         * The type of the enchantment.
         */
        @Getter
        private final EnchantmentType type;

        /**
         * The rarity of the enchantment.
         */
        @Getter
        private final EnchantmentRarity rarity;

        private CacheEntry(@NotNull final Enchantment enchantment,
                           @NotNull final String name,
                           @NotNull final String rawName,
                           @NotNull final List<String> description,
                           @NotNull final EnchantmentType type,
                           @NotNull final EnchantmentRarity rarity) {
            this.enchantment = enchantment;
            this.name = name;
            this.rawName = rawName;
            this.description = description;
            this.type = type;
            this.rarity = rarity;

            StringBuilder descriptionBuilder = new StringBuilder();

            description.forEach(s -> {
                descriptionBuilder.append(s);
                descriptionBuilder.append(" ");
            });

            String processedStringDescription = descriptionBuilder.toString();
            processedStringDescription = processedStringDescription.replace(Display.PREFIX, "");
            this.stringDescription = processedStringDescription.replaceAll(PLUGIN.getDisplayModule().getOptions().getDescriptionOptions().getColor(), "");
        }
    }
}
