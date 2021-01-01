package com.willfp.ecoenchants.mmo.enchants.stamina;


import com.willfp.eco.util.ProxyUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.mmo.integrations.mmo.MMOManager;
import com.willfp.ecoenchants.mmo.structure.MMOEnchantment;
import com.willfp.ecoenchants.proxy.proxies.CooldownProxy;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Motivate extends MMOEnchantment {
    public Motivate() {
        super("motivate", EnchantmentType.NORMAL);
    }

    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(!(attacker instanceof Player && victim instanceof Player))
            return;
        Player pAttacker = (Player) attacker;
        Player pVictim = (Player) victim;

        boolean notcharged = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged");
        if (ProxyUtils.getProxy(CooldownProxy.class).getAttackCooldown(pAttacker) != 1.0f && !notcharged)
            return;

        double victimStamina = MMOManager.getStamina(pVictim);

        double quantity = (this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percentage-per-level") / 100) * level;

        double toSteal = victimStamina * quantity;

        MMOManager.setStamina(pVictim, victimStamina - toSteal);
        MMOManager.giveStamina(pAttacker, toSteal);
    }
}
