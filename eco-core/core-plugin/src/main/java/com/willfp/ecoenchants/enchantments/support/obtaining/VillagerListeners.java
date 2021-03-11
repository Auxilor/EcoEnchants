package com.willfp.ecoenchants.enchantments.support.obtaining;


import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class VillagerListeners extends PluginDependent implements Listener {
    /**
     * Create new villager listeners.
     *
     * @param plugin The plugin.
     */
    public VillagerListeners(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Called on villager gain trade.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onVillagerGainBookTrade(@NotNull final VillagerAcquireTradeEvent event) {
        if (!event.getRecipe().getResult().getType().equals(Material.ENCHANTED_BOOK)) {
            return;
        }

        if (!this.getPlugin().getConfigYml().getBool("villager.enabled")) {
            return;
        }

        ItemStack result = event.getRecipe().getResult().clone();
        int uses = event.getRecipe().getUses();
        int maxUses = event.getRecipe().getMaxUses();
        boolean experienceReward = event.getRecipe().hasExperienceReward();
        int villagerExperience = event.getRecipe().getVillagerExperience();
        float priceMultiplier = event.getRecipe().getPriceMultiplier();
        List<ItemStack> ingredients = event.getRecipe().getIngredients();

        if (!(result.getItemMeta() instanceof EnchantmentStorageMeta)) {
            return;
        }

        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) result.getItemMeta();

        ArrayList<EcoEnchant> enchantments = new ArrayList<>(EcoEnchants.values());
        Collections.shuffle(enchantments); // Prevent list bias towards early enchantments like telekinesis

        double multiplier = 0.01 / this.getPlugin().getConfigYml().getDouble("villager.book-times-less-likely");

        for (EcoEnchant enchantment : enchantments) {
            if (NumberUtils.randFloat(0, 1) > enchantment.getRarity().getVillagerProbability() * multiplier) {
                continue;
            }

            if (!enchantment.isAvailableFromVillager()) {
                continue;
            }

            if (!enchantment.isEnabled()) {
                continue;
            }

            int level;

            if (enchantment.getType().equals(EnchantmentType.SPECIAL)) {
                double enchantlevel1 = NumberUtils.randFloat(0, 1);
                double enchantlevel2 = NumberUtils.bias(enchantlevel1, this.getPlugin().getConfigYml().getDouble("enchanting-table.special-bias"));
                double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            } else {
                int cost = event.getRecipe().getIngredients().get(0).getAmount();
                double enchantlevel1 = (double) cost / 64;
                double enchantlevel2 = NumberUtils.triangularDistribution(0, 1, enchantlevel1);
                double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            }

            meta.getStoredEnchants().forEach(((enchantment1, integer) -> meta.removeStoredEnchant(enchantment1)));

            meta.addStoredEnchant(enchantment, level, false);
            break;
        }

        result.setItemMeta(meta);

        MerchantRecipe recipe = new MerchantRecipe(result, uses, maxUses, experienceReward, villagerExperience, priceMultiplier);
        recipe.setIngredients(ingredients);
        event.setRecipe(recipe);
    }

    /**
     * Called on villager gain trade.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onVillagerGainItemTrade(@NotNull final VillagerAcquireTradeEvent event) {

        if (!EnchantmentTarget.ALL.getMaterials().contains(event.getRecipe().getResult().getType())) {
            return;
        }

        if (event.getRecipe().getResult().getType().equals(Material.BOOK)) {
            return;
        }

        if (!this.getPlugin().getConfigYml().getBool("villager.enabled")) {
            return;
        }

        ItemStack result = event.getRecipe().getResult().clone();
        int uses = event.getRecipe().getUses();
        int maxUses = event.getRecipe().getMaxUses();
        boolean experienceReward = event.getRecipe().hasExperienceReward();
        int villagerExperience = event.getRecipe().getVillagerExperience();
        float priceMultiplier = event.getRecipe().getPriceMultiplier();
        List<ItemStack> ingredients = event.getRecipe().getIngredients();

        if (result.getItemMeta() instanceof EnchantmentStorageMeta) {
            return;
        }

        ItemMeta meta = result.getItemMeta();

        ArrayList<EcoEnchant> enchantments = new ArrayList<>(EcoEnchants.values());
        Collections.shuffle(enchantments); // Prevent list bias towards early enchantments like telekinesis

        Map<EcoEnchant, Integer> toAdd = new HashMap<>();

        double multiplier = 0.01;

        for (EcoEnchant enchantment : enchantments) {
            if (NumberUtils.randFloat(0, 1) > enchantment.getRarity().getVillagerProbability() * multiplier) {
                continue;
            }

            if (!enchantment.isAvailableFromVillager()) {
                continue;
            }

            if (!enchantment.canEnchantItem(result)) {
                continue;
            }

            if (!enchantment.isEnabled()) {
                continue;
            }

            AtomicBoolean anyConflicts = new AtomicBoolean(false);
            toAdd.forEach((enchant, integer) -> {
                if (enchantment.conflictsWithAny(toAdd.keySet())) {
                    anyConflicts.set(true);
                }

                if (enchant.conflictsWith(enchantment)) {
                    anyConflicts.set(true);
                }

                if (enchantment.conflictsWith(enchant)) {
                    anyConflicts.set(true);
                }

                EcoEnchant ecoEnchant = EcoEnchants.getFromEnchantment(enchant);

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
                int cost = event.getRecipe().getIngredients().get(0).getAmount();
                double enchantlevel1 = (double) cost / 64;
                double enchantlevel2 = NumberUtils.triangularDistribution(0, 1, enchantlevel1);
                double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            }

            toAdd.put(enchantment, level);

            if (this.getPlugin().getConfigYml().getBool("villager.reduce-probability.enabled")) {
                multiplier /= this.getPlugin().getConfigYml().getDouble("villager.reduce-probability.factor");
            }
        }

        toAdd.forEach(((enchantment, integer) -> meta.addEnchant(enchantment, integer, false)));

        result.setItemMeta(meta);

        MerchantRecipe recipe = new MerchantRecipe(result, uses, maxUses, experienceReward, villagerExperience, priceMultiplier);
        recipe.setIngredients(ingredients);
        event.setRecipe(recipe);
    }
}
