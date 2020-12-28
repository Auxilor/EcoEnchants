package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.util.events.armorequip.ArmorEquipEvent;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Streamlining extends EcoEnchant {
    public Streamlining() {
        super(
                "streamlining", EnchantmentType.SPECIAL
        );
    }

    @Override
    public void onArmorEquip(@NotNull final Player player,
                             final int level,
                             @NotNull final ArmorEquipEvent event) {
        if (level == 0) {
            player.setWalkSpeed(0.2f);
            return;
        }

        player.setWalkSpeed((float) (0.2 + (level * EcoEnchants.STREAMLINING.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "speed-per-level"))));
    }
}
