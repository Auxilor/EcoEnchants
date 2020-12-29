package com.willfp.ecoenchants.command.tabcompleters;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.command.AbstractTabCompleter;
import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TabCompleterEnchantinfo extends AbstractTabCompleter {
    /**
     * The cached enchantment names.
     */
    private static final List<String> ENCHANT_NAMES = EcoEnchants.values().stream().filter(EcoEnchant::isEnabled).map(EcoEnchant::getName).collect(Collectors.toList());

    /**
     * Instantiate a new tab-completer for /enchantinfo.
     */
    public TabCompleterEnchantinfo() {
        super((AbstractCommand) Objects.requireNonNull(Bukkit.getPluginCommand("enchantinfo")).getExecutor());
    }

    /**
     * Called on /ecoreload.
     */
    @ConfigUpdater
    public static void reload() {
        ENCHANT_NAMES.clear();
        ENCHANT_NAMES.addAll(EcoEnchants.values().stream().filter(EcoEnchant::isEnabled).map(EcoEnchant::getName).collect(Collectors.toList()));
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

        if (args.isEmpty()) {
            // Currently, this case is not ever reached
            return ENCHANT_NAMES;
        }

        StringUtil.copyPartialMatches(String.join(" ", args), ENCHANT_NAMES, completions);

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
