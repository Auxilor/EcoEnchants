package com.willfp.ecoenchants.mmo.enchants.mana;


import com.willfp.eco.core.proxy.proxies.CooldownProxy;
import com.willfp.eco.util.ProxyUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.mmo.integrations.mmo.MMOManager;
import com.willfp.ecoenchants.mmo.structure.MMOEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Elixir extends MMOEnchantment {
    public Elixir() {
        super("elixir", EnchantmentType.NORMAL);
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

        double victimMana = MMOManager.getMana(pVictim);

        double quantity = (this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percentage-per-level") / 100) * level;

        double toSteal = victimMana * quantity;

        MMOManager.setMana(pVictim, victimMana - toSteal);
        MMOManager.giveMana(pAttacker, toSteal);
    }
}
