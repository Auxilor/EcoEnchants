package com.willfp.ecoenchants.command;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.eco.core.items.builder.EnchantedBookBuilder;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandGiverandombook extends Subcommand {
    /**
     * The cached enchantment names.
     */
    private static final List<String> RARITY_NAMES = EnchantmentRarity.values().stream().map(EnchantmentRarity::getName).collect(Collectors.toList());

    /**
     * The cached type names.
     */
    private static final List<String> TYPE_NAMES = EnchantmentType.values().stream().map(EnchantmentType::getName).collect(Collectors.toList());

    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandGiverandombook(@NotNull final EcoPlugin plugin) {
        super(plugin, "giverandombook", "ecoenchants.command.giverandombook", false);
    }

    /**
     * Called on /reload.
     */
    @ConfigUpdater
    public static void reload() {
        RARITY_NAMES.clear();
        RARITY_NAMES.addAll(EnchantmentRarity.values().stream().map(EnchantmentRarity::getName).collect(Collectors.toList()));
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        if (args.isEmpty()) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("requires-player"));
            return;
        }
        Player player = Bukkit.getServer().getPlayer(args.get(0));

        EnchantmentRarity rarity = args.size() >= 2 ? EnchantmentRarity.getByName(args.get(1).toLowerCase()) : null;

        EnchantmentType type = rarity == null && args.size() >= 2 ? EnchantmentType.getByName(args.get(1).toLowerCase()) : null;


        if (player == null) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-player"));
            return;
        }

        List<Enchantment> allowed = Arrays.stream(Enchantment.values()).filter(enchantment -> {
            if (enchantment instanceof EcoEnchant) {
                return ((EcoEnchant) enchantment).isEnabled();
            }
            return true;
        }).filter(enchantment -> {
            if (rarity != null) {
                if (!(enchantment instanceof EcoEnchant ecoEnchant)) {
                    return false;
                }
                return ecoEnchant.getEnchantmentRarity().equals(rarity);
            } else if (type != null) {
                if (!(enchantment instanceof EcoEnchant ecoEnchant)) {
                    return false;
                }
                return ecoEnchant.getType().equals(type);
            }
            return true;
        }).collect(Collectors.toList());

        Enchantment enchantment = allowed.get(NumberUtils.randInt(0, allowed.size() - 1));
        int level = NumberUtils.randInt(1, enchantment.getMaxLevel());

        ItemStack itemStack = new EnchantedBookBuilder()
                .addStoredEnchantment(enchantment, level)
                .build();

        for (ItemStack stack : player.getInventory().addItem(itemStack).values()) {
            player.getWorld().dropItem(player.getLocation(), stack);
        }

        String message = this.getPlugin().getLangYml().getMessage("gave-random-book");
        message = message.replace("%enchantment%", EnchantmentCache.getEntry(enchantment).getName() + " " + NumberUtils.toNumeral(level) + "§r");
        sender.sendMessage(message);

        String message2 = this.getPlugin().getLangYml().getMessage("received-random-book");
        message2 = message2.replace("%enchantment%", EnchantmentCache.getEntry(enchantment).getName() + " " + NumberUtils.toNumeral(level) + "§r");
        player.sendMessage(message2);
    }

    @Override
    public List<String> tabComplete(@NotNull final CommandSender sender,
                                    @NotNull final List<String> args) {
        List<String> completions = new ArrayList<>();

        List<String> playerNames = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());

        if (args.isEmpty()) {
            // Currently, this case is not ever reached
            return playerNames;
        }

        if (args.size() == 1) {
            StringUtil.copyPartialMatches(String.join(" ", args.get(0)), playerNames, completions);
        }

        if (args.size() == 2) {
            StringUtil.copyPartialMatches(String.join(" ", args.get(1)), RARITY_NAMES, completions);
            StringUtil.copyPartialMatches(String.join(" ", args.get(1)), TYPE_NAMES, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
