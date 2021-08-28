package com.willfp.ecoenchants.enchantments.meta.requirements.requirements;

import com.willfp.eco.core.integrations.placeholder.PlaceholderManager;
import com.willfp.ecoenchants.enchantments.meta.requirements.EnchantmentRequirement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RequirementPlaceholderGreaterThan extends EnchantmentRequirement {
    /**
     * Create new requirement.
     */
    public RequirementPlaceholderGreaterThan() {
        super("placeholder-greater-than");
    }

    @Override
    public boolean doesPlayerMeet(@NotNull final Player player,
                                  @NotNull final List<String> args) {
        String placeholder = args.get(0);
        double equals = Double.parseDouble(args.get(1));

        try {
            return Double.parseDouble(PlaceholderManager.translatePlaceholders(placeholder, player)) >= equals;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
