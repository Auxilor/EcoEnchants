package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.TridentStack;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
public class Aquatic extends EcoEnchant {
    public Aquatic() {
        super(
                new EcoEnchantBuilder("aquatic", EnchantmentType.NORMAL, Target.Applicable.TRIDENT, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Trident))
            return;

        if(!(((Trident) event.getDamager()).getShooter() instanceof Player))
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (event.isCancelled())
            return;

        Player player = (Player) ((Trident) event.getDamager()).getShooter();
        Trident trident = (Trident) event.getDamager();
        ItemStack item = TridentStack.getTridentStack(trident);

        if(!player.getLocation().getBlock().getType().equals(Material.WATER))
            return;

        if (!EnchantChecks.item(item, this)) return;

        int level = EnchantChecks.getItemLevel(item, this);

        double perLevelDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-more-per-level");

        double totalDamagePercent = (100 + (perLevelDamage * level))/100;

        event.setDamage(event.getDamage() * totalDamagePercent);
    }
}
