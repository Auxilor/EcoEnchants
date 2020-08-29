package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Cooldown;
import com.willfp.ecoenchants.nms.Target;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Razor extends EcoEnchant {
    public Razor() {
        super(
                new EcoEnchantBuilder("razor", EnchantmentType.SPECIAL, Target.Applicable.SWORD, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void razorHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;

        Player player = (Player) event.getDamager();

        LivingEntity victim = (LivingEntity) event.getEntity();

        if (!EnchantChecks.mainhand(player, this)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        double perLevelMultiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "per-level-multiplier");
        double baseDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "base-damage");
        double extra = level*perLevelMultiplier + baseDamage;
        if(this.getConfig().getBool((EcoEnchants.CONFIG_LOCATION) + "decrease-if-cooldown")) {
            extra *= Cooldown.getCooldown(player);
        }

        event.setDamage(event.getDamage() + extra);
    }
}
