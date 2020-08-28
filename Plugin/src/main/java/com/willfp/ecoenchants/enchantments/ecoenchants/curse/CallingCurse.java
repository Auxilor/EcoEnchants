package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.task.EcoRunnable;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.LocationUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class CallingCurse extends EcoEnchant implements EcoRunnable {
    public CallingCurse() {
        super(
                new EcoEnchantBuilder("calling_curse", EnchantmentType.CURSE, new Target.Applicable[]{Target.Applicable.ARMOR}, 4.0)
        );
    }

    @Override
    public void run() {
        Main.getInstance().getServer().getOnlinePlayers().stream().filter(player -> HasEnchant.getArmorPoints(player, EcoEnchants.CALLING_CURSE, false) > 0).forEach((player -> {
            double distance = EcoEnchants.CALLING_CURSE.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance");

            for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), distance, distance, distance)) {
                if(!(e instanceof Monster)) continue;

                if(e instanceof PigZombie) {
                    ((PigZombie) e).setAngry(true);
                }

                ((Monster) e).setTarget(player);

                Vector vector = player.getLocation().toVector().clone().subtract(e.getLocation().toVector()).normalize().multiply(0.23d);

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
