package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Butchering extends EcoEnchant {
    public Butchering() {
        super(
                new EcoEnchantBuilder("butchering", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.SWORD, Target.Applicable.AXE}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void butcheringHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        if (event.getEntity() instanceof Monster)
            return;

        Player player = (Player) event.getDamager();

        if (!EnchantChecks.mainhand(player, this)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bonus-per-level");

        event.setDamage(event.getDamage() + (level * multiplier));
    }
}
