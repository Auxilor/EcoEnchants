package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.EcoRunnable;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;

public class Forcefield extends EcoEnchant implements EcoRunnable {
    public Forcefield() {
        super(
                new EcoEnchantBuilder("forcefield", EnchantmentType.NORMAL, Target.Applicable.ARMOR, 4.0)
        );
    }

    @Override
    public void run() {
        EcoEnchantsPlugin.getInstance().getServer().getOnlinePlayers().stream().filter(player -> HasEnchant.getArmorPoints(player, EcoEnchants.FORCEFIELD, false) > 0).forEach((player -> {
            int level = HasEnchant.getArmorPoints(player, EcoEnchants.FORCEFIELD, false);

            double initialDistance = EcoEnchants.FORCEFIELD.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "initial-distance");
            double bonus = EcoEnchants.FORCEFIELD.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bonus-per-level");
            double distance = initialDistance + (level * bonus);
            double damagePerPoint = EcoEnchants.FORCEFIELD.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level");
            final double damage = damagePerPoint * level;

            for(Entity e : player.getWorld().getNearbyEntities(player.getLocation(), distance, 2.0d, distance)) {
                if(!(e instanceof Monster)) continue;

                ((Monster) e).damage(damage, player);

                HasEnchant.getArmorPoints(player, EcoEnchants.FORCEFIELD, true);
            }
        }));
    }

    @Override
    public long getTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "repeat-ticks");
    }
}
