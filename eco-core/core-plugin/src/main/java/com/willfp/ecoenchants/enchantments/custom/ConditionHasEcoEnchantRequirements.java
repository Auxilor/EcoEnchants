package com.willfp.ecoenchants.enchantments.custom;

import com.willfp.eco.core.config.interfaces.Config;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.libreforge.conditions.Condition;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConditionHasEcoEnchantRequirements extends Condition {
    /**
     * Create new condition.
     */
    public ConditionHasEcoEnchantRequirements() {
        super("has_ecoenchant_requirements");
    }

    @Override
    public boolean isConditionMet(@NotNull final Player player,
                                  @NotNull final Config config) {
        EcoEnchant ecoEnchant = EcoEnchants.getByKey(NamespacedKey.minecraft(config.getString("enchant")));
        return ecoEnchant.areRequirementsMet(player);
    }
}
