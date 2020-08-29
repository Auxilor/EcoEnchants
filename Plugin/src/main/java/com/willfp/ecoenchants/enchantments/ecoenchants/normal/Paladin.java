package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Paladin extends EcoEnchant {
    public Paladin() {
        super(
                new EcoEnchantBuilder("paladin", EnchantmentType.NORMAL, Target.Applicable.SWORD, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void paladinHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;

        Player player = (Player) event.getDamager();

        LivingEntity victim = (LivingEntity) event.getEntity();

        if (!EnchantChecks.mainhand(player, this)) return;

        if(!(player.getVehicle() instanceof Horse)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        event.setDamage(event.getDamage() * ((level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level")) + 1));
    }
}
