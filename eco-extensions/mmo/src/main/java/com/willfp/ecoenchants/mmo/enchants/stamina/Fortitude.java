package com.willfp.ecoenchants.mmo.enchants.stamina;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.mmo.integrations.mmo.MMOManager;
import com.willfp.ecoenchants.mmo.structure.MMOEnchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Fortitude extends MMOEnchantment {
    public Fortitude() {
        super("fortitude", EnchantmentType.NORMAL);
    }

    @Override
    public void onArrowDamage(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, @NotNull Arrow arrow, int level, @NotNull EntityDamageByEntityEvent event) {
        if (!(attacker instanceof Player && victim instanceof Player))
            return;
        Player pAttacker = (Player) attacker;
        Player pVictim = (Player) victim;

        double victimStamina = MMOManager.getStamina(pVictim);

        double quantity = (this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percentage-per-level") / 100) * level;

        double toSteal = victimStamina * quantity;

        MMOManager.setStamina(pVictim, victimStamina - toSteal);
        MMOManager.giveStamina(pAttacker, toSteal);
    }
}
