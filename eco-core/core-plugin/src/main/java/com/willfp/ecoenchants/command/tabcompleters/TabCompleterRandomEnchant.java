package com.willfp.ecoenchants.command.tabcompleters;

import com.willfp.eco.core.command.AbstractCommand;
import com.willfp.eco.core.command.AbstractTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleterRandomEnchant extends AbstractTabCompleter {
    /**
     * Instantiate a new tab-completer for /randomenchant.
     *
     * @param command /randomenchant.
     */
    public TabCompleterRandomEnchant(@NotNull final AbstractCommand command) {
        super(command);
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

        if (args.isEmpty() || !sender.hasPermission("ecoenchants.randomenchant.others")) {
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
