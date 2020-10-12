package com.willfp.ecoenchants.command.tabcompleters;

import com.willfp.ecoenchants.command.AbstractCommand;
import com.willfp.ecoenchants.command.AbstractTabCompleter;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleterEnchantinfo extends AbstractTabCompleter {
    private final List<String> enchantsNames;

    public TabCompleterEnchantinfo() {
        super((AbstractCommand) Bukkit.getPluginCommand("enchantinfo").getExecutor());
        enchantsNames = EcoEnchants.getAll().stream().map(EcoEnchant::getName).collect(Collectors.toList());
    }

    private static String removePrefix(String s, String prefix) {
        if (s != null && prefix != null && s.startsWith(prefix)) {
            return s.substring(prefix.length());
        }
        return s;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> args) {
        List<String> completions = new ArrayList<>();

        if (args.size() == 0) {
            // Currently, this case is not ever reached
            return enchantsNames;
        }

        StringUtil.copyPartialMatches(String.join(" ", args), enchantsNames, completions);

        if (args.size() > 1) { // Remove all previous words from the candidate of completions
            ArrayList<String> finishedArgs = new ArrayList<>(args);
            finishedArgs.remove(args.size() - 1);

            String prefix = String.join(" ", finishedArgs);
            completions = completions.stream().map(enchantName -> removePrefix(enchantName, prefix).trim()).collect(Collectors.toList());
        }

        Collections.sort(completions);
        return completions;
    }
}
