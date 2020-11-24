package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
public class Flinch extends EcoEnchant {
    public Flinch() {
        super(
                "flinch", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onDeflect(Player blocker, LivingEntity attacker, int level, EntityDamageByEntityEvent event) {
        int duration = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level");

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        int finalDuration = duration * level;

        attacker.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, finalDuration, 1, false, false, false));
    }
}
