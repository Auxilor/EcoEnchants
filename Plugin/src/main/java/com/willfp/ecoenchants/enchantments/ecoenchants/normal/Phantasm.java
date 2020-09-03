package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.TridentStack;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Trident;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Phantasm extends EcoEnchant {
    public Phantasm() {
        super(
                new EcoEnchantBuilder("phantasm", EnchantmentType.NORMAL, Target.Applicable.TRIDENT, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Trident))
            return;

        if(!(event.getEntity() instanceof Zombie || event.getEntity() instanceof Skeleton)) return;

        Trident trident = (Trident) event.getDamager();
        ItemStack item = TridentStack.getTridentStack(trident);

        if(!EnchantChecks.item(item, this))
            return;

        int level = EnchantChecks.getItemLevel(item, this);

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double bonus = (multiplier * (level + 1)) + 1;
        event.setDamage(damage * bonus);
    }
}
