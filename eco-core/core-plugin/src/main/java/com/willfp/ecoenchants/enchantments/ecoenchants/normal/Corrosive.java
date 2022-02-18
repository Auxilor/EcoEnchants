package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.DurabilityUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class Corrosive extends EcoEnchant {
    public Corrosive() {
        super(
                "corrosive", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onArrowDamage(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity uncastVictim,
                              @NotNull final Arrow arrow,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!(uncastVictim instanceof Player victim)) {
            return;
        }

        if (attacker instanceof Player player) {
            if (!AntigriefManager.canInjure(player, uncastVictim)) {
                return;
            }
        }

        ArrayList<ItemStack> armor = new ArrayList<>(Arrays.asList(victim.getInventory().getArmorContents()));
        if (armor.isEmpty()) {
            return;
        }

        for (ItemStack armorPiece : armor) {
            if (armorPiece == null) {
                continue;
            }

            DurabilityUtils.damageItem(victim, armorPiece, level);
        }
    }
}
