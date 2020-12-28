package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.core.proxy.proxies.CooldownProxy;
import com.willfp.eco.util.ProxyUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Razor extends EcoEnchant {
    public Razor() {
        super(
                "razor", EnchantmentType.SPECIAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, int level, @NotNull EntityDamageByEntityEvent event) {
        double perLevelMultiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double baseDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "base-damage");
        double extra = level*perLevelMultiplier + baseDamage;
        if(this.getConfig().getBool((EcoEnchants.CONFIG_LOCATION) + "decrease-if-cooldown") && attacker instanceof Player) {
            extra *= ProxyUtils.getProxy(CooldownProxy.class).getAttackCooldown((Player) attacker);
        }

        event.setDamage(event.getDamage() + extra);
    }
}
