package com.willfp.ecoenchants.display;

import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.config.annotations.ConfigUpdater;
import com.willfp.eco.util.interfaces.Updatable;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
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

@UtilityClass
@SuppressWarnings("deprecation")
public class EnchantmentCache implements Updatable {
    private static final Set<CacheEntry> CACHE = new HashSet<>();

    @SuppressWarnings("OptionalGetWithoutIsPresent")
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

    public static Set<CacheEntry> getCache() {
        return new HashSet<>(CACHE);
    }

    @ConfigUpdater
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
                description = ecoEnchant.getDescription();
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
                rarity = enchantment.isTreasure() ? EnchantmentRarity.getByName(Configs.CONFIG.getString("rarity.vanilla-treasure-rarity")) : EnchantmentRarity.getByName(Configs.CONFIG.getString("rarity.vanilla-rarity"));
            }

            color = type.getColor();

            if (rarity != null && rarity.hasCustomColor() && type != EnchantmentType.CURSE) {
                color = rarity.getCustomColor();
            }

            String rawName = name;
            name = color + name;
            description.replaceAll(line -> EnchantDisplay.PREFIX + EnchantDisplay.OPTIONS.getDescriptionColor() + line);
            CACHE.add(new CacheEntry(enchantment, name, rawName, description, type, rarity));
        });
    }

    @ToString
    public static class CacheEntry {
        private final Enchantment enchantment;
        private final String name;
        private final String rawName;
        private final List<String> description;
        private final String stringDescription;
        private final EnchantmentType type;
        private final EnchantmentRarity rarity;

        public CacheEntry(@NotNull final Enchantment enchantment,
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
            this.stringDescription = processedStringDescription.replaceAll(EnchantDisplay.OPTIONS.getDescriptionColor(), "");
        }

        public Enchantment getEnchantment() {
            return enchantment;
        }

        public String getName() {
            return name;
        }

        public String getRawName() {
            return rawName;
        }

        public List<String> getDescription() {
            return description;
        }

        public String getStringDescription() {
            return stringDescription;
        }

        public EnchantmentType getType() {
            return type;
        }

        public EnchantmentRarity getRarity() {
            return rarity;
        }
    }
}
