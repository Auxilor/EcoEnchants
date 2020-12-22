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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class Bleed extends EcoEnchant {
    public Bleed() {
        super(
                "bleed", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if (attacker instanceof Player && new ProxyFactory<>(CooldownProxy.class).getProxy().getAttackCooldown((Player) attacker) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged"))
            return;

        if (!EnchantmentUtils.passedChance(this, level))
            return;

        double bleedDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bleed-damage");

        int bleedCount = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "amount-per-level");
        bleedCount *= level;
        final int finalBleedCount = bleedCount;

        AtomicInteger currentBleedCount = new AtomicInteger(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                currentBleedCount.addAndGet(1);

                victim.damage(bleedDamage);

                if (currentBleedCount.get() >= finalBleedCount) this.cancel();
            }
        }.runTaskTimer(this.plugin, 0, 10);
    }
}
