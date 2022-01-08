package com.willfp.ecoenchants.enchantments.custom;

import com.willfp.eco.core.config.interfaces.Config;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.libreforge.Holder;
import com.willfp.libreforge.conditions.Conditions;
import com.willfp.libreforge.conditions.ConfiguredCondition;
import com.willfp.libreforge.effects.ConfiguredEffect;
import com.willfp.libreforge.effects.Effects;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class CustomEcoEnchantLevel implements Holder {
    /**
     * The parent EcoEnchant.
     */
    @Getter
    private final EcoEnchant parent;

    /**
     * The conditions.
     */
    private final Set<ConfiguredCondition> conditions = new HashSet<>();

    /**
     * The effects.
     */
    private final Set<ConfiguredEffect> effects = new HashSet<>();

    /**
     * Create custom EcoEnchant level.
     *
     * @param parent The parent.
     * @param config The config.
     */
    public CustomEcoEnchantLevel(@NotNull final EcoEnchant parent,
                                 @NotNull final Config config) {
        this.parent = parent;

        for (Config cfg : config.getSubsections("effects")) {
            effects.add(Effects.compile(cfg, "Custom EcoEnchant ID " + parent.getKey().getKey()));
        }

        for (Config cfg : config.getSubsections("conditions")) {
            conditions.add(Conditions.compile(cfg, "Custom EcoEnchant ID " + parent.getKey().getKey()));
        }
    }

    @NotNull
    @Override
    public Set<ConfiguredCondition> getConditions() {
        return conditions;
    }

    @NotNull
    @Override
    public Set<ConfiguredEffect> getEffects() {
        return effects;
    }

    @Override
    public String toString() {
        return "CustomEcoEnchantLevel{" +
                "parent=" + parent +
                '}';
    }
}
