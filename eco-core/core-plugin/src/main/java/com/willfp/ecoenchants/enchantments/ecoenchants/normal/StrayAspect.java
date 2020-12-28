package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.proxy.proxies.CooldownProxy;
import com.willfp.eco.util.ProxyUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class StrayAspect extends EcoEnchant {
    public StrayAspect() {
        super(
                "stray_aspect", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, int level, @NotNull EntityDamageByEntityEvent event) {
        if(attacker instanceof Player && ProxyUtils.getProxy(CooldownProxy.class).getAttackCooldown((Player) attacker) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged")) {
            return;
        }
        if(!EnchantmentUtils.passedChance(this, level))
            return;

        int ticksPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level");

        victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, level * ticksPerLevel, level));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, level * ticksPerLevel, level));
    }
}
