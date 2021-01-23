package com.willfp.ecoenchants.command.commands;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.command.AbstractTabCompleter;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.command.tabcompleters.TabCompleterEnchantinfo;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandEnchantinfo extends AbstractCommand {
    /**
     * Instantiate a new /enchantinfo command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandEnchantinfo(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, "enchantinfo", "ecoenchants.enchantinfo", false);
    }

    @Override
    public AbstractTabCompleter getTab() {
        return new TabCompleterEnchantinfo();
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

        if (enchantment == null || !enchantment.isEnabled()) {
            String message = this.getPlugin().getLangYml().getMessage("not-found").replace("%name%", searchName);
            sender.sendMessage(message);
            return;
        }

        Set<String> conflictNames = new HashSet<>();

        Set<Enchantment> conflicts = enchantment.getConflicts();

        new HashSet<>(conflicts).forEach(enchantment1 -> {
            EcoEnchant ecoEnchant = EcoEnchants.getFromEnchantment(enchantment1);
            if (ecoEnchant != null && !ecoEnchant.isEnabled()) {
                conflicts.remove(enchantment1);
            }
        });

        conflicts.forEach((enchantment1 -> {
            if (EcoEnchants.getFromEnchantment(enchantment1) != null) {
                conflictNames.add(EcoEnchants.getFromEnchantment(enchantment1).getName());
            } else {
                conflictNames.add(this.getPlugin().getLangYml().getString("enchantments." + enchantment1.getKey().getKey() + ".name"));
            }
        }));

        StringBuilder conflictNamesBuilder = new StringBuilder();
        conflictNames.forEach(name1 -> conflictNamesBuilder.append(name1).append(", "));
        String allConflicts = conflictNamesBuilder.toString();
        if (allConflicts.length() >= 2) {
            allConflicts = allConflicts.substring(0, allConflicts.length() - 2);
        } else {
            allConflicts = StringUtils.translate(this.getPlugin().getLangYml().getString("no-conflicts"));
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
            allTargets = StringUtils.translate(this.getPlugin().getLangYml().getString("no-targets"));
        }

        String maxLevel = String.valueOf(enchantment.getMaxLevel());

        final String finalName = EnchantmentCache.getEntry(enchantment).getName();
        final String finalDescription = EnchantmentCache.getEntry(enchantment).getStringDescription();
        final String finalTargets = allTargets;
        final String finalConflicts = allConflicts;
        final String finalMaxLevel = maxLevel;
        Arrays.asList(this.getPlugin().getLangYml().getMessage("enchantinfo").split("\\r?\\n")).forEach((string -> {
            string = string.replace("%name%", finalName)
                    .replace("%description%", finalDescription)
                    .replace("%target%", finalTargets)
                    .replace("%conflicts%", finalConflicts)
                    .replace("%maxlevel%", finalMaxLevel);
            sender.sendMessage(string);
        }));
    }
}
