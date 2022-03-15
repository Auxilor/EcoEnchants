package com.willfp.ecoenchants.enchantments.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.integrations.placeholder.PlaceholderManager;
import com.willfp.eco.core.placeholder.PlayerPlaceholder;
import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.StringUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@UtilityClass
@SuppressWarnings({"unchecked", "deprecation"})
public class EnchantmentUtils {
    /**
     * If the enchantment has successfully passed its specified chance.
     *
     * @param enchantment The enchantment to query.
     * @param level       The level to base the chance off of.
     * @return If the enchantment should then be executed.
     */
    public static boolean passedChance(@NotNull final EcoEnchant enchantment,
                                       final int level) {
        return NumberUtils.randFloat(0, 1) < ((enchantment.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level") * level) / 100);
    }

    /**
     * If the enchantment has successfully passed its specified chance.
     *
     * @param enchantment The enchantment to query.
     * @param level       The level to base the chance off of.
     * @return If the enchantment should then be executed.
     */
    public static String chancePlaceholder(@NotNull final EcoEnchant enchantment,
                                           final int level) {
        return NumberUtils.format(enchantment.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level") * level);
    }

    /**
     * If attack was fully charged if required.
     *
     * @param enchantment The enchantment.
     * @param entity      The attacker.
     * @return If was fully charged.
     */
    public static boolean isFullyChargeIfRequired(@NotNull final EcoEnchant enchantment,
                                                  @NotNull final LivingEntity entity) {
        if (entity instanceof Player) {
            if (((Player) entity).getAttackCooldown() != 1.0f) {
                return enchantment.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged");
            }
        }

        return true;
    }

    /**
     * Register the placeholders for an enchantment.
     *
     * @param enchantment The enchantment to register placeholders for.
     */
    public static void registerPlaceholders(@NotNull final EcoEnchant enchantment) {
        PlaceholderManager.registerPlaceholder(
                new PlayerPlaceholder(
                        enchantment.getPlugin(),
                        enchantment.getPermissionName() + "_" + "enabled",
                        player -> String.valueOf(enchantment.isEnabled())
                )
        );

        enchantment.getConfig().getKeys(true).forEach(string -> {
            String key = string.replace(".", "_").replace("-", "_");
            Object object = enchantment.getConfig().get(string);

            PlaceholderManager.registerPlaceholder(
                    new PlayerPlaceholder(
                            enchantment.getPlugin(),
                            enchantment.getPermissionName() + "_" + key,
                            player -> StringUtils.toNiceString(object)
                    )
            );
        });
    }

    /**
     * Register enchantment with the server.
     *
     * @param enchantment The enchantment.
     */
    public static void register(@NotNull final Enchantment enchantment) {
        try {
            Field byIdField = Enchantment.class.getDeclaredField("byKey");
            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byIdField.setAccessible(true);
            byNameField.setAccessible(true);
            Map<NamespacedKey, Enchantment> byKey = (Map<NamespacedKey, Enchantment>) byIdField.get(null);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
            byKey.remove(enchantment.getKey());
            byName.remove(enchantment.getName());

            if (enchantment instanceof EcoEnchant) {
                byName.remove(((EcoEnchant) enchantment).getDisplayName());
            }

            Map<String, Enchantment> byNameClone = new HashMap<>(byName);
            for (Map.Entry<String, Enchantment> entry : byNameClone.entrySet()) {
                if (entry.getValue().getKey().equals(enchantment.getKey())) {
                    byName.remove(entry.getKey());
                }
            }

            if (enchantment instanceof EcoEnchant) {
                if (EcoEnchantsPlugin.getInstance().getConfigYml().getBool("advanced.dual-registration.enabled")) {
                    byName.put(((EcoEnchant) enchantment).getDisplayName(), enchantment);
                }
            }

            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            f.setAccessible(false);

            Enchantment.registerEnchantment(enchantment);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
    }

    /**
     * Unregister enchantment with the server.
     *
     * @param enchantment The enchantment.
     */
    public static void unregister(@NotNull final Enchantment enchantment) {
        try {
            Field byIdField = Enchantment.class.getDeclaredField("byKey");
            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byIdField.setAccessible(true);
            byNameField.setAccessible(true);
            Map<NamespacedKey, Enchantment> byKey = (Map<NamespacedKey, Enchantment>) byIdField.get(null);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
            byKey.remove(enchantment.getKey());
            byName.remove(enchantment.getName());

            if (enchantment instanceof EcoEnchant) {
                byName.remove(((EcoEnchant) enchantment).getDisplayName());
            }

            Map<String, Enchantment> byNameClone = new HashMap<>(byName);
            for (Map.Entry<String, Enchantment> entry : byNameClone.entrySet()) {
                if (entry.getValue().getKey().equals(enchantment.getKey())) {
                    byName.remove(entry.getKey());
                }
            }

            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            f.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
    }

    /**
     * Old method still used for backwards compatibility.
     *
     * @param player  The player.
     * @param toBreak The blocks to break.
     * @param plugin  Instance of EcoEnchants, required for meta.
     */
    public static void rehandleBreaking(@NotNull final Player player,
                                        @NotNull final Set<Block> toBreak,
                                        @NotNull final EcoPlugin plugin) {
        for (Block block : toBreak) {
            block.setMetadata("block-ignore", plugin.getMetadataValueFactory().create(true));
            player.breakBlock(block);
            block.removeMetadata("block-ignore", plugin);
        }
    }
}
