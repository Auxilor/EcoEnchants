package com.willfp.ecoenchants.command;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.eco.util.StringUtils;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandEnchantinfo extends PluginCommand {
    /**
     * Instantiate a new /enchantinfo command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandEnchantinfo(@NotNull final EcoPlugin plugin) {
        super(plugin, "enchantinfo", "ecoenchants.command.enchantinfo", false);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        if (args.isEmpty()) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("missing-enchant"));
            return;
        }
        StringBuilder nameBuilder = new StringBuilder();

        args.forEach(arg -> nameBuilder.append(arg).append(" "));
        String searchName = nameBuilder.toString();
        searchName = searchName.substring(0, searchName.length() - 1);

        EcoEnchant enchantment = EcoEnchants.getByName(searchName);

        if (enchantment == null) {
            String finalSearchName = searchName;
            enchantment = EcoEnchants.values().stream().filter(ecoEnchant -> ChatColor.stripColor(ecoEnchant.getDisplayName()).equalsIgnoreCase(finalSearchName)).findFirst().orElse(null);
        }

        if (enchantment == null || !enchantment.isEnabled()) {
            String message = this.getPlugin().getLangYml().getMessage("not-found").replace("%name%", searchName);
            sender.sendMessage(message);
            return;
        }

        Set<String> conflictNames = new HashSet<>();

        Set<Enchantment> conflicts = enchantment.getConflicts();

        new HashSet<>(conflicts).forEach(enchantment1 -> {
            if (enchantment1 instanceof EcoEnchant ecoEnchant) {
                if (!ecoEnchant.isEnabled()) {
                    conflicts.remove(enchantment1);
                }
            }
        });

        conflicts.forEach((enchantment1 -> {
            if (enchantment1 instanceof EcoEnchant enchant) {
                conflictNames.add(enchant.getDisplayName());
            } else {
                conflictNames.add(this.getPlugin().getLangYml().getFormattedString("enchantments." + enchantment1.getKey().getKey() + ".name"));
            }
        }));

        StringBuilder conflictNamesBuilder = new StringBuilder();
        conflictNames.forEach(name1 -> conflictNamesBuilder.append(name1).append(", "));
        String allConflicts = conflictNamesBuilder.toString();
        if (allConflicts.length() >= 2) {
            allConflicts = allConflicts.substring(0, allConflicts.length() - 2);
        } else {
            allConflicts = this.getPlugin().getLangYml().getFormattedString("no-conflicts");
        }

        Set<Material> targets = enchantment.getTargetMaterials();

        Set<String> applicableItemsSet = new HashSet<>();

        if (this.getPlugin().getConfigYml().getBool("commands.enchantinfo.show-target-group")) {
            enchantment.getTargets().forEach(target -> {
                String targetName = target.getName();
                targetName = targetName.toLowerCase();
                targetName = targetName.replace("_", " ");
                targetName = WordUtils.capitalize(targetName);
                applicableItemsSet.add(targetName);
            });
        } else {
            targets.forEach(material -> {
                String matName = material.toString();
                matName = matName.toLowerCase();
                matName = matName.replace("_", " ");
                matName = WordUtils.capitalize(matName);
                applicableItemsSet.add(matName);
            });
        }

        StringBuilder targetNamesBuilder = new StringBuilder();
        applicableItemsSet.forEach(name1 -> targetNamesBuilder.append(name1).append(", "));
        String allTargets = targetNamesBuilder.toString();
        if (allTargets.length() >= 2) {
            allTargets = allTargets.substring(0, allTargets.length() - 2);
        } else {
            allTargets = this.getPlugin().getLangYml().getFormattedString("no-targets");
        }

        String maxLevel = String.valueOf(enchantment.getMaxLevel());

        final String finalName = EnchantmentCache.getEntry(enchantment).getName();
        final String finalDescription = EnchantmentCache.getEntry(enchantment).getStringDescription(1);
        final EnchantmentRarity finalRarity = enchantment.getEnchantmentRarity();
        final String finalTargets = allTargets;
        final String finalConflicts = allConflicts;
        final String finalMaxLevel = maxLevel;
        Arrays.asList(this.getPlugin().getLangYml().getMessage("enchantinfo").split("\\r?\\n")).forEach((string -> {
            string = string.replace("%name%", finalName)
                    .replace("%description%", finalDescription)
                    .replace("%rarity%", finalRarity.getName())
                    .replace("%target%", finalTargets)
                    .replace("%conflicts%", finalConflicts)
                    .replace("%maxlevel%", finalMaxLevel);
            sender.sendMessage(string);
        }));
    }

    @Override
    public List<String> tabComplete(@NotNull final CommandSender sender,
                                    @NotNull final List<String> args) {
        List<String> completions = new ArrayList<>();

        List<String> names = EcoEnchants.values().stream().filter(EcoEnchant::isEnabled).map(EcoEnchant::getDisplayName).map(ChatColor::stripColor).toList();

        if (args.isEmpty()) {
            // Currently, this case is not ever reached
            return names;
        }

        StringUtil.copyPartialMatches(String.join(" ", args), names, completions);

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
