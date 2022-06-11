package com.willfp.ecoenchants.display;

import com.google.common.collect.Lists;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.display.Display;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.display.DisplayPriority;
import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.eco.util.StringUtils;
import com.willfp.ecoenchants.display.options.DisplayOptions;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.custom.CustomEcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.util.ItemConversionOptions;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * All methods and fields pertaining to showing players the enchantments on their items.
 */
@SuppressWarnings("DeprecatedIsStillUsed")
public class EnchantDisplay extends DisplayModule {
    /**
     * The meta key to hide enchantments in lore.
     * <p>
     * EcoEnchants packet lore implementation of HideEnchants.
     */
    @Getter
    private final NamespacedKey keySkip;

    /**
     * The legacy V key.
     * <p>
     * Exists for backwards compatibility.
     */
    @Getter
    @Deprecated
    private final NamespacedKey legacyV;

    /**
     * The configurable options for displaying enchantments.
     */
    @Getter
    private final DisplayOptions options;

    /**
     * Create EcoEnchants display module.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public EnchantDisplay(@NotNull final EcoPlugin plugin) {
        super(plugin, DisplayPriority.LOW);
        keySkip = this.getPlugin().getNamespacedKeyFactory().create("ecoenchantlore-skip");
        legacyV = this.getPlugin().getNamespacedKeyFactory().create("ecoenchantlore-v");
        options = new DisplayOptions(this.getPlugin());
    }

    /**
     * Update config values.
     */
    public void update() {
        options.update();
        EnchantmentCache.update();
    }

    @Override
    public void display(@NotNull final ItemStack itemStack,
                        @Nullable final Player player,
                        @NotNull final Object... args) {
        if (options.isRequireTarget()) {
            if (!EnchantmentTarget.ALL.getMaterials().contains(itemStack.getType())) {
                return;
            }
        }

        FastItemStack fastItemStack = FastItemStack.wrap(itemStack);

        boolean hide = (boolean) args[0];

        if (hide || fastItemStack.getPersistentDataContainer().has(keySkip, PersistentDataType.INTEGER)) {
            fastItemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            if (itemStack.getType() == Material.ENCHANTED_BOOK) {
                fastItemStack.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            }
            fastItemStack.getPersistentDataContainer().set(keySkip, PersistentDataType.INTEGER, 1);
            return;
        }

        List<String> itemLore = fastItemStack.getLore();

        List<String> lore = new ArrayList<>();
        List<String> requirementLore = new ArrayList<>();

        LinkedHashMap<Enchantment, Integer> enchantments = new LinkedHashMap<>(fastItemStack.getEnchants(true));

        enchantments.entrySet().removeIf(enchantmentIntegerEntry -> enchantmentIntegerEntry.getValue().equals(0));

        List<Enchantment> unsorted = new ArrayList<>();
        enchantments.forEach((enchantment, integer) -> unsorted.add(enchantment));

        Map<Enchantment, Integer> tempEnchantments = new HashMap<>(enchantments);

        options.getSorter().sortEnchantments(unsorted);

        enchantments.clear();
        unsorted.forEach(enchantment -> enchantments.put(enchantment, tempEnchantments.get(enchantment)));
        enchantments.forEach((enchantment, level) -> {
            if (enchantment instanceof EcoEnchant ecoEnchant && ecoEnchant.hasFlag("hide-in-lore")) {
                return;
            }

            String name = player == null
                    ? EnchantmentCache.getEntry(enchantment).getNameWithLevel(level)
                    : EnchantmentCache.getEntry(enchantment).getNameWithLevel(level, player);

            lore.add(Display.PREFIX + name);
            if (!options.getDescriptionOptions().isShowingAtBottom()) {
                if (enchantments.size() <= options.getDescriptionOptions().getThreshold()
                        && options.getDescriptionOptions().isEnabled()
                        && options.getDescriptionOptions().enabledForPlayer(player)
                ) {
                    if (options.getDescriptionOptions().isOnlyOnBooks() && itemStack.getType() != Material.ENCHANTED_BOOK) {
                        return;
                    }

                    lore.addAll(EnchantmentCache.getEntry(enchantment).getDescription(level));
                }
            }

            if (player != null && enchantment instanceof EcoEnchant ecoEnchant) {
                if (ecoEnchant instanceof CustomEcoEnchant custom) {
                    requirementLore.addAll(custom.getLevel(level).getNotMetLines(player).stream().map(l -> Display.PREFIX + l).toList());
                }

                if (!ecoEnchant.areRequirementsMet(player)) {
                    requirementLore.addAll(StringUtils.formatList(EnchantmentCache.getEntry(enchantment).getRequirementLore(), player));
                }
            }
        });

        if (options.getShrinkOptions().isEnabled() && (enchantments.size() > options.getShrinkOptions().getThreshold())) {
            List<List<String>> partitionedCombinedLoreList = Lists.partition(lore, options.getShrinkOptions().getShrinkPerLine());
            List<String> newLore = new ArrayList<>();
            partitionedCombinedLoreList.forEach(list -> {
                StringBuilder builder = new StringBuilder();
                for (String s : list) {
                    builder.append(s);
                    builder.append(", ");
                }
                String line = builder.toString();
                line = line.substring(0, line.length() - 2);
                newLore.add(line);
            });
            lore.clear();
            lore.addAll(newLore);
        }

        if (options.getDescriptionOptions().isShowingAtBottom()) {
            if (enchantments.size() <= options.getDescriptionOptions().getThreshold()
                    && options.getDescriptionOptions().isEnabled()
                    && options.getDescriptionOptions().enabledForPlayer(player)
            ) {
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    lore.addAll(EnchantmentCache.getEntry(entry.getKey()).getDescription(entry.getValue()));
                }
            }
        }

