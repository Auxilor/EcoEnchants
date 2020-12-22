package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Frozen extends EcoEnchant {
    public Frozen() {
        super(
                "frozen", EnchantmentType.NORMAL
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

        final int points = EnchantChecks.getArmorPoints(player, this, 0);

        if (points == 0)
            return;
        if (this.getDisabledWorlds().contains(player.getWorld())) return;


        if (!EnchantmentUtils.passedChance(this, points))
            return;

        int divisor = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "points-per-level");
        final int level = (int) Math.ceil((double) points / divisor);

        this.getPlugin().getScheduler().runLater(() -> {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, points * 5, level));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, points * 5, level));
        }, 1);
    }
}
