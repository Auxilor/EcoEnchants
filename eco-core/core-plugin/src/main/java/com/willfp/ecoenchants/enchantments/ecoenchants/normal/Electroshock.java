package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.LightningUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Electroshock extends EcoEnchant {
    public Electroshock() {
        super(
                "electroshock", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onDeflect(@NotNull final Player blocker,
                          @NotNull final LivingEntity attacker,
                          final int level,
                          @NotNull final EntityDamageByEntityEvent event) {
        double damage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage");

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        boolean silent = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "local-lightning-sound");
        LightningUtils.strike(attacker, damage, silent);
    }
}
