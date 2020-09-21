package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class WaterAspect extends EcoEnchant {
    public WaterAspect() {
        super(
                new EcoEnchantBuilder("water_aspect", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onWaterAspectDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        if (!(event.getEntity() instanceof LivingEntity)) return;

        Player player = (Player) event.getDamager();
        LivingEntity victim = (LivingEntity) event.getEntity();

        if(!(victim instanceof Blaze || victim instanceof MagmaCube || victim instanceof Enderman))
            return;

        if (!EnchantChecks.mainhand(player, this)) return;
        int level = EnchantChecks.getMainhandLevel(player, this);

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");

        double damageMultiplier = (level * multiplier) + 1;

        event.setDamage(event.getDamage() * damageMultiplier);
    }
}