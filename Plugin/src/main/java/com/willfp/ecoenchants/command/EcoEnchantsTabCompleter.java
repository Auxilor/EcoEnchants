package com.willfp.ecoenchants.command;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class EcoEnchantsTabCompleter implements TabCompleter {
    private final List<String> enchantsNames;

    public EcoEnchantsTabCompleter() {
        // Fetching the list of all enchants takes longer than anything inside onTabComplete, so caching is effective
        enchantsNames = EcoEnchants.getAll().stream().map(EcoEnchant::getName).collect(Collectors.toList());
    }

    private static String removePrefix(String s, String prefix) {
        if (s != null && prefix != null && s.startsWith(prefix)) {
            return s.substring(prefix.length());
        }
        return s;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            if (command.getLabel().equalsIgnoreCase("enchantinfo") && (sender.isOp() || sender.hasPermission("ecoenchants.enchantinfo"))) {
                List<String> completions = new ArrayList<>();

                if (args.length == 0) {
                    // Currently, this case is not ever reached
                    return enchantsNames;
                }

                StringUtil.copyPartialMatches(String.join(" ", args), enchantsNames, completions);

                if (args.length > 1) { // Remove all previous words from the candidate of completions
                    ArrayList<String> finishedArgs = new ArrayList<>(Arrays.asList(args));
                    finishedArgs.remove(args.length - 1);

                    String prefix = String.join(" ", finishedArgs);
                    completions = completions.stream().map(enchantName -> removePrefix(enchantName, prefix).trim()).collect(Collectors.toList());
                }

                Collections.sort(completions);
                return completions;
            }
        } catch (Exception ignored) {}
        return null;
    }
}