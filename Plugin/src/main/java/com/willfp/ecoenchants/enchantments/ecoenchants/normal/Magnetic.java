package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.util.EcoRunnable;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.util.VectorUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;
public class Magnetic extends EcoEnchant implements EcoRunnable {
    public Magnetic() {
        super(
                new EcoEnchantBuilder("magnetic", EnchantmentType.NORMAL, 5.0)
        );
    }

    @Override
    public void run() {
        EcoEnchantsPlugin.getInstance().getServer().getOnlinePlayers().stream().filter(player -> EnchantChecks.getArmorPoints(player, EcoEnchants.MAGNETIC, 0) > 0).forEach((player -> {
            int level = EnchantChecks.getArmorPoints(player, EcoEnchants.MAGNETIC, 0);

            double initialDistance = EcoEnchants.MAGNETIC.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "initial-distance");
            double bonus = EcoEnchants.MAGNETIC.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bonus-per-level");
            double distance = initialDistance + (level * bonus);

            for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), distance, 2.0d, distance)) {
                if(!(e instanceof Item || e instanceof ExperienceOrb)) continue;

                if (e instanceof Item) {
                    if (((Item) e).getPickupDelay() > 0) {
                        continue;
                    }
                }

                Vector vector = player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.1 * level);

                if(VectorUtils.isFinite(vector)) {
                    e.setVelocity(vector);
                }
            }
        }));
    }

    @Override
    public long getTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "repeat-ticks");
    }
}
