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
public final class StrayAspect extends EcoEnchant {
    public StrayAspect() {
        super(
                new EcoEnchantBuilder("stray_aspect", EnchantmentType.NORMAL)
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

        victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, level * 10, level));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, level * 10, level));
    }
}
