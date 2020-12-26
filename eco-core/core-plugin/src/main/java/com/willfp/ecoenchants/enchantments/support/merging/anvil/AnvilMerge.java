package com.willfp.ecoenchants.enchantments.support.merging.anvil;

import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.tuplets.Pair;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import lombok.experimental.UtilityClass;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class AnvilMerge {
    private static final String ALLOW_UNSAFE_KEY = "anvil.allow-unsafe-levels";

    /**
     * Merge items in anvil
     *
     * @param left   The {@link ItemStack} on the left of the anvil
     * @param right  The {@link ItemStack} in the middle of the anvil
     * @param old    The previous {@link ItemStack} result
     * @param name   The anvil display name
     * @param player The player merging (for permissions)
     *
     * @return The result, stored as a {@link Pair} of {@link ItemStack} and {@link Integer}.
     */
    public Pair<ItemStack, Integer> doMerge(@Nullable final ItemStack left,
                                            @Nullable final ItemStack right,
                                            @Nullable final ItemStack old,
                                            @NotNull final String name,
                                            @NotNull final Player player) {
        // Here so it can be accessed later (scope)

        int outDamage = -1;
        if (old != null && old.getItemMeta() instanceof Damageable) {
            outDamage = ((Damageable) old.getItemMeta()).getDamage();
        }

        if (left == null) {
            return new Pair<>(null, null);
        }

        if (left.getEnchantments().containsKey(EcoEnchants.PERMANENCE_CURSE)) {
            return new Pair<>(null, null);
        }

        if (!EnchantmentTarget.ALL.getMaterials().contains(left.getType()) || right == null || !EnchantmentTarget.ALL.getMaterials().contains(right.getType())) {
            ItemStack out = left.clone();
            ItemMeta outMeta = out.getItemMeta();
            assert outMeta != null;
            ItemMeta meta = left.getItemMeta();
            assert meta != null;

            outMeta.setDisplayName(name);

            if (meta.getDisplayName().equals(name)) {

                if (meta instanceof Damageable) {
                    int leftDamage = ((Damageable) meta).getDamage();

                    if (outDamage >= leftDamage || outDamage == -1) {
                        return new Pair<>(null, null);
                    } else {
                        ((Damageable) outMeta).setDamage(outDamage);
                    }
                } else {
                    return new Pair<>(null, null);
                }
                if (right == null) {
                    return new Pair<>(null, null);
                }
            }

            out.setItemMeta(outMeta);

            if (out.equals(left)) {
                return new Pair<>(null, null);
            }
            return new Pair<>(out, 0);
        }

        if (left.getItemMeta() instanceof Damageable && right.getItemMeta() instanceof EnchantmentStorageMeta) {
            outDamage = ((Damageable) left.getItemMeta()).getDamage();
        }

        if (!left.getType().equals(right.getType()) && !(right.getItemMeta() instanceof EnchantmentStorageMeta)) {
            return new Pair<>(null, null);
        }

        HashMap<Enchantment, Integer> leftEnchants = new HashMap<>();
        HashMap<Enchantment, Integer> rightEnchants = new HashMap<>();

        Map<Enchantment, Integer> outEnchants = new HashMap<>();

        if (left.getItemMeta() instanceof EnchantmentStorageMeta) {
            leftEnchants.putAll(((EnchantmentStorageMeta) left.getItemMeta()).getStoredEnchants());
        } else {
            leftEnchants.putAll(left.getItemMeta().getEnchants());
        }

        if (right.getItemMeta() instanceof EnchantmentStorageMeta) {
            rightEnchants.putAll(((EnchantmentStorageMeta) right.getItemMeta()).getStoredEnchants());
        } else {
            rightEnchants.putAll(right.getItemMeta().getEnchants());
        }

        leftEnchants.forEach(((enchantment, integer) -> {
            int level = integer;

            if (rightEnchants.containsKey(enchantment)) {
                int rightLevel = rightEnchants.get(enchantment);
                if (rightLevel > level) {
                    level = rightLevel;
                } else if (rightLevel == level
                        && (rightLevel > enchantment.getMaxLevel() && Configs.CONFIG.getBool("anvil.allow-combining-unsafe"))
                        || ((rightLevel + 1) <= enchantment.getMaxLevel()
                        || Configs.CONFIG.getBool(ALLOW_UNSAFE_KEY))) {
                        level++;
                }
                rightEnchants.remove(enchantment);
            }

            outEnchants.put(enchantment, level);
        }));

        rightEnchants.forEach(((enchantment, integer) -> {
            AtomicBoolean doesConflict = new AtomicBoolean(false);

            EnchantmentType.values().forEach(enchantmentType -> {
                EcoEnchant enchant = EcoEnchants.getFromEnchantment(enchantment);
                if (enchant == null) {
                    return;
                }
                if (enchant.getType().equals(enchantmentType) && EcoEnchants.hasAnyOfType(left, enchantmentType) && enchantmentType.isSingular()) {
                    doesConflict.set(true);
                }
            });

            leftEnchants.forEach(((enchantment1, integer1) -> {
                if (enchantment.conflictsWith(enchantment1)) {
                    doesConflict.set(true);
                }
                if (enchantment1.conflictsWith(enchantment)) {
                    doesConflict.set(true);
                }
            }));

            boolean canEnchantItem = enchantment.canEnchantItem(left);
            if (left.getItemMeta() instanceof EnchantmentStorageMeta) {
                canEnchantItem = true;
            }

            if (canEnchantItem && !doesConflict.get()) {
                if (Configs.CONFIG.getBool("anvil.hard-cap.enabled") && !player.hasPermission("ecoenchants.anvil.bypasshardcap") && outEnchants.size() >= Configs.CONFIG.getInt("anvil.hard-cap.cap")) {
                    return;
                }
                outEnchants.put(enchantment, integer);
            }
        }));

        // Test if the output is the same as left
        if (outEnchants.equals(leftEnchants) && left.getItemMeta().getDisplayName().equals(name) && left.getItemMeta() instanceof Damageable) {
            int leftDamage = ((Damageable) left.getItemMeta()).getDamage();

            if (outDamage == leftDamage) {
                return new Pair<>(null, null);
            }
        }

        ItemStack output = left.clone();

        if (output.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) output.getItemMeta();
            meta.getStoredEnchants().forEach(((enchantment, integer) -> {
                meta.removeStoredEnchant(enchantment);
            }));

            outEnchants.forEach(((enchantment, integer) -> {
                meta.addStoredEnchant(enchantment, integer, Configs.CONFIG.getBool("anvil.allow-existing-unsafe-levels") || Configs.CONFIG.getBool(ALLOW_UNSAFE_KEY));
            }));

            meta.setDisplayName(name);

            output.setItemMeta(meta);
        } else {
            ItemMeta meta = output.getItemMeta();
            meta.getEnchants().forEach(((enchantment, integer) -> {
                meta.removeEnchant(enchantment);
            }));

            outEnchants.forEach(((enchantment, integer) -> {
                meta.addEnchant(enchantment, integer, Configs.CONFIG.getBool("anvil.allow-existing-unsafe-levels") || Configs.CONFIG.getBool(ALLOW_UNSAFE_KEY));
            }));

            if (output.getItemMeta() instanceof Damageable) {
                ((Damageable) meta).setDamage(outDamage);
            }

            meta.setDisplayName(name);

            output.setItemMeta(meta);
        }


        // Calculate difference in enchant levels
        int totalEnchantLevelDelta;
        AtomicInteger outEnchantLevels = new AtomicInteger();
        AtomicInteger inEnchantLevels = new AtomicInteger();

        outEnchants.forEach(((enchantment, integer) -> {
            if (EcoEnchants.getFromEnchantment(enchantment) != null) {
                outEnchantLevels.addAndGet(integer);
            }
        }));
        leftEnchants.forEach(((enchantment, integer) -> {
            if (EcoEnchants.getFromEnchantment(enchantment) != null) {
                outEnchantLevels.addAndGet(integer);
            }
        }));

        totalEnchantLevelDelta = Math.abs(outEnchantLevels.intValue() - inEnchantLevels.intValue());

        if (output.equals(left)) {
            return new Pair<>(null, null);
        }

        if (Configs.CONFIG.getBool("anvil.cost-exponent.enabled")) {
            double exponent = Configs.CONFIG.getDouble("anvil.cost-exponent.exponent");
            int prevDelta = totalEnchantLevelDelta;

            double costMultiplier = Math.pow(exponent, totalEnchantLevelDelta);
            double modifiedCost = Math.ceil((double) totalEnchantLevelDelta * costMultiplier);
            totalEnchantLevelDelta = (int) modifiedCost;

            if (prevDelta > 0 && totalEnchantLevelDelta == 0) {
                totalEnchantLevelDelta = prevDelta;
            }
        }

        return new Pair<>(output, totalEnchantLevelDelta);
    }
}
