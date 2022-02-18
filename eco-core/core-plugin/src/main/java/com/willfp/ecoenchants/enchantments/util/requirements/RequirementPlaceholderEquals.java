package com.willfp.ecoenchants.enchantments.util.requirements;

import com.willfp.eco.core.integrations.placeholder.PlaceholderManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RequirementPlaceholderEquals implements Requirement {
    @Override
    public boolean isMetBy(@NotNull final Player player,
                           @NotNull final List<String> args) {
        if (args.size() < 2) {
            return false;
        }

        return PlaceholderManager.translatePlaceholders(
                args.get(0),
                player
        ).equalsIgnoreCase(args.get(1));
    }
}
