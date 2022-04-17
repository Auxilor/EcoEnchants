package com.willfp.ecoenchants.enchantments.support.merging.anvil;

import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.eco.util.StringUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
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

@SuppressWarnings("deprecation")
@UtilityClass
public class AnvilMerge {
    /**
     * Instance of EcoEnchants.
     */
    private static final EcoEnchantsPlugin PLUGIN = EcoEnchantsPlugin.getInstance();

    /**
     * Config key for allowing unsafe levels.
     */
    private static final String ALLOW_UNSAFE_KEY = "anvil.allow-unsafe-levels";

    /**
     * Merge items in anvil.
     *
     * @param left     The {@link ItemStack} on the left of the anvil.
     * @param right    The {@link ItemStack} in the middle of the anvil.
     * @param old      The previous {@link ItemStack} result.
     * @param itemName The anvil display name.
     * @param player   The player merging (for permissions).
     * @return The result.
     */
    public AnvilResult doMerge(@Nullable final ItemStack left,
                               @Nullable final ItemStack right,
                               @Nullable final ItemStack old,
                               @NotNull final String itemName,
                               @NotNull final Player player) {
        /*
        If you're currently looking at this code,
        pray to whatever god you have that any changes
        don't cause things to break.

        I have no idea how this code works, it does - and it scares me.
        I'll just pretend that I understand it and never look at it again.
         */

        // Copied to non-final string.
        String name = itemName;

        int outDamage = -1;
        if (old != null && old.getItemMeta() instanceof Damageable) {
            outDamage = ((Damageable) old.getItemMeta()).getDamage();
        }

        if (left == null) {
            return AnvilResult.FAIL;
        }

        if (left.getEnchantments().containsKey(EcoEnchants.PERMANENCE_CURSE)) {
            return AnvilResult.FAIL;
        }

        name = name.replace("§", "&");

        if (player.hasPermission("ecoenchants.anvil.color")) {
            name = StringUtils.format(name);
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
                        return AnvilResult.FAIL;
                    } else {
                        ((Damageable) outMeta).setDamage(outDamage);
                    }
                } else {
                    return AnvilResult.FAIL;
                }
                if (right == null) {
                    return AnvilResult.FAIL;
                }
            }

            out.setItemMeta(outMeta);

            if (out.equals(left)) {
                return AnvilResult.FAIL;
            }
            return new AnvilResult(out, 0);
        }

        if (left.getItemMeta() instanceof Damageable && right.getItemMeta() instanceof EnchantmentStorageMeta) {
            outDamage = ((Damageable) left.getItemMeta()).getDamage();
        }

        if (!left.getType().equals(right.getType()) && !(right.getItemMeta() instanceof EnchantmentStorageMeta)) {
            return AnvilResult.FAIL;
        }

        if (left.getAmount() != right.getAmount()) {
            return AnvilResult.FAIL;
        }

        Map<Enchantment, Integer> outEnchants = new HashMap<>();

        HashMap<Enchantment, Integer> leftEnchants = new HashMap<>(FastItemStack.wrap(left).getEnchants(true));
        HashMap<Enchantment, Integer> rightEnchants = new HashMap<>(FastItemStack.wrap(right).getEnchants(true));

        leftEnchants.forEach(((enchantment, integer) -> {
            int level = integer;

            if (rightEnchants.containsKey(enchantment)) {
                int rightLevel = rightEnchants.get(enchantment);
                if (rightLevel > level) {
                    level = rightLevel;
                } else if (rightLevel == level
                        && ((rightLevel > enchantment.getMaxLevel() && PLUGIN.getConfigYml().getBool("anvil.allow-combining-unsafe"))
                        || ((rightLevel + 1) <= enchantment.getMaxLevel() || PLUGIN.getConfigYml().getBool(ALLOW_UNSAFE_KEY)))) {
                    level++;
                }
                rightEnchants.remove(enchantment);
            }

            if (PLUGIN.getConfigYml().getBool("anvil.hard-cap.enabled")) {
                if (!player.hasPermission("ecoenchants.anvil.bypasshardcap")) {
                    if (outEnchants.keySet()
                            .stream()
                            .filter(enchant -> {
                                if (enchant instanceof EcoEnchant) {
                                    return !((EcoEnchant) enchant).hasFlag("hard-cap-ignore");
                                }

                                return true;
                            }).count() >= PLUGIN.getConfigYml().getInt("anvil.hard-cap.cap")) {
                        return;
                    }
                }
            }

            outEnchants.put(enchantment, level);
        }));

        rightEnchants.forEach(((enchantment, integer) -> {
            AtomicBoolean doesConflict = new AtomicBoolean(false);

            EnchantmentType.values().forEach(enchantmentType -> {
                if (!(enchantment instanceof EcoEnchant enchant)) {
                    return;
                }
                if (enchant.getType().equals(enchantmentType) && EcoEnchants.hasAnyOfType(left, enchantmentType) && enchantmentType.isSingular()) {
                    doesConflict.set(true);
                }
            });

            leftEnchants.forEach((enchantment1, integer1) -> {
                if (enchantment.conflictsWith(enchantment1)) {
                    doesConflict.set(true);
                }
                if (enchantment1.conflictsWith(enchantment)) {
                    doesConflict.set(true);
                }
            });

            boolean canEnchantItem = enchantment.canEnchantItem(left);
            if (left.getItemMeta() instanceof EnchantmentStorageMeta) {
                canEnchantItem = true;
            }

            if (PLUGIN.getConfigYml().getBool("anvil.hard-cap.enabled")) {
                if (!player.hasPermission("ecoenchants.anvil.bypasshardcap")) {
                    if (outEnchants.keySet()
                            .stream()
                            .filter(enchant -> {
                                if (enchant instanceof EcoEnchant) {
                                    return !((EcoEnchant) enchant).hasFlag("hard-cap-ignore");
                                }

                                return true;
                            }).count() >= PLUGIN.getConfigYml().getInt("anvil.hard-cap.cap")) {
                        doesConflict.set(true);
                    }
                }
            }

            if (canEnchantItem && !doesConflict.get()) {
                outEnchants.put(enchantment, integer);
            }
        }));

        // Test if the output is the same as left
        if (outEnchants.equals(leftEnchants) && left.getItemMeta().getDisplayName().equals(name) && left.getItemMeta() instanceof Damageable) {
            int leftDamage = ((Damageable) left.getItemMeta()).getDamage();

            if (outDamage == leftDamage) {
                return AnvilResult.FAIL;
            }
        }

        ItemStack output = left.clone();

        if (output.getItemMeta() instanceof EnchantmentStorageMeta meta) {
            meta.getStoredEnchants().forEach(((enchantment, integer) -> {
                meta.removeStoredEnchant(enchantment);
            }));

            outEnchants.forEach(((enchantment, integer) -> {
                meta.addStoredEnchant(enchantment, integer, PLUGIN.getConfigYml().getBool("anvil.allow-existing-unsafe-levels") || PLUGIN.getConfigYml().getBool(ALLOW_UNSAFE_KEY));
            }));

            meta.setDisplayName(name);

            output.setItemMeta(meta);
        } else {
            ItemMeta meta = output.getItemMeta();
            meta.getEnchants().forEach(((enchantment, integer) -> {
                meta.removeEnchant(enchantment);
            }));

            outEnchants.forEach(((enchantment, integer) -> {
                meta.addEnchant(enchantment, integer, PLUGIN.getConfigYml().getBool("anvil.allow-existing-unsafe-levels") || PLUGIN.getConfigYml().getBool(ALLOW_UNSAFE_KEY));
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
            if (enchantment instanceof EcoEnchant) {
                outEnchantLevels.addAndGet(integer);
            }
        }));
        leftEnchants.forEach(((enchantment, integer) -> {
            if (enchantment instanceof EcoEnchant) {
                outEnchantLevels.addAndGet(integer);
            }
        }));

        totalEnchantLevelDelta = Math.abs(outEnchantLevels.intValue() - inEnchantLevels.intValue());

        if (output.equals(left)) {
            return AnvilResult.FAIL;
        }

        if (PLUGIN.getConfigYml().getBool("anvil.cost-exponent.enabled")) {
            double exponent = PLUGIN.getConfigYml().getDouble("anvil.cost-exponent.exponent");
            int prevDelta = totalEnchantLevelDelta;

            double costMultiplier = Math.pow(exponent, totalEnchantLevelDelta);
            double modifiedCost = Math.ceil((double) totalEnchantLevelDelta * costMultiplier);
            totalEnchantLevelDelta = (int) modifiedCost;

            if (prevDelta > 0 && totalEnchantLevelDelta == 0) {
                totalEnchantLevelDelta = prevDelta;
            }
        }

        return new AnvilResult(output, totalEnchantLevelDelta);
    }
}
