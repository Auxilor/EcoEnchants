package com.willfp.ecoenchants.enchantments.custom;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CustomEnchantLookup {
    /**
     * All registered providers.
     */
    private static final Set<Function<Player, Map<ItemStack, EnchantmentTarget.Slot>>> PROVIDERS = new HashSet<>();

    /**
     * Cached items.
     */
    private static final Cache<Player, Map<ItemStack, EnchantmentTarget.Slot>> ITEM_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build();

    /**
     * Cached enchant levels.
     */
    private static final Cache<Player, Collection<CustomEcoEnchantLevel>> ENCHANT_LEVELS_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build();

    /**
     * Instance of EcoEnchants.
     */
    private static final EcoPlugin PLUGIN = EcoEnchantsPlugin.getInstance();

    /**
     * Register provider.
     *
     * @param provider The provider.
     */
    public static void registerProvider(@NotNull final Function<Player, Map<ItemStack, EnchantmentTarget.Slot>> provider) {
        PROVIDERS.add(provider);
    }

    /**
     * Provide ItemStacks.
     *
     * @param p The player.
     * @return The ItemStacks.
     */
    public static Map<ItemStack, EnchantmentTarget.Slot> provide(@NotNull final Player p) {
        return ITEM_CACHE.get(p, player -> {
            Map<ItemStack, EnchantmentTarget.Slot> found = new HashMap<>();
            for (Function<Player, Map<ItemStack, EnchantmentTarget.Slot>> provider : PROVIDERS) {
                found.putAll(provider.apply(player));
            }
            found.keySet().removeIf(Objects::isNull);
            return found;
        });
    }

    /**
     * Provide levels.
     *
     * @param p The player.
     * @return The levels.
     */
    public static List<CustomEcoEnchantLevel> provideLevels(@NotNull final Player p) {
        return new ArrayList<>(ENCHANT_LEVELS_CACHE.get(p, player -> {
            List<CustomEcoEnchantLevel> found = new ArrayList<>();

            for (Map.Entry<ItemStack, EnchantmentTarget.Slot> entry : provide(player).entrySet()) {
                ItemStack itemStack = entry.getKey();
                EnchantmentTarget.Slot slot = entry.getValue();
                if (itemStack == null) {
                    continue;
                }

                Map<EcoEnchant, Integer> enchants = EnchantChecks.getEnchantsOnItem(itemStack);

                if (enchants.isEmpty()) {
                    continue;
                }

                for (Map.Entry<EcoEnchant, Integer> enchantEntry : enchants.entrySet()) {
                    if (!(enchantEntry.getKey() instanceof CustomEcoEnchant enchant)) {
                        continue;
                    }

                    if (slot != EnchantmentTarget.Slot.ANY) {
                        if (!enchant.getTargets().stream()
                                .map(EnchantmentTarget::getSlot).toList()
                                .contains(slot)) {
                            continue;
                        }
                    }

                    found.add(enchant.getLevel(enchantEntry.getValue()));
                }
            }

            return found;
        }));
    }

    /**
     * Clear cache.
     *
     * @param player The player.
     */
    public static void clearCache(@NotNull final Player player) {
        ITEM_CACHE.invalidate(player);
        ENCHANT_LEVELS_CACHE.invalidate(player);
    }

    static {
        registerProvider(player -> Map.of(
                player.getInventory().getItemInMainHand(),
                EnchantmentTarget.Slot.HANDS
        ));
        registerProvider(player -> Map.of(
                player.getInventory().getItemInOffHand(),
                EnchantmentTarget.Slot.HANDS
        ));
        registerProvider(player -> {
            Map<ItemStack, EnchantmentTarget.Slot> items = new HashMap<>();
            for (ItemStack stack : player.getInventory().getArmorContents()) {
                items.put(stack, EnchantmentTarget.Slot.ARMOR);
            }
            return items;
        });
    }
}
