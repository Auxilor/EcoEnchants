package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.util.NumberUtils;
import com.willfp.ecoenchants.util.VectorUtils;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Infuriate extends EcoEnchant {
    public Infuriate() {
        super(
                new EcoEnchantBuilder("infuriate", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onBlock(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof LivingEntity))
            return;

        Player player = (Player) event.getEntity();

        LivingEntity victim = (LivingEntity) event.getDamager();

        if(!player.isBlocking()) return;

        if(!AntigriefManager.canInjure(player, victim)) return;

        int level;
        if (!EnchantChecks.offhand(player, this) && !EnchantChecks.mainhand(player, this)) return;
        if(EnchantChecks.offhand(player, this)) level = EnchantChecks.getOffhandLevel(player, this);
        else level = EnchantChecks.getMainhandLevel(player, this);

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");
        double finalChance = (chance * level)/100;
        if(NumberUtils.randFloat(0, 1) > finalChance) return;

        double distancePerLevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance-per-level");
        final double distance = distancePerLevel * level;

        for (Entity e : victim.getWorld().getNearbyEntities(victim.getLocation(), distance, distance, distance)) {
            if(!(e instanceof Monster)) continue;

            if(e instanceof PigZombie) {
                ((PigZombie) e).setAngry(true);
            }

            ((Monster) e).setTarget(victim);

            Vector vector = player.getLocation().toVector().clone().subtract(e.getLocation().toVector()).normalize().multiply(0.23d);

            if(VectorUtils.isFinite(vector)) {
                e.setVelocity(vector);
            }
        }
    }
}
