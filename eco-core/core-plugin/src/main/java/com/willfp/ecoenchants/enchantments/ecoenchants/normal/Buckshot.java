package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Buckshot extends EcoEnchant {
    public Buckshot() {
        super(
                "buckshot", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onBowShoot(@NotNull final LivingEntity shooter,
                           @NotNull final Arrow arrow,
                           final int level,
                           @NotNull final EntityShootBowEvent event) {
        event.getProjectile().remove();
        if (shooter instanceof Player) {
            ((Player) shooter).playSound(shooter.getLocation(), Sound.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }

        int number = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "amount-per-level");
        number *= level;

        double spread = Math.abs(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "spread-per-level"));
        spread *= level;

        for (int i = 0; i < number; i++) {
            Vector velocity = event.getProjectile().getVelocity().clone();

            velocity.add(new Vector(NumberUtils.randFloat(-spread, spread), NumberUtils.randFloat(-spread, spread), NumberUtils.randFloat(-spread, spread)));

            Arrow arrow1 = shooter.launchProjectile(Arrow.class, velocity);
            if (EnchantChecks.mainhand(shooter, Enchantment.ARROW_FIRE)) {
                arrow1.setFireTicks(Integer.MAX_VALUE);
            }
            if (EnchantChecks.mainhand(shooter, EcoEnchants.MARKSMAN)) {
                arrow1.setGravity(false);
            }
            arrow1.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        }
    }
}
