package com.willfp.ecoenchants.enchantments.support.obtaining;

import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class LootGenerateListeners extends PluginDependent<EcoEnchantsPlugin> implements Listener {
    /**
     * Create a new loot populator.
     *
     * @param plugin The this.getPlugin().
     */
    public LootGenerateListeners(@NotNull final EcoEnchantsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onGenerate(@NotNull final LootGenerateEvent event) {
        if (!this.getPlugin().getConfigYml().getBool("loot.enabled")) {
            return;
        }

        for (ItemStack itemStack : event.getLoot()) {
            modifyItem(itemStack);
        }
    }

    private void modifyItem(@Nullable final ItemStack item) {
        if (item == null) {
            return;
        }
        if (!EnchantmentTarget.ALL.getMaterials().contains(item.getType())) {
            return;
        }
        if (item.getType().equals(Material.BOOK)) {
            return;
        }

        Map<Enchantment, Integer> toAdd = new HashMap<>();
        List<Enchantment> existing = FastItemStack.wrap(item).getEnchants(true)
                .keySet().stream().filter(enchant -> !(enchant instanceof EcoEnchant)).collect(Collectors.toList());

        List<EcoEnchant> enchantments = new ArrayList<>(EcoEnchants.values());
        Collections.shuffle(enchantments); // Prevent list bias towards early enchantments like telekinesis

        double multiplier = 0.01;
        if (item.getType().equals(Material.BOOK) || item.getType().equals(Material.ENCHANTED_BOOK)) {
            multiplier /= this.getPlugin().getConfigYml().getInt("loot.book-times-less-likely");
        }

        if (this.getPlugin().getConfigYml().getBool("loot.reduce-probability.enabled")) {
            multiplier /= this.getPlugin().getConfigYml().getDouble("loot.reduce-probability.factor");
        }

        int cap = 0;

        for (EcoEnchant enchantment : enchantments) {
            if (enchantment == null || enchantment.getEnchantmentRarity() == null) {
                continue;
            }

            if (NumberUtils.randFloat(0, 1) > enchantment.getEnchantmentRarity().getLootProbability() * multiplier) {
                continue;
            }

            if (!enchantment.isAvailableFromLoot()) {
                continue;
            }

            if (!enchantment.canEnchantItem(item)) {
                continue;
            }

            if (!enchantment.isEnabled()) {
                continue;
            }

            AtomicBoolean anyConflicts = new AtomicBoolean(false);

            if (enchantment.conflictsWithAny(existing)) {
                anyConflicts.set(true);
            }

            toAdd.forEach((enchant, integer) -> {
                if (enchantment.conflictsWithAny(toAdd.keySet())) {
                    anyConflicts.set(true);
                }
                if (enchant.conflictsWith(enchantment)) {
                    anyConflicts.set(true);
                }

                EcoEnchant ecoEnchant = (EcoEnchant) enchant;
                if (enchantment.getType().equals(ecoEnchant.getType()) && ecoEnchant.getType().isSingular()) {
                    anyConflicts.set(true);
                }
            });
            if (anyConflicts.get()) {
                continue;
            }

            int level;

            if (enchantment.getType().equals(EnchantmentType.SPECIAL)) {
                double enchantlevel1 = NumberUtils.randFloat(0, 1);
                double enchantlevel2 = NumberUtils.bias(enchantlevel1, this.getPlugin().getConfigYml().getDouble("enchanting-table.special-bias"));
                double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            } else {
                double enchantlevel2 = NumberUtils.triangularDistribution(0, 1, 1);
                double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            }

            toAdd.put(enchantment, level);

            if (this.getPlugin().getConfigYml().getBool("loot.reduce-probability.enabled")) {
                multiplier /= this.getPlugin().getConfigYml().getDouble("loot.reduce-probability.factor");
            }

            if (!enchantment.hasFlag("hard-cap-ignore")) {
                cap++;
            }

            if (this.getPlugin().getConfigYml().getBool("anvil.hard-cap.enabled")) {
                if (cap >= this.getPlugin().getConfigYml().getInt("anvil.hard-cap.cap")) {
                    break;
                }
            }
        }

        if (item.getItemMeta() instanceof EnchantmentStorageMeta meta) {
            toAdd.forEach(((enchantment, integer) -> meta.addStoredEnchant(enchantment, integer, false)));
            item.setItemMeta(meta);
        } else {
            ItemMeta meta = item.getItemMeta();
            toAdd.forEach(((enchantment, integer) -> meta.addEnchant(enchantment, integer, false)));
            item.setItemMeta(meta);
        }
    }
}
