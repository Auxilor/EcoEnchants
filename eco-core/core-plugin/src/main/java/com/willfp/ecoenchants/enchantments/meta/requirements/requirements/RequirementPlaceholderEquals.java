package com.willfp.ecoenchants.enchantments.meta.requirements.requirements;

import com.willfp.eco.core.integrations.placeholder.PlaceholderManager;
import com.willfp.ecoenchants.enchantments.meta.requirements.EnchantmentRequirement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RequirementPlaceholderEquals extends EnchantmentRequirement {
    /**
     * Create new requirement.
     */
    public RequirementPlaceholderEquals() {
        super("placeholder-equals");
    }

    @Override
    public boolean doesPlayerMeet(@NotNull final Player player,
                                  @NotNull final List<String> args) {
        String placeholder = args.get(0);
        String equals = args.get(1);

        return PlaceholderManager.translatePlaceholders(placeholder, player).equalsIgnoreCase(equals);
    }
}
