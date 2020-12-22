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
public class Buckshot extends EcoEnchant {
    public Buckshot() {
        super(
                "buckshot", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @Override
    public void onBowShoot(LivingEntity shooter, Arrow arrow, int level, EntityShootBowEvent event) {
        event.getProjectile().remove();
        if(shooter instanceof Player) {
            ((Player) shooter).playSound(shooter.getLocation(), Sound.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }

        int numberPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "amount-per-level");
        int number = numberPerLevel * level;
        double spread = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "spread-per-level");
        spread *= level;

        for (int i = 0; i < number; i += 1) {

            Vector velocity = event.getProjectile().getVelocity().clone();

            velocity.add(new Vector(NumberUtils.randFloat(-spread, spread), NumberUtils.randFloat(-spread, spread), NumberUtils.randFloat(-spread, spread)));

            Arrow arrow1 = shooter.launchProjectile(Arrow.class, velocity);
            if(EnchantChecks.mainhand(shooter, Enchantment.ARROW_FIRE)) arrow1.setFireTicks(Integer.MAX_VALUE);
            if(EnchantChecks.mainhand(shooter, EcoEnchants.MARKSMAN)) arrow1.setGravity(false);
            arrow1.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        }
    }
}
