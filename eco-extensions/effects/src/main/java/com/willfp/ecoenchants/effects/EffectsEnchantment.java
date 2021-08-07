package com.willfp.ecoenchants.effects;

import com.willfp.eco.core.Prerequisite;
import com.willfp.eco.core.events.ArmorChangeEvent;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public abstract class EffectsEnchantment extends EcoEnchant {
    protected EffectsEnchantment(@NotNull final String key,
                                 @NotNull final EnchantmentType type,
                                 @NotNull final Prerequisite... prerequisites) {
        super(key, type, prerequisites);
    }

    public abstract PotionEffectType getPotionEffect();

    @EventHandler
    public void onEquip(@NotNull final ArmorChangeEvent event) {
        final Player player = event.getPlayer();

        if (player.hasPotionEffect(this.getPotionEffect()) && player.getPotionEffect(this.getPotionEffect()).getDuration() >= 1639) {
            player.removePotionEffect(this.getPotionEffect());
        }

        int level = EnchantChecks.getArmorPoints(player, this);
        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }
        if (level > 0) {
            player.addPotionEffect(new PotionEffect(this.getPotionEffect(), 0x6fffffff, level - 1, false, false, true));
        }
    }
}
