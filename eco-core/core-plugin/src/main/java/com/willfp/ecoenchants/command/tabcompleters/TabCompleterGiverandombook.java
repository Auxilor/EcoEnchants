package com.willfp.ecoenchants.command.tabcompleters;

import com.willfp.eco.core.command.AbstractCommand;
import com.willfp.eco.core.command.AbstractTabCompleter;
import com.willfp.eco.core.config.ConfigUpdater;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleterGiverandombook extends AbstractTabCompleter {
    /**
     * The cached enchantment names.
     */
    private static final List<String> RARITY_NAMES = EnchantmentRarity.values().stream().map(EnchantmentRarity::getName).collect(Collectors.toList());

    /**
     * Instantiate a new tab-completer for /enchantinfo.
     *
     * @param command /enchantinfo.
     */
    public TabCompleterGiverandombook(@NotNull final AbstractCommand command) {
        super(command);
    }

    /**
     * Called on /ecoreload.
     */
    @ConfigUpdater
    public static void reload() {
        RARITY_NAMES.clear();
        RARITY_NAMES.addAll(EnchantmentRarity.values().stream().map(EnchantmentRarity::getName).collect(Collectors.toList()));
    }

    /**
     * The execution of the tabcompleter.
     *
     * @param sender The sender of the command.
     * @param args   The arguments of the command.
     * @return A list of tab-completions.
     */
    @Override
    public List<String> onTab(@NotNull final CommandSender sender,
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
        }

        Collections.sort(completions);
        return completions;
    }
}
