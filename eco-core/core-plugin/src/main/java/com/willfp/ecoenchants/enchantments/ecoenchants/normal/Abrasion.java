package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.DurabilityUtils;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class Abrasion extends EcoEnchant {
    public Abrasion() {
        super(
                "abrasion", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return NumberUtils.format(level);
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity uncastVictim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!(uncastVictim instanceof Player victim)) {
            return;
        }

        if (!EnchantmentUtils.isFullyChargeIfRequired(this, attacker)) {
            return;
        }

        ArrayList<ItemStack> armor = new ArrayList<>(Arrays.asList(victim.getInventory().getArmorContents()));
        if (armor.isEmpty()) {
            return;
        }

        for (ItemStack armorPiece : armor) {
            if (armorPiece == null) {
                continue;
            }

            if (armorPiece.equals(victim.getInventory().getHelmet())) {
                DurabilityUtils.damageItem(victim, victim.getInventory().getHelmet(), level, 39);
            }
            if (armorPiece.equals(victim.getInventory().getChestplate())) {
                DurabilityUtils.damageItem(victim, victim.getInventory().getChestplate(), level, 38);
            }
            if (armorPiece.equals(victim.getInventory().getLeggings())) {
                DurabilityUtils.damageItem(victim, victim.getInventory().getLeggings(), level, 37);
            }
            if (armorPiece.equals(victim.getInventory().getBoots())) {
                DurabilityUtils.damageItem(victim, victim.getInventory().getBoots(), level, 36);
            }
        }
    }
}
