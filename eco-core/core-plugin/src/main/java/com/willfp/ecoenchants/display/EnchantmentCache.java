package com.willfp.ecoenchants.display;

import com.google.common.collect.ImmutableMap;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.eco.core.display.Display;
import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.StringUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.display.options.NumbersOptions;
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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
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
                    new HashMap<>(Map.of(1, Collections.singletonList(Display.PREFIX + "INVALID ENCHANTMENT: " + enchantment.getClass().getName()))),
                    EnchantmentType.NORMAL,
                    EnchantmentRarity.getByName(PLUGIN.getConfigYml().getString("rarity.vanilla-rarity")),
                    "&7"
            ));
            return;
        }

        String name;
        String color;
        EnchantmentType type;
        EnchantmentRarity rarity;
        List<String> description;
        if (enchantment instanceof EcoEnchant ecoEnchant) {
            description = StringUtils.formatList(ecoEnchant.getWrappedDescription());
            name = ecoEnchant.getDisplayName();
            type = ecoEnchant.getType();
            rarity = ecoEnchant.getEnchantmentRarity();
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


        if (rarity == null) {
            rarity = EnchantmentRarity.getByName(PLUGIN.getConfigYml().getString("rarity.vanilla-rarity"));
        }

        if (rarity.hasCustomColor() && type != EnchantmentType.CURSE) {
            color = rarity.getCustomColor();
        }

        description.replaceAll(line -> line.replace("§r", "§r" + PLUGIN.getDisplayModule().getOptions().getDescriptionOptions().getColor()));
        description.replaceAll(line -> line.replace("&r", "&r" + PLUGIN.getDisplayModule().getOptions().getDescriptionOptions().getColor()));
        description.replaceAll(line -> Display.PREFIX + PLUGIN.getDisplayModule().getOptions().getDescriptionOptions().getColor() + line);

        Map<Integer, List<String>> levelDescription = new HashMap<>();

        if (enchantment instanceof EcoEnchant ecoEnchant) {
            for (int i = 1; i <= ecoEnchant.getMaxLevel(); i++) {
                List<String> levelDesc = new ArrayList<>();
                for (String s : description) {
                    levelDesc.add(s.replace("%value%", ecoEnchant.getPlaceholder(i)));
                }

                levelDescription.put(
                        i,
                        levelDesc
                );
            }
        } else {
            for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                levelDescription.put(i, description);
            }
        }
        CACHE.put(enchantment.getKey(), new CacheEntry(enchantment, name, levelDescription, type, rarity, color));
    }

    @ToString
    public static final class CacheEntry {
        /**
         * The enchantment that this cache is for.
         */
        @Getter
        private final Enchantment enchantment;

        /**
         * The name of the enchantment.
         */
        private final String name;

        /**
         * The default color of the enchantment.
         */
        @Getter
        private final String color;

        /**
         * The description, line-wrapped.
         */
        private final Map<Integer, List<String>> description;

        /**
         * The description, not line-wrapped or colorized.
         */
        private final Map<Integer, String> stringDescription;

        /**
         * The type of the enchantment.
         */
        @Getter
        private final EnchantmentType type;

        /**
         * The requirement lore.
         */
        @Getter
        private final List<String> requirementLore;

        /**
         * The rarity of the enchantment.
         */
        @Getter
        private final EnchantmentRarity rarity;

        private CacheEntry(@NotNull final Enchantment enchantment,
                           @NotNull final String name,
                           @NotNull final Map<Integer, List<String>> description,
                           @NotNull final EnchantmentType type,
                           @NotNull final EnchantmentRarity rarity,
                           @NotNull final String color) {
            this.enchantment = enchantment;
            this.name = name;
            this.description = description;
            this.type = type;
            this.rarity = rarity;
            this.color = color;
            this.stringDescription = new HashMap<>();
            this.requirementLore = new ArrayList<>();
            if (enchantment instanceof EcoEnchant ecoEnchant) {
                for (String s : ecoEnchant.getRequirementLore()) {
                    requirementLore.add(Display.PREFIX + s);
                }
            }

            for (Integer level : description.keySet()) {
                StringBuilder descriptionBuilder = new StringBuilder();

                for (String s : description.get(level)) {
                    descriptionBuilder.append(s);
                    descriptionBuilder.append(" ");
                }

                String processedStringDescription = descriptionBuilder.toString();
                processedStringDescription = processedStringDescription.replace(Display.PREFIX, "");
                stringDescription.put(level, processedStringDescription.replaceAll(PLUGIN.getDisplayModule().getOptions().getDescriptionOptions().getColor(), ""));
            }
        }

        /**
         * Get the name with the level.
         *
         * @param level The level.
         * @return The name with the level.
         */
        public String getNameWithLevel(final int level) {
            return getNameWithLevel(level, null);
        }

        /**
         * Get enchantment with level.
         *
         * @param level  The level.
         * @param player The player.
         * @return The name with the level.
         */
        public String getNameWithLevel(final int level,
                                       @Nullable final Player player) {
            String processed = name;

            if (enchantment instanceof EcoEnchant enchant && player != null) {
                if (!enchant.areRequirementsMet(player)) {
                    processed = PLUGIN.getDisplayModule().getOptions().getRequirementsOptions().getRequirementColor() + processed;
                }
            }

            if (!(enchantment.getMaxLevel() == 1 && level == 1) && level != 0) {
                String numberString = " ";

                NumbersOptions numbersOptions = PLUGIN.getDisplayModule().getOptions().getNumbersOptions();

                if (numbersOptions.isUseNumerals() && level < numbersOptions.getThreshold()) {
                    numberString += NumberUtils.toNumeral(level);
                } else {
                    numberString += level;
                }

                if (level > enchantment.getMaxLevel() && PLUGIN.getDisplayModule().getOptions().getMaxLevelOptions().isReformatAboveMaxLevel()) {
                    if (PLUGIN.getDisplayModule().getOptions().getMaxLevelOptions().isNumbersOnly()) {
                        String aboveMaxLevel = PLUGIN.getDisplayModule().getOptions().getMaxLevelOptions().getAboveMaxLevelFormat();

                        processed = processed + aboveMaxLevel + numberString;
                    } else {
                        String aboveMaxLevel = PLUGIN.getDisplayModule().getOptions().getMaxLevelOptions().getAboveMaxLevelFormat();
                        processed = aboveMaxLevel + processed + numberString;
                    }
                } else {
                    processed = processed + numberString;
                }
            }

            processed = color + processed;
            processed = StringUtils.format(processed, StringUtils.FormatOption.WITHOUT_PLACEHOLDERS);
            return processed;
        }

        /**
         * Get the description of an enchantment at a certain level.
         *
         * @param level The level.
         * @return The description, wrapped and formatted.
         */
        public List<String> getDescription(final int level) {
            List<String> description = this.description.get(level);
            if (description == null) {
                if (enchantment instanceof EcoEnchant enchant) {
                    List<String> levelDesc = new ArrayList<>();
                    List<String> defDesc = enchant.getWrappedDescription();
                    defDesc.replaceAll(line -> line.replace("&r", "&r" + PLUGIN.getDisplayModule().getOptions().getDescriptionOptions().getColor()));
                    defDesc.replaceAll(line -> Display.PREFIX + PLUGIN.getDisplayModule().getOptions().getDescriptionOptions().getColor() + line);
                    for (String s : defDesc) {
                        levelDesc.add(s.replace("%value%", enchant.getPlaceholder(level)));
                    }

                    this.description.put(
                            level,
                            StringUtils.formatList(levelDesc)
                    );
                } else {
                    List<String> baseDesc = this.description.get(1);
                    this.description.put(level, baseDesc);
                    return baseDesc;
                }

                return getDescription(level);
            } else {
                return description;
            }
        }

        /**
         * Get the description of an enchantment at a certain level.
         *
         * @param level The level.
         * @return The description, unwrapped and unformatted.
         */
        public String getStringDescription(final int level) {
            String stringDesc = this.stringDescription.get(level);
            if (stringDesc == null) {
                StringBuilder descriptionBuilder = new StringBuilder();

                for (String s : description.get(level)) {
                    descriptionBuilder.append(s);
                    descriptionBuilder.append(" ");
                }

                String processedStringDescription = descriptionBuilder.toString();
                processedStringDescription = processedStringDescription.replace(Display.PREFIX, "");
                stringDescription.put(level, processedStringDescription.replaceAll(PLUGIN.getDisplayModule().getOptions().getDescriptionOptions().getColor(), ""));
                return getStringDescription(level);
            } else {
                return stringDesc;
            }
        }

        /**
         * Get the description of an enchantment at level 1.
         *
         * @return The description, wrapped and formatted.
         */
        @Deprecated
        public List<String> getDescription() {
            return getDescription(1);
        }

        /**
         * Get the description of an enchantment at level 1.
         *
         * @return The description, unwrapped and unformatted.
         */
        @Deprecated
        public String getStringDescription() {
            return getStringDescription(1);
        }

        /**
         * Get the unformatted name of the enchantment.
         *
         * @return The raw name.
         */
        public String getRawName() {
            return name;
        }

        /**
         * Get the formatted name of the enchantment.
         *
         * @return The name.
         */
        public String getName() {
            return getNameWithLevel(0);
        }
    }
}
