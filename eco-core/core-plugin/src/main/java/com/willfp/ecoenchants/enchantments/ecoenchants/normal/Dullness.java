package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.proxy.ProxyFactory;
import com.willfp.eco.core.proxy.proxies.CooldownProxy;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Dullness extends EcoEnchant {
    public Dullness() {
        super(
                "dullness", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(attacker instanceof Player) {
            if (new ProxyFactory<>(CooldownProxy.class).getProxy().getAttackCooldown((Player) attacker) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged"))
                return;
        }

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        int durationPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "duration-per-level");

        victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, level * durationPerLevel, level));
    }
}
