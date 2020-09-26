package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.util.ItemDurability;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
public class Succession extends EcoEnchant {
    public Succession() {
        super(
                new EcoEnchantBuilder("succession", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onBowShoot(LivingEntity shooter, Arrow arrow, int level, EntityShootBowEvent event) {
        int number = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "extra-arrows");

        boolean fire = EnchantChecks.mainhand(shooter, Enchantment.ARROW_FIRE);


        for (int i = 1; i <= number; i++) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(EcoEnchantsPlugin.getInstance(), () -> {
                Arrow arrow1 = shooter.launchProjectile(Arrow.class, event.getProjectile().getVelocity());
                arrow1.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                if(fire) arrow1.setFireTicks(Integer.MAX_VALUE);

                if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "per-arrow-damage")) {
                    if(shooter instanceof Player) {
                        ItemDurability.damageItem((Player) shooter, ((Player) shooter).getInventory().getItemInMainHand(), 1, ((Player) shooter).getInventory().getHeldItemSlot());
                    }
                }
            }, i * 2);
        }
    }
}
