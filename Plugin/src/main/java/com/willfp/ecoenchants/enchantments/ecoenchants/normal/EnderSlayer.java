package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashSet;
import java.util.Set;
public final class EnderSlayer extends EcoEnchant {
    public EnderSlayer() {
        super(
                "ender_slayer", EnchantmentType.NORMAL
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
