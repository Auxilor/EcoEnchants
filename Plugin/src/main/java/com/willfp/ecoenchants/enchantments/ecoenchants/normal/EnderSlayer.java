package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashSet;
import java.util.Set;
public class EnderSlayer extends EcoEnchant {
    public EnderSlayer() {
        super(
                new EcoEnchantBuilder("ender_slayer", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    private static Set<EntityType> endMobs = new HashSet<EntityType>() {{
        add(EntityType.ENDERMITE);
        add(EntityType.ENDERMAN);
        add(EntityType.ENDER_DRAGON);
        add(EntityType.SHULKER);
    }};

    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if (!endMobs.contains(victim.getType()))
            return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bonus-per-level");

        event.setDamage(event.getDamage() + (level * multiplier));
    }
}
