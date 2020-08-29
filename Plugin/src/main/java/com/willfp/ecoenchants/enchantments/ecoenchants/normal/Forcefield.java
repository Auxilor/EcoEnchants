package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.EcoRunnable;
import com.willfp.ecoenchants.enchantments.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
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
        EcoEnchantsPlugin.getInstance().getServer().getOnlinePlayers().stream().filter(player -> EnchantChecks.getArmorPoints(player, EcoEnchants.FORCEFIELD, 0) > 0).forEach((player -> {
            int level = EnchantChecks.getArmorPoints(player, EcoEnchants.FORCEFIELD, 0);

            double initialDistance = EcoEnchants.FORCEFIELD.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "initial-distance");
            double bonus = EcoEnchants.FORCEFIELD.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bonus-per-level");
            double distance = initialDistance + (level * bonus);
            double damagePerPoint = EcoEnchants.FORCEFIELD.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level");
            final double damage = damagePerPoint * level;

            for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), distance, 2.0d, distance)) {
                if(!(e instanceof Monster)) continue;

                ((Monster) e).damage(damage, player);

                EnchantChecks.getArmorPoints(player, EcoEnchants.FORCEFIELD, 1);
            }
        }));
    }

    @Override
    public long getTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "repeat-ticks");
    }
}
