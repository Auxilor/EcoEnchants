package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.queue.DropQueue;
import com.willfp.ecoenchants.util.tuplets.Pair;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public final class InfernalTouch extends EcoEnchant {
    private static final HashMap<Material, Pair<Material, Integer>> recipes = new HashMap<>();
    private static final Set<Material> allowsFortune = new HashSet<>(Arrays.asList(Material.GOLD_INGOT, Material.IRON_INGOT));

    public InfernalTouch() {
        super(
                "infernal_touch", EnchantmentType.NORMAL
        );
    }

    static {
        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (!(recipe instanceof FurnaceRecipe)) {
                continue;
            }
            FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
            recipes.put(furnaceRecipe.getInput().getType(), new Pair<>(furnaceRecipe.getResult().getType(), (int) Math.ceil(furnaceRecipe.getExperience())));
        }
    }

    private static Pair<Material, Integer> getOutput(Material input) {
        Optional<Pair<Material, Integer>> matching = recipes.entrySet().parallelStream().filter(materialPairEntry -> materialPairEntry.getKey().equals(input)).map(Map.Entry::getValue).findFirst();
        return matching.orElse(new Pair<>(input, 0));
    }

    // START OF LISTENERS

    @EventHandler
    public void infernalTouchBreak(BlockDropItemEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!EnchantChecks.mainhand(player, this)) return;

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
            return;

        if (event.getBlock().getState() instanceof Container)
            return;

        if (event.isCancelled())
            return;

        if (!AntigriefManager.canBreakBlock(player, block)) return;

        Collection<ItemStack> drops = new ArrayList<>();
        event.getItems().forEach((item -> {
            drops.add(item.getItemStack());
        }));

        AtomicInteger experience = new AtomicInteger(0);
        int fortune = EnchantChecks.getMainhandLevel(player, Enchantment.LOOT_BONUS_BLOCKS);


        drops.forEach(itemStack -> {
            itemStack.setType(getOutput(itemStack.getType()).getFirst());
            experience.addAndGet(getOutput(itemStack.getType()).getSecond());

            if(fortune > 0 && allowsFortune.contains(itemStack.getType())) {
                itemStack.setAmount((int) Math.ceil(1/((double) fortune + 2) + ((double) fortune + 1)/2));
                experience.addAndGet(1);
            }
        });

        event.getItems().clear();

        new DropQueue(player)
                .setLocation(block.getLocation())
                .addItems(drops)
                .addXP(experience.get())
                .push();
    }
}
