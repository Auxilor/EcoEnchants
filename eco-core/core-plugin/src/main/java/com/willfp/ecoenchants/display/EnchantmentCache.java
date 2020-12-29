package com.willfp.ecoenchants.display;

import com.google.common.collect.ImmutableSet;
import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.config.annotations.Updatable;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.WordUtils;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("deprecation")
@Updatable
@UtilityClass
public class EnchantmentCache {
    /**
     * The physical cache.
     */
    private static final Set<CacheEntry> CACHE = new HashSet<>();

    /**
     * Get the {@link CacheEntry} for a specific enchantment.
     * <p>
     * Returns a default "broken" cache entry if not cached.
     *
     * @param enchantment The enchantment to query.
     * @return The found cache entry.
     */
    public static CacheEntry getEntry(@NotNull final Enchantment enchantment) {
        Optional<CacheEntry> matching = CACHE.stream().filter(entry -> entry.getEnchantment().getKey().getKey().equals(enchantment.getKey().getKey())).findFirst();
        return matching.orElse(
                new CacheEntry(
                        enchantment,
                        EnchantDisplay.PREFIX + "§7" + enchantment.getKey().getKey(),
                        enchantment.getKey().getKey(),
                        Collections.singletonList(EnchantDisplay.PREFIX + "No Description Found"),
                        EnchantmentType.NORMAL,
                        EnchantmentRarity.values().stream().findFirst().get()
                )
        );
    }

    /**
     * Get the entire cache.
     *
     * @return An immutable set of the cache.
     */
    public static Set<CacheEntry> getCache() {
        return ImmutableSet.copyOf(CACHE);
    }

    /**
     * Update the cache.
     */
    public static void update() {
        CACHE.clear();
        Arrays.asList(Enchantment.values()).parallelStream().forEach(enchantment -> {
            String name;
            String color;
            EnchantmentType type;
            EnchantmentRarity rarity;
            List<String> description;
            if (EcoEnchants.getFromEnchantment(enchantment) != null) {
                EcoEnchant ecoEnchant = EcoEnchants.getFromEnchantment(enchantment);
                description = ecoEnchant.getWrappedDescription();
                name = ecoEnchant.getName();
                type = ecoEnchant.getType();
                rarity = ecoEnchant.getRarity();
            } else {
                description = Arrays.asList(
                        WordUtils.wrap(
                                String.valueOf(Configs.LANG.getString("enchantments." + enchantment.getKey().getKey().toLowerCase() + ".description")),
                                Configs.CONFIG.getInt("lore.describe.wrap"),
                                "\n", false
                        ).split("\\r?\\n")
                );
                name = String.valueOf(Configs.LANG.getString("enchantments." + enchantment.getKey().getKey().toLowerCase() + ".name"));
                type = enchantment.isCursed() ? EnchantmentType.CURSE : EnchantmentType.NORMAL;
                if (enchantment.isTreasure()) {
                    rarity = EnchantmentRarity.getByName(Configs.CONFIG.getString("rarity.vanilla-treasure-rarity"));
                } else {
                    rarity = EnchantmentRarity.getByName(Configs.CONFIG.getString("rarity.vanilla-rarity"));
                }
            }

            color = type.getColor();

            if (rarity != null && rarity.hasCustomColor() && type != EnchantmentType.CURSE) {
                color = rarity.getCustomColor();
            }

            String rawName = name;
            name = color + name;
            description.replaceAll(line -> EnchantDisplay.PREFIX + EnchantDisplay.OPTIONS.getDescriptionOptions().getColor() + line);
            CACHE.add(new CacheEntry(enchantment, name, rawName, description, type, rarity));
        });
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
            processedStringDescription = processedStringDescription.replace("§w", "");
            this.stringDescription = processedStringDescription.replaceAll(EnchantDisplay.OPTIONS.getDescriptionOptions().getColor(), "");
        }
    }
}
