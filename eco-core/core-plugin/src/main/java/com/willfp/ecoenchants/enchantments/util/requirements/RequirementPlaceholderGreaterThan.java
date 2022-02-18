package com.willfp.ecoenchants.enchantments.util.requirements;

import com.willfp.eco.core.integrations.placeholder.PlaceholderManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RequirementPlaceholderGreaterThan implements Requirement {
    @Override
    public boolean isMetBy(@NotNull final Player player,
                           @NotNull final List<String> args) {
        if (args.size() < 2) {
            return false;
        }

        try {
            return Double.parseDouble(PlaceholderManager.translatePlaceholders(
                    args.get(0),
                    player
            )) >= Double.parseDouble(args.get(1));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
