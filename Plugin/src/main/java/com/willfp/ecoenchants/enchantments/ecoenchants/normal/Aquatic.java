package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
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
                new EcoEnchantBuilder("aquatic", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onTridentDamage(LivingEntity attacker, LivingEntity victim, Trident trident, int level, EntityDamageByEntityEvent event) {
        if(!attacker.getLocation().getBlock().getType().equals(Material.WATER))
            return;

        double perLevelDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-more-per-level");

        double totalDamagePercent = (100 + (perLevelDamage * level))/100;

        event.setDamage(event.getDamage() * totalDamagePercent);
    }
}
