package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
public class Frozen extends EcoEnchant {
    public Frozen() {
        super(
                new EcoEnchantBuilder("frozen", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {
        if (NumberUtils.randFloat(0, 1) > level * 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-point"))
            return;

        int divisor = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "points-per-level");
        final int amplifier = (int) Math.ceil((double) level / divisor);

        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, level * 5, amplifier));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, level * 5, amplifier));
        }, 1);
    }
}
