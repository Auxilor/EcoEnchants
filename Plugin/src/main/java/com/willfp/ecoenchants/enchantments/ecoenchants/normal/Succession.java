package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.ItemDurability;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
public class Succession extends EcoEnchant {
    public Succession() {
        super(
                new EcoEnchantBuilder("succession", EnchantmentType.NORMAL, Target.Applicable.BOW, 4.01)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onSuccessionShoot(EntityShootBowEvent event) {
        if (event.getProjectile().getType() != EntityType.ARROW)
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (!EnchantChecks.mainhand(player, this)) return;

        int number = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "extra-arrows");

        boolean fire = EnchantChecks.mainhand(player, Enchantment.ARROW_FIRE);


        for (int i = 1; i <= number; i++) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(EcoEnchantsPlugin.getInstance(), () -> {
                Arrow arrow = player.launchProjectile(Arrow.class, event.getProjectile().getVelocity());
                arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                if(fire) arrow.setFireTicks(Integer.MAX_VALUE);

                if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "per-arrow-damage")) {
                    ItemDurability.damageItem(player, player.getInventory().getItemInMainHand(), 1, player.getInventory().getHeldItemSlot());
                }
            }, i * 2);
        }
    }
}
