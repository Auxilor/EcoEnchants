package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("deprecation")
public class Frozen extends EcoEnchant {
    public Frozen() {
        super(
                new EcoEnchantBuilder("frozen", EnchantmentType.NORMAL, Target.Applicable.ARMOR, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHurt(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof LivingEntity))
            return;

        Player player = (Player) event.getEntity();
        LivingEntity victim = (LivingEntity) event.getDamager();

        final int points = HasEnchant.getArmorPoints(player, this, true);

        if (points == 0)
            return;

        if (Rand.randFloat(0, 1) > points * 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-point"))
            return;

        int divisor = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "points-per-level");
        final int level = (int) Math.ceil((double) points / divisor);

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, points * 5, level));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, points * 5, level));
        }, 1);
    }
}
