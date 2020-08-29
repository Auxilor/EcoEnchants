package com.willfp.ecoenchants.listeners;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.Bias;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class VillagerListeners implements Listener {

    // For books
    @EventHandler
    public void onVillagerGainBookTrade(VillagerAcquireTradeEvent event) {
        if(!event.getRecipe().getResult().getType().equals(Material.ENCHANTED_BOOK))
            return;

        if(!ConfigManager.getConfig().getBool("villager.enabled"))
            return;

        ItemStack result = event.getRecipe().getResult().clone();
        int uses = event.getRecipe().getUses();
        int maxUses = event.getRecipe().getMaxUses();
        boolean experienceReward = event.getRecipe().hasExperienceReward();
        int villagerExperience = event.getRecipe().getVillagerExperience();
        float priceMultiplier = event.getRecipe().getPriceMultiplier();
        List<ItemStack> ingredients = event.getRecipe().getIngredients();

        if(!(result.getItemMeta() instanceof EnchantmentStorageMeta)) return;

        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) result.getItemMeta();

        ArrayList<EcoEnchant> enchantments = new ArrayList<>(EcoEnchants.getAll());
        Collections.shuffle(enchantments); // Prevent list bias towards early enchantments like telekinesis

        double multiplier = 0.01 / ConfigManager.getConfig().getDouble("villager.book-times-less-likely");

        for(EcoEnchant enchantment : enchantments) {
            if(Rand.randFloat(0, 1) > enchantment.getRarity().getVillagerProbability() * multiplier)
                continue;
            if(!enchantment.canGetFromVillager())
                continue;

            int level;

            if(enchantment.getType().equals(EcoEnchant.EnchantmentType.SPECIAL)) {
                double enchantlevel1 = Rand.randFloat(0, 1);
                double enchantlevel2 = Bias.bias(enchantlevel1, ConfigManager.getConfig().getDouble("enchanting-table.special-bias"));
                double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            } else {
                int cost = event.getRecipe().getIngredients().get(0).getAmount();
                double enchantlevel1 = cost / 64;
                double enchantlevel2 = Rand.triangularDistribution(0, 1, enchantlevel1);
                double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            }

            meta.getStoredEnchants().forEach(((enchantment1, integer) -> {
                meta.removeStoredEnchant(enchantment1);
            }));

            meta.addStoredEnchant(enchantment, level, false);
            break;
        }

        result.setItemMeta(meta);

        MerchantRecipe recipe = new MerchantRecipe(result, uses, maxUses, experienceReward, villagerExperience, priceMultiplier);
        recipe.setIngredients(ingredients);
        event.setRecipe(recipe);
    }

    // For tools
    @EventHandler
    public void onVillagerGainItemTrade(VillagerAcquireTradeEvent event) {

        if(!Target.Applicable.ALL.getMaterials().contains(event.getRecipe().getResult().getType()))
            return;

        if(event.getRecipe().getResult().getType().equals(Material.BOOK)) return;

        if(!ConfigManager.getConfig().getBool("villager.enabled"))
            return;

        ItemStack result = event.getRecipe().getResult().clone();
        int uses = event.getRecipe().getUses();
        int maxUses = event.getRecipe().getMaxUses();
        boolean experienceReward = event.getRecipe().hasExperienceReward();
        int villagerExperience = event.getRecipe().getVillagerExperience();
        float priceMultiplier = event.getRecipe().getPriceMultiplier();
        List<ItemStack> ingredients = event.getRecipe().getIngredients();

        if(result.getItemMeta() instanceof EnchantmentStorageMeta) return;

        ItemMeta meta = result.getItemMeta();

        ArrayList<EcoEnchant> enchantments = new ArrayList<>(EcoEnchants.getAll());
        Collections.shuffle(enchantments); // Prevent list bias towards early enchantments like telekinesis

        Map<EcoEnchant, Integer> toAdd = new HashMap<>();

        double multiplier = 0.01;

        for(EcoEnchant enchantment : enchantments) {
            if(Rand.randFloat(0, 1) > enchantment.getRarity().getVillagerProbability() * multiplier)
                continue;
            if(!enchantment.canGetFromVillager())
                continue;
            if(!enchantment.canEnchantItem(result))
                continue;

            AtomicBoolean anyConflicts = new AtomicBoolean(false);
            toAdd.forEach((enchant, integer) -> {
                if(enchantment.conflictsWithAny(toAdd.keySet())) anyConflicts.set(true);
                if(enchant.conflictsWith(enchantment)) anyConflicts.set(true);

                EcoEnchant ecoEnchant = EcoEnchants.getFromEnchantment(enchant);
                if(enchantment.getType().equals(EcoEnchant.EnchantmentType.SPECIAL) && ecoEnchant.getType().equals(EcoEnchant.EnchantmentType.SPECIAL))
                    anyConflicts.set(true);
                if(enchantment.getType().equals(EcoEnchant.EnchantmentType.ARTIFACT) && ecoEnchant.getType().equals(EcoEnchant.EnchantmentType.ARTIFACT))
                    anyConflicts.set(true);
            });
            if(anyConflicts.get()) continue;

            int level;

            if(enchantment.getType().equals(EcoEnchant.EnchantmentType.SPECIAL)) {
                double enchantlevel1 = Rand.randFloat(0, 1);
                double enchantlevel2 = Bias.bias(enchantlevel1, ConfigManager.getConfig().getDouble("enchanting-table.special-bias"));
                double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            } else {
                int cost = event.getRecipe().getIngredients().get(0).getAmount();
                double enchantlevel1 = cost / 64;
                double enchantlevel2 = Rand.triangularDistribution(0, 1, enchantlevel1);
                double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            }

            toAdd.put(enchantment, level);

            if(ConfigManager.getConfig().getBool("villager.reduce-probability.enabled")) {
                multiplier /= ConfigManager.getConfig().getDouble("villager.reduce-probability.factor");
            }
        }

        toAdd.forEach(((enchantment, integer) -> {
            meta.addEnchant(enchantment, integer, false);
        }));

        result.setItemMeta(meta);

        MerchantRecipe recipe = new MerchantRecipe(result, uses, maxUses, experienceReward, villagerExperience, priceMultiplier);
        recipe.setIngredients(ingredients);
        event.setRecipe(recipe);
    }
}
