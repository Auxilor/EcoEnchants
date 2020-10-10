package com.willfp.ecoenchants.enchantments.support.merging.anvil;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.util.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class AnvilMerge {

    /**
     * Merge items in anvil
     *
     * @param left The {@link ItemStack} on the left of the anvil
     * @param right The {@link ItemStack} in the middle of the anvil
     * @param old The previous {@link ItemStack} result
     * @param name The anvil display name
     * @return The result, stored as a {@link Pair} of {@link ItemStack} and {@link Integer}.
     */
    public static Pair<ItemStack, Integer> doMerge(ItemStack left, ItemStack right, ItemStack old, String name) {
        // Here so it can be accessed later (scope)

        int outDamage = -1;
        if(old != null) {
            if (old.getItemMeta() instanceof Damageable) {
                outDamage = ((Damageable) old.getItemMeta()).getDamage();
            }
        }

        if(left == null) return new Pair<>(null, null);

        if(left.getEnchantments().containsKey(EcoEnchants.PERMANENCE_CURSE)) return new Pair<>(null, null);

        if(!EnchantmentTarget.ALL.getMaterials().contains(left.getType()) || right == null || !EnchantmentTarget.ALL.getMaterials().contains(right.getType())) {
            ItemStack out = left.clone();
            ItemMeta outMeta = out.getItemMeta();
            outMeta.setDisplayName(name);

            if(left.getItemMeta().getDisplayName().equals(name)) {

                if(left.getItemMeta() instanceof Damageable) {
                    int leftDamage = ((Damageable) left.getItemMeta()).getDamage();

                    if(outDamage >= leftDamage || outDamage == -1) {
                        return new Pair<>(null, null);
                    } else {
                        ((Damageable) outMeta).setDamage(outDamage);
                    }
                } else {
                    return new Pair<>(null, null);
                }
                if(right == null) {
                    return new Pair<>(null, null);
                }
            }

            out.setItemMeta(outMeta);

            if(out.equals(left)) return new Pair<>(null, null);
            return new Pair<>(out, 0);
        }

        if(left.getItemMeta() instanceof Damageable && right.getItemMeta() instanceof EnchantmentStorageMeta) {
            outDamage = ((Damageable) left.getItemMeta()).getDamage();
        }

        if(!left.getType().equals(right.getType()) && !(right.getItemMeta() instanceof EnchantmentStorageMeta)) return new Pair<>(null, null);

        HashMap<Enchantment, Integer> leftEnchants = new HashMap<>();
        HashMap<Enchantment, Integer> rightEnchants = new HashMap<>();

        Map<Enchantment, Integer> outEnchants = new HashMap<>();

        if(left.getItemMeta() instanceof EnchantmentStorageMeta) {
            leftEnchants.putAll(((EnchantmentStorageMeta) left.getItemMeta()).getStoredEnchants());
        } else {
            leftEnchants.putAll(left.getItemMeta().getEnchants());
        }

        if(right.getItemMeta() instanceof EnchantmentStorageMeta) {
            rightEnchants.putAll(((EnchantmentStorageMeta) right.getItemMeta()).getStoredEnchants());
        } else {
            rightEnchants.putAll(right.getItemMeta().getEnchants());
        }

        leftEnchants.forEach(((enchantment, integer) -> {
            int level = integer;

            if(rightEnchants.containsKey(enchantment)) {
                int rightLevel = rightEnchants.get(enchantment);
                if(rightLevel > level) {
                    level = rightLevel;
                }
                else if(rightLevel == level) {
                    if(rightLevel > enchantment.getMaxLevel() && ConfigManager.getConfig().getBool("anvil.allow-combining-unsafe")) {
                        level++;
                    } else if((rightLevel + 1) <= enchantment.getMaxLevel() || ConfigManager.getConfig().getBool("anvil.allow-unsafe-levels")) {
                        level++;
                    }
                }
                rightEnchants.remove(enchantment);
            }

            outEnchants.put(enchantment, level);
        }));

        rightEnchants.forEach(((enchantment, integer) -> {
            AtomicBoolean doesConflict = new AtomicBoolean(false);

            if(EcoEnchants.getFromEnchantment(enchantment) != null && EcoEnchants.getFromEnchantment(enchantment).getType().equals(EcoEnchant.EnchantmentType.SPECIAL) && EcoEnchants.hasAnyOfType(left, EcoEnchant.EnchantmentType.SPECIAL)) doesConflict.set(true);
            if(EcoEnchants.getFromEnchantment(enchantment) != null && EcoEnchants.getFromEnchantment(enchantment).getType().equals(EcoEnchant.EnchantmentType.ARTIFACT) && EcoEnchants.hasAnyOfType(left, EcoEnchant.EnchantmentType.ARTIFACT)) doesConflict.set(true);

            leftEnchants.forEach(((enchantment1, integer1) -> {
                if(enchantment.conflictsWith(enchantment1)) doesConflict.set(true);
                if(enchantment1.conflictsWith(enchantment)) doesConflict.set(true);
            }));

            boolean canEnchantItem = enchantment.canEnchantItem(left);
            if(left.getItemMeta() instanceof EnchantmentStorageMeta) canEnchantItem = true;

            if(canEnchantItem && !doesConflict.get()) {
                if(ConfigManager.getConfig().getBool("anvil.hard-cap.enabled")) {
                    if(outEnchants.size() > ConfigManager.getConfig().getInt("anvil.hard-cap.cap")) {
                        return;
                    }
                }
                outEnchants.put(enchantment, integer);
            }
        }));

        // Test if the output is the same as left
        if(outEnchants.equals(leftEnchants) && left.getItemMeta().getDisplayName().equals(name)) {
            if(left.getItemMeta() instanceof Damageable) {
                int leftDamage = ((Damageable) left.getItemMeta()).getDamage();

                if(outDamage == leftDamage) {
                    return new Pair<>(null, null);
                }
            }
        }

        ItemStack output = left.clone();

        if(output.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) output.getItemMeta();
            meta.getStoredEnchants().forEach(((enchantment, integer) -> {
                meta.removeStoredEnchant(enchantment);
            }));

            outEnchants.forEach(((enchantment, integer) -> {
                meta.addStoredEnchant(enchantment, integer, ConfigManager.getConfig().getBool("anvil.allow-existing-unsafe-levels") || ConfigManager.getConfig().getBool("anvil.allow-unsafe-levels"));
            }));

            meta.setDisplayName(name);

            output.setItemMeta(meta);
        } else {
            ItemMeta meta = output.getItemMeta();
            meta.getEnchants().forEach(((enchantment, integer) -> {
                meta.removeEnchant(enchantment);
            }));

            outEnchants.forEach(((enchantment, integer) -> {
                meta.addEnchant(enchantment, integer, ConfigManager.getConfig().getBool("anvil.allow-existing-unsafe-levels") || ConfigManager.getConfig().getBool("anvil.allow-unsafe-levels"));
            }));

            if(output.getItemMeta() instanceof Damageable) {
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
            outEnchantLevels.addAndGet(integer);
        }));
        leftEnchants.forEach((((enchantment, integer) -> {
            outEnchantLevels.addAndGet(integer);
        })));

        totalEnchantLevelDelta = Math.abs(outEnchantLevels.intValue() - inEnchantLevels.intValue());

        if(output.equals(left)) return new Pair<>(null, null);

        if(ConfigManager.getConfig().getBool("anvil.cost-exponent.enabled")) {
            double exponent = ConfigManager.getConfig().getDouble("anvil.cost-exponent.exponent");
            int prevDelta = totalEnchantLevelDelta;

            double costMultiplier = Math.pow(exponent, totalEnchantLevelDelta);
            double modifiedCost = Math.ceil((double) totalEnchantLevelDelta * costMultiplier);
            totalEnchantLevelDelta = (int) modifiedCost;

            if(prevDelta > 0 && totalEnchantLevelDelta == 0) {
                totalEnchantLevelDelta = prevDelta;
            }
        }

        return new Pair<>(output, totalEnchantLevelDelta);
    }
}
