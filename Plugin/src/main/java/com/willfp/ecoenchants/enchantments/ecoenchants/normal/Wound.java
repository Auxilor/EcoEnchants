package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class Wound extends EcoEnchant {
    public Wound() {
        super(
                new EcoEnchantBuilder("wound", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {
        if (NumberUtils.randFloat(0, 1) > level * 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level"))
            return;

        double bleedDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bleed-damage");

        int bleedCount = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "amount-per-level");
        bleedCount *= level;
        final int finalBleedCount = bleedCount;

        AtomicInteger currentBleedCount = new AtomicInteger(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                currentBleedCount.addAndGet(1);

                victim.damage(bleedDamage);

                if(currentBleedCount.get() >= finalBleedCount) this.cancel();
            }
        }.runTaskTimer(EcoEnchantsPlugin.getInstance(), 0, 10);
    }
}
