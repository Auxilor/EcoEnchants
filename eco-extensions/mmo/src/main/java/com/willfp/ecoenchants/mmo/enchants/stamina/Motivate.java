package com.willfp.ecoenchants.mmo.enchants.stamina;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.mmo.integrations.mmo.MMOManager;
import com.willfp.ecoenchants.mmo.structure.MMOEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Motivate extends MMOEnchantment {
    public Motivate() {
        super("motivate", EnchantmentType.NORMAL);
    }

    @Override
    public void onMeleeAttack(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, int level, @NotNull EntityDamageByEntityEvent event) {
        if (!(attacker instanceof Player && victim instanceof Player))
            return;
        Player pAttacker = (Player) attacker;
        Player pVictim = (Player) victim;

        if (!EnchantmentUtils.isFullyChargeIfRequired(this, attacker)) {
            return;
        }

        double victimStamina = MMOManager.getStamina(pVictim);

        double quantity = (this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percentage-per-level") / 100) * level;

        double toSteal = victimStamina * quantity;

        MMOManager.setStamina(pVictim, victimStamina - toSteal);
        MMOManager.giveStamina(pAttacker, toSteal);
    }
}
