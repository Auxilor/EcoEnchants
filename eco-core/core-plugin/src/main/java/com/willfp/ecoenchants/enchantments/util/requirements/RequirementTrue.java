package com.willfp.ecoenchants.enchantments.util.requirements;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RequirementTrue implements Requirement {
    @Override
    public boolean isMetBy(@NotNull final Player player,
                           @NotNull final List<String> args) {
        return true;
    }
}
