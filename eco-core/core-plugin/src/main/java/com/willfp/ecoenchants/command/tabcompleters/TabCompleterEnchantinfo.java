package com.willfp.ecoenchants.command.tabcompleters;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.command.AbstractTabCompleter;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
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
    private static final List<String> enchantsNames = EcoEnchants.getAll().stream().filter(EcoEnchant::isEnabled).map(EcoEnchant::getName).collect(Collectors.toList());

    public TabCompleterEnchantinfo(AbstractEcoPlugin plugin) {
        super(plugin, (AbstractCommand) Bukkit.getPluginCommand("enchantinfo").getExecutor());
    }

    public static void reload() {
        enchantsNames.clear();
        enchantsNames.addAll(EcoEnchants.getAll().stream().filter(EcoEnchant::isEnabled).map(EcoEnchant::getName).collect(Collectors.toList()));
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
            completions = completions.stream().map(enchantName -> StringUtils.removePrefix(enchantName, prefix).trim()).collect(Collectors.toList());
        }

        Collections.sort(completions);
        return completions;
    }
}
