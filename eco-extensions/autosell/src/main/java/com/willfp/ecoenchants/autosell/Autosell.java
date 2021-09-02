package com.willfp.ecoenchants.autosell;


import com.willfp.eco.core.drops.DropQueue;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Autosell extends EcoEnchant {
    private static final Set<Material> FORTUNE_MATERIALS = new HashSet<>(
            Arrays.asList(
                    Material.GOLD_INGOT,
                    Material.IRON_INGOT
            )
    );

    public Autosell() {
        super(
                "autosell", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void infernalTouchBreak(@NotNull final BlockDropItemEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (event.getBlockState() instanceof Container) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (!AntigriefManager.canBreakBlock(player, block)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        Collection<ItemStack> drops = new ArrayList<>();

        for (Item item : event.getItems()) {
            drops.add(item.getItemStack());
        }

        int experience = 0;
        int fortune = EnchantChecks.getMainhandLevel(player, Enchantment.LOOT_BONUS_BLOCKS);

        for (ItemStack itemStack : drops) {
            if (fortune > 0 && FORTUNE_MATERIALS.contains(itemStack.getType())) {
                itemStack.setAmount((int) Math.round((Math.random() * ((double) fortune - 1)) + 1.1));
            }

            double price = ShopGuiPlusApi.getItemStackPriceSell(player, itemStack);
            if (price <= 0) {
                continue;
            }

            EconomyHandler.getInstance().depositPlayer(player, price);

            drops.remove(itemStack);
        }

        event.getItems().clear();

        if (!this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "drop-xp")) {
            experience = 0;
        }

        new DropQueue(player)
                .setLocation(block.getLocation())
                .addItems(drops)
                .addXP(experience)
                .push();
    }
}
