package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
public final class Instability extends EcoEnchant {
    public Instability() {
        super(
                new EcoEnchantBuilder("instability", EnchantmentType.SPECIAL)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onInstabilityLand(ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.ARROW)
            return;

        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        Player player = (Player) event.getEntity().getShooter();

        if (!EnchantChecks.mainhand(player, this)) return;

        if (!(event.getEntity() instanceof Arrow)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        boolean fire = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "fire");
        boolean breakblocks = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "break-blocks");

        float power = (float) (0.5 + (level * 0.5));

        if (!AntigriefManager.canCreateExplosion(player, event.getEntity().getLocation())) return;
        if (breakblocks) {
            if (!AntigriefManager.canBreakBlock(player, event.getEntity().getLocation().getWorld().getBlockAt(event.getEntity().getLocation())))
                return;
        }

        event.getEntity().getWorld().createExplosion(event.getEntity().getLocation().getX(), event.getEntity().getLocation().getY(), event.getEntity().getLocation().getZ(), power, fire, breakblocks);

        event.getEntity().remove();
    }
}