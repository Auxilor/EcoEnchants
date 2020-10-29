package com.willfp.ecoenchants.display;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.util.Logger;
import org.apache.commons.lang.WordUtils;
import org.bukkit.enchantments.Enchantment;

import java.util.*;

public class EnchantmentCache {
    private static final Set<CacheEntry> CACHE = new HashSet<>();

    static {
        update();
    }

    public static CacheEntry getEntry(Enchantment enchantment) {
        Optional<CacheEntry> matching = CACHE.stream().filter(enchant -> enchant.getEnchantment().getKey().equals(enchantment.getKey())).findFirst();
        return matching.orElse(new CacheEntry(enchantment, enchantment.getKey().getKey(), enchantment.getKey().getKey(), Collections.singletonList("No Description Found")));
    }

    public static void update() {
        CACHE.clear();
        Arrays.asList(Enchantment.values()).parallelStream().forEach(enchantment -> {
            String name;
            String color;
            EcoEnchant.EnchantmentType type;
            List<String> description;
            if(EcoEnchants.getFromEnchantment(enchantment) != null) {
                EcoEnchant ecoEnchant = EcoEnchants.getFromEnchantment(enchantment);
                description = ecoEnchant.getDescription();
                name = ecoEnchant.getName();
                type = ecoEnchant.getType();
            } else {
                description = Arrays.asList(
                        WordUtils.wrap(
                                String.valueOf(ConfigManager.getLang().getString("enchantments." + enchantment.getKey().getKey().toLowerCase() + ".description")),
                                ConfigManager.getConfig().getInt("lore.describe.wrap"),
                                "\n", false
                        ).split("\\r?\\n")
                );
                name = String.valueOf(ConfigManager.getLang().getString("enchantments." + enchantment.getKey().getKey().toLowerCase() + ".name"));
                type = enchantment.isCursed() ? EcoEnchant.EnchantmentType.CURSE : EcoEnchant.EnchantmentType.NORMAL;
            }

            switch(type) {
                case ARTIFACT:
                    color = EnchantDisplay.artifactColor;
                    break;
                case SPECIAL:
                    color = EnchantDisplay.specialColor;
                    break;
                case CURSE:
                    color = EnchantDisplay.curseColor;
                    break;
                default:
                    color = EnchantDisplay.normalColor;
                    break;
            }

            if(EcoEnchants.getFromEnchantment(enchantment) != null) {
                EnchantmentRarity rarity = EcoEnchants.getFromEnchantment(enchantment).getRarity();
                if(rarity != null) {
                    if (rarity.hasCustomColor() && type != EcoEnchant.EnchantmentType.CURSE) {
                        color = rarity.getCustomColor();
                    }
                } else {
                    Logger.warn("Enchantment " + enchantment.getKey().getKey() + " has an invalid rarity");
                }
            }

            String rawName = name;
            name = color + name;
            description.replaceAll(line -> EnchantDisplay.PREFIX + EnchantDisplay.descriptionColor + line);
            CACHE.add(new CacheEntry(enchantment, name, rawName, description));
        });
    }


    public static class CacheEntry {
        private final Enchantment enchantment;
        private String name;
        private String rawName;
        private List<String> description;

        public CacheEntry(Enchantment enchantment, String name, String rawName, List<String> description) {
            this.enchantment = enchantment;
            this.name = name;
            this.rawName = rawName;
            this.description = description;
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
            StringBuilder descriptionBuilder = new StringBuilder();

            EnchantmentCache.getEntry(enchantment).getDescription().forEach(s -> {
                descriptionBuilder.append(s);
                descriptionBuilder.append(" ");
            });

            String description = descriptionBuilder.toString();
            description = description.replaceAll("§w", "");
            description = description.replaceAll(EnchantDisplay.descriptionColor, "");

            return description;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setRawName(String rawName) {
            this.rawName = rawName;
        }

        public void setDescription(List<String> description) {
            this.description = description;
        }
    }
}
