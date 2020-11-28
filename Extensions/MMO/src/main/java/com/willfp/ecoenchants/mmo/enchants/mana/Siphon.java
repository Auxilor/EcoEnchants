package com.willfp.ecoenchants.mmo.enchants.mana;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.mmo.MMOEnchantment;
import com.willfp.ecoenchants.mmo.integrations.mmo.MMOManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Siphon extends MMOEnchantment {
    public Siphon() {
        super("siphon", EnchantmentType.NORMAL);
    }

    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {
        if(!(attacker instanceof Player && victim instanceof Player))
            return;
        Player pAttacker = (Player) attacker;
        Player pVictim = (Player) victim;

        double victimMana = MMOManager.getMana(pVictim);

        double quantity = (this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percentage-per-level") / 100) * level;

        double toSteal = victimMana * quantity;

        MMOManager.setMana(pVictim, victimMana - toSteal);
        MMOManager.giveMana(pAttacker, toSteal);
    }
}
