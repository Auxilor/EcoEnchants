package com.willfp.ecoenchants.enchantments.util;


import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.eco.util.ArrowUtils;
import com.willfp.eco.util.DurabilityUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class EnchantChecks {
    /**
     * Does the specified ItemStack have a certain Enchantment present?
     *
     * @param item        The {@link ItemStack} to check
     * @param enchantment The enchantment to query
     * @return If the item has the queried enchantment
     */
    public static boolean item(@Nullable final ItemStack item,
                               @NotNull final Enchantment enchantment) {
        return getItemLevel(item, enchantment) != 0;
    }

    /**
     * What level of the specified enchantment does the specified ItemStack have?
     *
     * @param item        The {@link ItemStack} to check
     * @param enchantment The enchantment to query
     * @return The level of the enchantment, or 0 if not found
     */
    public static int getItemLevel(@Nullable final ItemStack item,
                                   @NotNull final Enchantment enchantment) {
        if (item == null) {
            return 0;
        }
        if (item.getType().equals(Material.AIR)) {
            return 0;
        }

        return FastItemStack.wrap(item).getEnchantmentLevel(enchantment, false);
    }

    /**
     * Get all {@link EcoEnchant}s on a specified ItemStack.
     *
     * @param item The ItemStack to query.
     * @return A {@link HashMap} of all EcoEnchants, where the key represents the level.
     */
    public static Map<EcoEnchant, Integer> getEnchantsOnItem(@Nullable final ItemStack item) {
        if (item == null) {
            return new HashMap<>();
        }
        if (item.getType().equals(Material.AIR)) {
            return new HashMap<>();
        }

        Map<EcoEnchant, Integer> ecoEnchants = new HashMap<>();
        for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : FastItemStack.wrap(item).getEnchants(false).entrySet()) {
            if (enchantmentIntegerEntry.getKey() instanceof EcoEnchant enchant) {
                ecoEnchants.put(enchant, enchantmentIntegerEntry.getValue());
            }
        }

        return ecoEnchants;
    }

    /**
     * Does the specified Arrow have a certain Enchantment present?
     * <p>
     * EcoEnchants automatically gives an arrow NBT data consisting of the enchantments present to avoid switching errors.
     *
     * @param arrow       The {@link Arrow} to check.
     * @param enchantment The enchantment to query.
     * @return If the arrow has the queried enchantment.
     */
    public static boolean arrow(@NotNull final Arrow arrow,
                                @NotNull final Enchantment enchantment) {
        return getArrowLevel(arrow, enchantment) != 0;
    }

    /**
     * What level specified Arrow has of a certain Enchantment present?
     * <p>
     * EcoEnchants automatically gives an arrow NBT data consisting of the enchantments present to avoid switching errors.
     *
     * @param arrow       The {@link Arrow} to check.
     * @param enchantment The enchantment to query.
     * @return The level found on the arrow, or 0 if not found.
     */
    public static int getArrowLevel(@NotNull final Arrow arrow,
                                    @NotNull final Enchantment enchantment) {
        ItemStack bow = ArrowUtils.getBow(arrow);

        if (bow == null) {
            return 0;
        }

        return getItemLevel(bow, enchantment);
    }

    /**
     * Get all {@link EcoEnchant}s on a specified Arrow.
     *
     * @param arrow The Arrow to query.
     * @return A {@link HashMap} of all EcoEnchants, where the key represents the level.
     */
    public static Map<EcoEnchant, Integer> getEnchantsOnArrow(@NotNull final Arrow arrow) {
        ItemStack bow = ArrowUtils.getBow(arrow);

        if (bow == null) {
            return new HashMap<>();
        }

        return getEnchantsOnItem(bow);
    }

    /**
     * Does the specified {@link LivingEntity} have a certain Enchantment present on the item in their main hand?
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return If the LivingEntity has the enchantment.
     */
    public static boolean mainhand(@NotNull final LivingEntity entity,
                                   @NotNull final Enchantment enchantment) {
        return getMainhandLevel(entity, enchantment) != 0;
    }

    /**
     * What level of the specified enchantment does the queried {@link LivingEntity} have on their main hand item?
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return The level found on the mainhand item, or 0 if not found.
     */
    public static int getMainhandLevel(@NotNull final LivingEntity entity,
                                       @NotNull final Enchantment enchantment) {
        if (entity.getEquipment() == null) {
            return 0;
        }

        ItemStack item = entity.getEquipment().getItemInMainHand();

        return getItemLevel(item, enchantment);
    }

    /**
     * Get all {@link EcoEnchant}s on a queried {@link LivingEntity}s main hand item.
     *
     * @param entity The entity to query.
     * @return A {@link HashMap} of all EcoEnchants, where the key represents the level.
     */
    public static Map<EcoEnchant, Integer> getEnchantsOnMainhand(@NotNull final LivingEntity entity) {
        if (entity.getEquipment() == null) {
            return new HashMap<>();
        }

        ItemStack item = entity.getEquipment().getItemInMainHand();

        return getEnchantsOnItem(item);
    }

    /**
     * Does the specified {@link LivingEntity} have a certain Enchantment present on the item in their offhand?
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return If the LivingEntity has the enchantment.
     */
    public static boolean offhand(@NotNull final LivingEntity entity,
                                  @NotNull final Enchantment enchantment) {
        return getOffhandLevel(entity, enchantment) != 0;
    }

    /**
     * What level of the specified enchantment does the queried {@link LivingEntity} have on their offhand item?
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return The level found on the offhand item, or 0 if not found.
     */
    public static int getOffhandLevel(@NotNull final LivingEntity entity,
                                      @NotNull final Enchantment enchantment) {
        if (entity.getEquipment() == null) {
            return 0;
        }

        ItemStack item = entity.getEquipment().getItemInOffHand();

        return getItemLevel(item, enchantment);
    }

    /**
     * Get all {@link EcoEnchant}s on a queried {@link LivingEntity}s offhand item.
     *
     * @param entity The entity to query.
     * @return A {@link HashMap} of all EcoEnchants, where the key represents the level.
     */
    public static Map<EcoEnchant, Integer> getEnchantsOnOffhand(@NotNull final LivingEntity entity) {
        if (entity.getEquipment() == null) {
            return new HashMap<>();
        }

        ItemStack item = entity.getEquipment().getItemInOffHand();

        return getEnchantsOnItem(item);
    }

    /**
     * Get a cumulative total of all levels on a {@link LivingEntity}s armor of a certain enchantment.
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return The cumulative total of all levels, ie 4 pieces all with level 3 returns 12
     */
    public static int getArmorPoints(@NotNull final LivingEntity entity,
                                     @NotNull final Enchantment enchantment) {
        return getArmorPoints(entity, enchantment, 0);
    }

    /**
     * Get a cumulative total of all levels on a {@link LivingEntity}s armor of a certain enchantment.
     * <p>
     * Then, apply a specified amount of damage to all items with said enchantment.
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @param damage      The amount of damage to deal to all armor pieces.
     * @return The cumulative total of all levels, ie 4 pieces all with level 3 returns 12.
     */
    public static int getArmorPoints(@NotNull final LivingEntity entity,
                                     @NotNull final Enchantment enchantment,
                                     final int damage) {
        if (entity.getEquipment() == null) {
            return 0;
        }

        boolean isPlayer = entity instanceof Player;

        AtomicInteger armorPoints = new AtomicInteger(0);
        List<ItemStack> armor = Arrays.asList(entity.getEquipment().getArmorContents());
        armor.forEach((itemStack -> {
            int level = getItemLevel(itemStack, enchantment);
            if (level != 0) {
                armorPoints.addAndGet(getItemLevel(itemStack, enchantment));
                if (damage > 0 && isPlayer) {
                    Player player = (Player) entity;
                    DurabilityUtils.damageItem(player, itemStack, level);
                }
            }
        }));

        return armorPoints.get();
    }

    /**
     * Get all {@link EcoEnchant}s on a queried {@link LivingEntity}s armor.
     *
     * @param entity The entity to query.
     * @return A {@link HashMap} of all EcoEnchants, where the key represents the cumulative total levels.
     */
    public static Map<EcoEnchant, Integer> getEnchantsOnArmor(@NotNull final LivingEntity entity) {
        if (entity.getEquipment() == null) {
            return new HashMap<>();
        }

        Map<EcoEnchant, Integer> ecoEnchants = new HashMap<>();

        for (ItemStack itemStack : entity.getEquipment().getArmorContents()) {
            ecoEnchants.putAll(EnchantChecks.getEnchantsOnItem(itemStack));
        }

        return ecoEnchants;
    }

    /**
     * Does the specified {@link LivingEntity} have a certain Enchantment present on their helmet?
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return If the LivingEntity has the enchantment.
     */
    public static boolean helmet(@NotNull final LivingEntity entity,
                                 @NotNull final Enchantment enchantment) {
        return getHelmetLevel(entity, enchantment) != 0;
    }

    /**
     * What level of the specified enchantment does the queried {@link LivingEntity} have on their helmet?
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return The level found, or 0 if not found.
     */
    public static int getHelmetLevel(@NotNull final LivingEntity entity,
                                     @NotNull final Enchantment enchantment) {
        if (entity.getEquipment() == null) {
            return 0;
        }

        ItemStack item = entity.getEquipment().getHelmet();

        return getItemLevel(item, enchantment);
    }

    /**
     * Does the specified {@link LivingEntity} have a certain Enchantment present on their chestplate?
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return If the LivingEntity has the enchantment.
     */
    public static boolean chestplate(@NotNull final LivingEntity entity,
                                     @NotNull final Enchantment enchantment) {
        return getChestplateLevel(entity, enchantment) != 0;
    }

    /**
     * What level of the specified enchantment does the queried {@link LivingEntity} have on their chestplate?
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return The level found, or 0 if not found.
     */
    public static int getChestplateLevel(@NotNull final LivingEntity entity,
                                         @NotNull final Enchantment enchantment) {
        if (entity.getEquipment() == null) {
            return 0;
        }

        ItemStack item = entity.getEquipment().getChestplate();

        return getItemLevel(item, enchantment);
    }

    /**
     * Does the specified {@link LivingEntity} have a certain Enchantment present on their leggings?
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return If the LivingEntity has the enchantment.
     */
    public static boolean leggings(@NotNull final LivingEntity entity,
                                   @NotNull final Enchantment enchantment) {
        return getLeggingsLevel(entity, enchantment) != 0;
    }

    /**
     * What level of the specified enchantment does the queried {@link LivingEntity} have on their leggings?
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return The level found, or 0 if not found.
     */
    public static int getLeggingsLevel(@NotNull final LivingEntity entity,
                                       @NotNull final Enchantment enchantment) {
        if (entity.getEquipment() == null) {
            return 0;
        }

        ItemStack item = entity.getEquipment().getLeggings();

        return getItemLevel(item, enchantment);
    }

    /**
     * Does the specified {@link LivingEntity} have a certain Enchantment present on their boots?
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return If the LivingEntity has the enchantment.
     */
    public static boolean boots(@NotNull final LivingEntity entity,
                                @NotNull final Enchantment enchantment) {
        return getBootsLevel(entity, enchantment) != 0;
    }

    /**
     * What level of the specified enchantment does the queried {@link LivingEntity} have on their boots?
     *
     * @param entity      The entity to query.
     * @param enchantment The enchantment to check.
     * @return The level found, or 0 if not found.
     */
    public static int getBootsLevel(@NotNull final LivingEntity entity,
                                    @NotNull final Enchantment enchantment) {
        if (entity.getEquipment() == null) {
            return 0;
        }

        ItemStack item = entity.getEquipment().getBoots();

        return getItemLevel(item, enchantment);
    }
}
