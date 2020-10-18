package com.willfp.ecoenchants.command.commands;

import com.willfp.ecoenchants.command.AbstractCommand;
import com.willfp.ecoenchants.command.AbstractTabCompleter;
import com.willfp.ecoenchants.command.tabcompleters.TabCompleterEnchantinfo;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CommandEnchantinfo extends AbstractCommand {
    public CommandEnchantinfo() {
        super("enchantinfo", "ecoenchants.enchantinfo", false);
    }

    @Override
    public AbstractTabCompleter getTab() {
        return new TabCompleterEnchantinfo();
    }

    @Override
    public void onExecute(CommandSender sender, List<String> args) {
        if(args.size() == 0) {
            sender.sendMessage(ConfigManager.getLang().getMessage("missing-enchant"));
            return;
        }
        StringBuilder nameBuilder = new StringBuilder();

        args.forEach((arg) -> {
            nameBuilder.append(arg).append(" ");
        });
        String searchName = nameBuilder.toString();
        searchName = searchName.substring(0, searchName.length() - 1);

        EcoEnchant enchantment = EcoEnchants.getByName(searchName);

        if(enchantment == null || !enchantment.isEnabled()) {
            String message = ConfigManager.getLang().getMessage("not-found").replace("%name%", searchName);
            sender.sendMessage(message);
            return;
        }

        Set<String> conflictNames = new HashSet<>();

        Set<Enchantment> conflicts = enchantment.getConflicts();

        new HashSet<>(conflicts).forEach(enchantment1 -> {
            if(EcoEnchants.getFromEnchantment(enchantment1) != null) {
                if(!EcoEnchants.getFromEnchantment(enchantment1).isEnabled())
                    conflicts.remove(enchantment1);
            }
        });

        conflicts.forEach((enchantment1 -> {
            if(EcoEnchants.getFromEnchantment(enchantment1) != null) {
                conflictNames.add(EcoEnchants.getFromEnchantment(enchantment1).getName());
            } else {
                conflictNames.add(ConfigManager.getLang().getString("enchantments." + enchantment1.getKey().getKey() + ".name"));
            }
        }));

        StringBuilder conflictNamesBuilder = new StringBuilder();
        conflictNames.forEach((name1) -> {
            conflictNamesBuilder.append(name1).append(", ");
        });
        String allConflicts = conflictNamesBuilder.toString();
        if(allConflicts.length() >= 2) {
            allConflicts = allConflicts.substring(0, allConflicts.length() -2);
        } else {
            allConflicts = ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("no-conflicts"));
        }

        Set<Material> targets = enchantment.getTarget();

        Set<String> applicableItemsSet = new HashSet<>();

        if(ConfigManager.getConfig().getBool("commands.enchantinfo.show-target-group")) {
            enchantment.getRawTarget().forEach(target -> {
                String targetName = target.getName();
                targetName = targetName.toLowerCase();
                targetName = targetName.replaceAll("_", " ");
                targetName = WordUtils.capitalize(targetName);
                applicableItemsSet.add(targetName);
            });
        } else {
            targets.forEach(material -> {
                String matName = material.toString();
                matName = matName.toLowerCase();
                matName = matName.replaceAll("_", " ");
                matName = WordUtils.capitalize(matName);
                applicableItemsSet.add(matName);
            });
        }

        StringBuilder targetNamesBuilder = new StringBuilder();
        applicableItemsSet.forEach((name1) -> {
            targetNamesBuilder.append(name1).append(", ");
        });
        String allTargets = targetNamesBuilder.toString();
        if(allTargets.length() >= 2) {
            allTargets = allTargets.substring(0, allTargets.length() - 2);
        } else {
            allTargets = ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("no-targets"));
        }

        String maxLevel = String.valueOf(enchantment.getMaxLevel());

        StringBuilder descriptionBuilder = new StringBuilder();

        EnchantDisplay.CACHE.get(enchantment).getValue().forEach(s -> {
            descriptionBuilder.append(s);
            descriptionBuilder.append(" ");
        });

        String description = descriptionBuilder.toString();
        description = description.replaceAll("§w", "");
        description = description.replaceAll(EnchantDisplay.descriptionColor, "");

        final String finalName = EnchantDisplay.CACHE.get(enchantment).getKey();
        final String finalDescription = description;
        final String finalTargets = allTargets;
        final String finalConflicts = allConflicts;
        final String finalMaxLevel = maxLevel;
        Arrays.asList(ConfigManager.getLang().getMessage("enchantinfo").split("\\r?\\n")).forEach((string -> {
            string = string.replaceAll("%name%", finalName)
                    .replaceAll("%description%", finalDescription)
                    .replaceAll("%target%", finalTargets)
                    .replaceAll("%conflicts%", finalConflicts)
                    .replaceAll("%maxlevel%", finalMaxLevel);
            sender.sendMessage(string);
        }));
    }
}
