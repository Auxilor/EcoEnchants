package com.willfp.ecoenchants.command;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandRandomenchant extends Subcommand {
    /**
     * Instantiate a new /ecoreload command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandRandomenchant(@NotNull final EcoPlugin plugin) {
        super(plugin, "randomenchant", "ecoenchants.command.randomenchant", false);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        Player player;

        if ((args.isEmpty() && sender instanceof Player) || !sender.hasPermission("ecoenchants.command.randomenchant.others")) {
            player = (Player) sender;
        } else if (!args.isEmpty()) {
            player = Bukkit.getServer().getPlayer(args.get(0));
        } else {
            player = null;
        }

        if (player == null) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-player"));
            return;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta meta = itemStack.getItemMeta();

        if (itemStack.getType() == Material.AIR || meta == null || !EnchantmentTarget.ALL.getMaterials().contains(itemStack.getType())) {
            if (player.equals(sender)) {
                player.sendMessage(this.getPlugin().getLangYml().getMessage("must-hold-item"));
            } else {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("must-hold-item-other"));
            }
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
                    if (ecoEnchant.isEnabled()) {
                        if (onItem.stream().noneMatch(enchantment -> enchantment.conflictsWith(ecoEnchant))) {
                            if (!onItem.contains(ecoEnchant)) {
                                boolean conflicts = false;
                                for (Enchantment enchantment : onItem) {
                                    if (enchantment instanceof EcoEnchant ecoEnchantOnItem) {
                                        if (ecoEnchantOnItem.getType().equals(ecoEnchant.getType()) && ecoEnchantOnItem.getType().isSingular()) {
                                            conflicts = true;
                                        }
                                    }
                                }
                                if (this.getPlugin().getConfigYml().getBool("anvil.hard-cap.enabled")
                                        && !player.hasPermission("ecoenchants.command.randomenchant.bypasshardcap")
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

    @Override
    public List<String> tabComplete(@NotNull final CommandSender sender,
                                    @NotNull final List<String> args) {
        List<String> completions = new ArrayList<>();

        List<String> playerNames = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());

        if (args.isEmpty() || !sender.hasPermission("ecoenchants.command.randomenchant.others")) {
            // Currently, this case is not ever reached
            return playerNames;
        }

        if (args.size() == 1) {
            StringUtil.copyPartialMatches(String.join(" ", args), playerNames, completions);
            Collections.sort(completions);
            return completions;
        }

        return new ArrayList<>();
    }
}
