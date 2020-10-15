package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Paralyze extends EcoEnchant {
    public Paralyze() {
        super(
                new EcoEnchantBuilder("paralyze", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS


    @Override
    public void onDeflect(Player blocker, LivingEntity attacker, int level, EntityDamageByEntityEvent event) {
        int duration = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level");

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        int finalDuration = duration * level;

        attacker.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, finalDuration, 10, false, false, false));
    }
}
