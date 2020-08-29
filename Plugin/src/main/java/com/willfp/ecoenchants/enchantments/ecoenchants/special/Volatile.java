package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.checks.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Cooldown;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Volatile extends EcoEnchant {
    public Volatile() {
        super(
                new EcoEnchantBuilder("volatile", EnchantmentType.SPECIAL, Target.Applicable.SWORD, 4.0)
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

        LivingEntity victim = (LivingEntity) event.getEntity();

        if(!AntigriefManager.canInjure(player, victim)) return;

        if (!EnchantChecks.mainhand(player, this)) return;

        if (Cooldown.getCooldown(player) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged"))
            return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        if (Rand.randFloat(0, 1) > level * 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level"))
            return;

        boolean fire = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "fire");
        boolean breakblocks = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "break-blocks");

        float power = (float) (0.5 + (level * 0.5));

        if (!AntigriefManager.canCreateExplosion(player, event.getEntity().getLocation())) return;
        if (breakblocks) {
            if (!AntigriefManager.canBreakBlock(player, event.getEntity().getLocation().getWorld().getBlockAt(event.getEntity().getLocation())))
                return;
        }

        double distance = player.getLocation().distance(victim.getLocation());
        Location explosionLoc = victim.getEyeLocation();

        victim.getWorld().createExplosion(explosionLoc, power, fire, breakblocks);
    }
}
