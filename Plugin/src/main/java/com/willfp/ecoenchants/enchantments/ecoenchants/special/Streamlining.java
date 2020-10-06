package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.events.armorequip.ArmorEquipEvent;
import org.bukkit.entity.Player;
public final class Streamlining extends EcoEnchant {
    public Streamlining() {
        super(
                new EcoEnchantBuilder("streamlining", EnchantmentType.SPECIAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onArmorEquip(Player player, int level, ArmorEquipEvent event) {
        if(level == 0) {
            player.setWalkSpeed(0.2f);
            return;
        }

        player.setWalkSpeed((float) (0.2 + (level * EcoEnchants.STREAMLINING.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "speed-per-level"))));
    }
}
