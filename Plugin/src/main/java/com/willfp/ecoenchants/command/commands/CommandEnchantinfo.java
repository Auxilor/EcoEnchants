package com.willfp.ecoenchants.command.commands;

import com.willfp.ecoenchants.command.AbstractCommand;
import com.willfp.ecoenchants.config.ConfigManager;
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

        boolean notFound = enchantment == null;

        if(notFound) {
            String message = ConfigManager.getLang().getMessage("not-found").replace("%name%", searchName);
            sender.sendMessage(message);
            return;
        }

        String name;
        String color;
        List<String> description;

        boolean isCurse = enchantment.isCursed();
        boolean isSpecial = false;
        boolean isArtifact = false;

        if(enchantment.getType().equals(EcoEnchant.EnchantmentType.SPECIAL)) {
            isSpecial = true;
        }
        if(enchantment.getType().equals(EcoEnchant.EnchantmentType.ARTIFACT)) {
            isArtifact = true;
        }

        if(isCurse) color = ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("curse-color"));
        else if(isSpecial) color = ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("special-color"));
        else if(isArtifact) color = ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("artifact-color"));
        else color = ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("not-curse-color"));


        name = enchantment.getName();
        description = EcoEnchants.getFromEnchantment(enchantment).getDescription();
        StringBuilder descriptionBuilder = new StringBuilder();
        description.forEach((line) -> {
            descriptionBuilder.append(line).append(" ");
        });
        String desc = descriptionBuilder.toString();

        Set<String> conflictNames = new HashSet<>();

        Set<Enchantment> conflicts = enchantment.getConflicts();

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

        targets.forEach(material -> {
            String matName = material.toString();
            matName = matName.toLowerCase();
            matName = matName.replaceAll("_", " ");
            matName = WordUtils.capitalize(matName);
            applicableItemsSet.add(matName);
        });

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

        final String finalName = color + name;
        final String finalDescription = desc;
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
