package com.willfp.ecoenchants.enchantments.support.vanilla;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.proxy.proxies.EcoCraftEnchantmentManagerProxy;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class VanillaEnchantments {
    /**
     * Vanilla Enchantment Metadata Map.
     */
    private static final Map<Enchantment, VanillaEnchantmentMetadata> MAP = new HashMap<>();

    /**
     * Get a map of all custom enchantment metadata.
     *
     * @return The map.
     */
    public Map<Enchantment, VanillaEnchantmentMetadata> getMetadataMap() {
        return MAP;
    }

    /**
     * Update the map.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public static void update(@NotNull final EcoEnchantsPlugin plugin) {
        Map<Enchantment, VanillaEnchantmentMetadata> map = new HashMap<>();

        List<Enchantment> enchantments = Arrays.stream(Enchantment.values())
                .filter(enchantment -> enchantment.getClass().getName().contains("CraftEnchantment"))
                .collect(Collectors.toList());

        Map<Enchantment, Integer> maxLevels = plugin.getVanillaEnchantsYml().getStrings("max-levels").stream()
                .collect(Collectors.toMap(
                        s -> Enchantment.getByKey(NamespacedKey.minecraft(s.split(":")[0].toLowerCase())),
                        s1 -> Integer.parseInt(s1.split(":")[1])
                ));

        Map<Enchantment, Set<NamespacedKey>> conflicts = plugin.getVanillaEnchantsYml().getStrings("conflicts").stream()
                .collect(Collectors.toMap(
                        s -> Enchantment.getByKey(NamespacedKey.minecraft(s.split(":")[0].toLowerCase())),
                        s1 -> {
                            String[] split = s1.split(":");
                            Set<NamespacedKey> keys = new HashSet<>();
                            for (int i = 1; i < split.length; i++) {
                                keys.add(NamespacedKey.minecraft(split[i]));
                            }

                            keys.removeIf(key -> key.getKey().equalsIgnoreCase("none"));

                            return keys;
                        }
                ));

        for (Enchantment enchantment : enchantments) {
            VanillaEnchantmentMetadata metadata = new VanillaEnchantmentMetadata(maxLevels.get(enchantment), conflicts.get(enchantment));

            map.put(enchantment, metadata);
        }

        MAP.clear();
        MAP.putAll(map);

        if (plugin.getVanillaEnchantsYml().getBool("enabled")) {
            plugin.getProxy(EcoCraftEnchantmentManagerProxy.class).registerNewCraftEnchantments();
        }
    }
}