        if (!(itemStack.getType() == Material.ENCHANTED_BOOK && !options.isPrefixOnBooks())) {
            if (!enchantments.isEmpty()) {
                lore.addAll(0, options.getLorePrefix());
                lore.addAll(options.getLoreSuffix());
            }
        }

        if (this.getOptions().isAboveLore()) {
            lore.addAll(itemLore);
        } else {
            lore.addAll(0, itemLore);
        }
        lore.addAll(requirementLore);

        if (!lore.isEmpty()) {
            fastItemStack.setLore(lore);
        }

        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            fastItemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        } else {
            fastItemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            fastItemStack.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }
    }

    @Override
    public void revert(@NotNull final ItemStack itemStack) {
        if (options.isRequireTarget()) {
            if (!EnchantmentTarget.ALL.getMaterials().contains(itemStack.getType())) {
                return;
            }
        }

        FastItemStack fastItemStack = FastItemStack.wrap(itemStack);

        PersistentDataContainer pdc = fastItemStack.getPersistentDataContainer();

        // Fixes weird bug. Apparently nullable.
        //noinspection ConstantConditions
        if (pdc == null) {
            return;
        }

        pdc.remove(legacyV);

        if (!pdc.has(keySkip, PersistentDataType.INTEGER)) {
            fastItemStack.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            fastItemStack.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        pdc.remove(keySkip);
    }

    @Override
    public Object[] generateVarArgs(@NotNull final ItemStack itemStack) {
        if (options.isRequireTarget()) {
            if (!EnchantmentTarget.ALL.getMaterials().contains(itemStack.getType())) {
                return new Object[]{false};
            }
        }

        FastItemStack fastItemStack = FastItemStack.wrap(itemStack);

        if (!itemStack.hasItemMeta()) {
            return new Object[]{false};
        }

        boolean hideEnchants = fastItemStack.hasItemFlag(ItemFlag.HIDE_ENCHANTS) || fastItemStack.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS);

        if (fastItemStack.getPersistentDataContainer().has(legacyV, PersistentDataType.INTEGER)) {
            hideEnchants = false;
        }

        if (Display.isFinalized(itemStack)) {
            hideEnchants = false;
        }

        if (ItemConversionOptions.isUsingExperimentalHideFixer() && ItemConversionOptions.isUsingForceHideFixer()) {
            hideEnchants = false;
        }

        if (ItemConversionOptions.isUsingExperimentalHideFixer() && fastItemStack.hasItemFlag(ItemFlag.HIDE_ENCHANTS) && fastItemStack.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
            hideEnchants = false;
        }

        return new Object[]{hideEnchants};
    }
}
