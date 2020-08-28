package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Cooldown;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

@SuppressWarnings("deprecation")
public class Cleave extends EcoEnchant {
    public Cleave() {
        super(
                new EcoEnchantBuilder("cleave", EnchantmentType.NORMAL, Target.Applicable.AXE, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;

        Player player = (Player) event.getDamager();

        LivingEntity victim = (LivingEntity) event.getEntity();

        if(victim.hasMetadata("cleaved"))
            return;

        if(!AntigriefManager.canInjure(player, victim)) return;

        if (!HasEnchant.playerHeld(player, this)) return;

        if (Cooldown.getCooldown(player) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged"))
            return;

        int level = HasEnchant.getPlayerLevel(player, this);

        double damagePerLevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-percentage-per-level") * 0.01;
        double radiusPerLevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius-per-level");
        final double damage = damagePerLevel * level * event.getDamage();
        final double radius = radiusPerLevel * level;

        victim.getNearbyEntities(radius, radius, radius).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> !entity.equals(player))
                .forEach(entity -> {
                    entity.setMetadata("cleaved", new FixedMetadataValue(Main.getInstance(), true));
                    ((LivingEntity) entity).damage(damage, player);
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> entity.removeMetadata("cleaved", Main.getInstance()), 5);
                });
    }
}
