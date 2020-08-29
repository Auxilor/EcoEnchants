package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.nms.TridentStack;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class Inferno extends EcoEnchant {
    public Inferno() {
        super(
                new EcoEnchantBuilder("inferno", EnchantmentType.NORMAL, Target.Applicable.TRIDENT, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onInfernoShoot(ProjectileLaunchEvent event) {
        if(!(event.getEntity() instanceof Trident))
            return;

        if(!(event.getEntity().getShooter() instanceof Player))
            return;

        Player player = (Player) event.getEntity().getShooter();
        Trident trident = (Trident) event.getEntity();
        ItemStack item = TridentStack.getTridentStack(trident);

        if(!HasEnchant.item(item, this)) return;

        trident.setFireTicks(Integer.MAX_VALUE);
    }

    @EventHandler
    public void onInfernoShoot(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Trident))
            return;

        if(!(event.getEntity() instanceof LivingEntity))
            return;

        Trident trident = (Trident) event.getDamager();
        ItemStack item = TridentStack.getTridentStack(trident);
        LivingEntity victim = (LivingEntity) event.getEntity();

        if(!HasEnchant.item(item, this)) return;
        if(trident.getFireTicks() <= 0) return;

        victim.setFireTicks(100);
    }
}
