package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Horde extends EcoEnchant {
    public Horde() {
        super(
                new EcoEnchantBuilder("horde", EnchantmentType.NORMAL, Target.Applicable.SWORD, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        Player player = (Player) event.getDamager();

        if (!EnchantChecks.mainhand(player, this)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        double distance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance-per-level") * level;

        int entitiesNearby = (int) player.getNearbyEntities(distance, distance, distance).stream().filter(entity -> entity instanceof LivingEntity).count();

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier-per-level");
        multiplier = (1 + (level * multiplier * entitiesNearby));

        event.setDamage(event.getDamage() * multiplier);
    }
}
