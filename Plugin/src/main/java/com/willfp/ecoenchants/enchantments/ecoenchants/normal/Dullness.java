package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.nms.Cooldown;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
public final class Dullness extends EcoEnchant {
    public Dullness() {
        super(
                new EcoEnchantBuilder("dullness", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS

    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(attacker instanceof Player) {
            if (Cooldown.getCooldown((Player) attacker) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged"))
                return;
        }


        if(!EnchantmentUtils.passedChance(this, level))
            return;

        int durationPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "duration-per-level");

        victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, level * durationPerLevel, level));
    }
}
