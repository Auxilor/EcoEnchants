package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.AntiGrief;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

@SuppressWarnings("deprecation")
public class Instability extends EcoEnchant {
    public Instability() {
        super(
                new EcoEnchantBuilder("instability", EnchantmentType.SPECIAL, new Target.Applicable[]{Target.Applicable.BOW, Target.Applicable.CROSSBOW}, 4.0)
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

        if (!HasEnchant.playerHeld(player, this)) return;

        if (!(event.getEntity() instanceof Arrow)) return;

        int level = HasEnchant.getPlayerLevel(player, this);

        boolean fire = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "fire");
        boolean breakblocks = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "break-blocks");

        float power = (float) (0.5 + (level * 0.5));

        if (!AntiGrief.canCreateExplosion(player, event.getEntity().getLocation())) return;
        if (breakblocks) {
            if (!AntiGrief.canBreakBlock(player, event.getEntity().getLocation().getWorld().getBlockAt(event.getEntity().getLocation())))
                return;
        }

        event.getEntity().getWorld().createExplosion(event.getEntity().getLocation().getX(), event.getEntity().getLocation().getY(), event.getEntity().getLocation().getZ(), power, fire, breakblocks);

        event.getEntity().remove();
    }
}