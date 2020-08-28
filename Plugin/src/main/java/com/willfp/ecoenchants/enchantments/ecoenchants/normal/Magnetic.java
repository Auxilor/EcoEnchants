package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.task.EcoRunnable;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.LocationUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class Magnetic extends EcoEnchant implements EcoRunnable {
    public Magnetic() {
        super(
                new EcoEnchantBuilder("magnetic", EnchantmentType.NORMAL, Target.Applicable.BOOTS, 4.0)
        );
    }

    @Override
    public void run() {
        Main.getInstance().getServer().getOnlinePlayers().stream().filter(player -> HasEnchant.getArmorPoints(player, EcoEnchants.MAGNETIC, false) > 0).forEach((player -> {
            int level = HasEnchant.getArmorPoints(player, EcoEnchants.MAGNETIC, false);

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

                if(LocationUtils.isFinite(vector)) {
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
