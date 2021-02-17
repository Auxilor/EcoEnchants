package com.willfp.ecoenchants.command.commands;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRandomenchant extends AbstractCommand {
    /**
     * Instantiate a new /ecoreload command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandRandomenchant(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, "randomenchant", "ecoenchants.randomenchant", true);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        Player player = (Player) sender;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta meta = itemStack.getItemMeta();

        if (itemStack.getType() == Material.AIR || meta == null || !EnchantmentTarget.ALL.getMaterials().contains(itemStack.getType())) {
            player.sendMessage(this.getPlugin().getLangYml().getMessage("must-hold-item"));
            return;
        }

        List<EcoEnchant> ecoEnchants = new ArrayList<>(EcoEnchants.values());
        Collections.shuffle(ecoEnchants);
        EcoEnchant enchant = null;

        List<Enchantment> onItem = new ArrayList<>();

        if (meta instanceof EnchantmentStorageMeta) {
            onItem.addAll(((EnchantmentStorageMeta) meta).getStoredEnchants().keySet());
        } else {
            onItem.addAll(meta.getEnchants().keySet());
        }

        for (EcoEnchant ecoEnchant : ecoEnchants) {
            if (ecoEnchant.canEnchantItem(itemStack)) {
                if (!ecoEnchant.conflictsWithAny(onItem)) {
                    if (onItem.stream().noneMatch(enchantment -> enchantment.conflictsWith(ecoEnchant))) {
                        if (!onItem.contains(ecoEnchant)) {
                            boolean conflicts = false;
                            for (Enchantment enchantment : onItem) {
                                if (EcoEnchants.getFromEnchantment(enchantment) != null) {
                                    EcoEnchant ecoEnchantOnItem = EcoEnchants.getFromEnchantment(enchantment);
                                    if (ecoEnchantOnItem.getType().equals(ecoEnchant.getType()) && ecoEnchantOnItem.getType().isSingular()) {
                                        conflicts = true;
                                    }
                                }
                            }
                            if (this.getPlugin().getConfigYml().getBool("anvil.hard-cap.enabled")
                                    && !player.hasPermission("ecoenchants.randomenchant.bypasshardcap")
                                    && onItem.size() >= this.getPlugin().getConfigYml().getInt("anvil.hard-cap.cap")) {
                                conflicts = true;
                            }
                            if (!conflicts) {
                                enchant = ecoEnchant;
                            }
                        }
                    }
                }
            }
        }

        if (enchant == null) {
            player.sendMessage(this.getPlugin().getLangYml().getMessage("no-enchants-available"));
            return;
        }

        if (meta instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) meta).addStoredEnchant(enchant, enchant.getMaxLevel(), true);
        } else {
            meta.addEnchant(enchant, enchant.getMaxLevel(), true);
        }

        itemStack.setItemMeta(meta);
        String message = this.getPlugin().getLangYml().getMessage("applied-random-enchant");
        message = message.replace("%enchantment%", EnchantmentCache.getEntry(enchant).getName() + "§r");
        player.sendMessage(message);
    }
}
