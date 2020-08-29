package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class WaterAffinity extends EcoEnchant {
    public WaterAffinity() {
        super(
                new EcoEnchantBuilder("water_affinity", EnchantmentType.NORMAL, Target.Applicable.SWORD, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;

        Player player = (Player) event.getDamager();

        if (!EnchantChecks.mainhand(player, this)) return;

        if(!player.getLocation().getBlock().getType().equals(Material.WATER))
            return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-more-per-level");

        double finalMultiplier = (multiplier/100 * level) + 1;

        event.setDamage(event.getDamage() * finalMultiplier);
    }
}
