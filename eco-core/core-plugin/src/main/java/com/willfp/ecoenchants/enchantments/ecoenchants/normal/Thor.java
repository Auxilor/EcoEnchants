package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.proxy.proxies.CooldownProxy;
import com.willfp.eco.util.LightningUtils;
import com.willfp.eco.util.ProxyUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Thor extends EcoEnchant {
    public Thor() {
        super(
                "thor", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (attacker instanceof Player
                && ProxyUtils.getProxy(CooldownProxy.class).getAttackCooldown((Player) attacker) != 1.0f
                && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged")) {
            return;
        }

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        double damage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "lightning-damage");

        LightningUtils.strike(victim, damage);
    }
}
