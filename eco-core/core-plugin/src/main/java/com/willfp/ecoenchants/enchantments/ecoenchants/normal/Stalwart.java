package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Stalwart extends EcoEnchant {
    public Stalwart() {
        super(
                "stalwart", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {
        if(!EnchantmentUtils.passedChance(this, level))
            return;

        int duration = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "duration-per-level");
        duration *= level;

        int amplifier = (int) Math.ceil((double) level / 4) - 1;

        victim.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, amplifier, false, false, true));

        event.setCancelled(true);
    }
}
