package com.willfp.ecoenchants.mmo.enchants;

import com.willfp.ecoenchants.events.armorequip.ArmorEquipEvent;
import com.willfp.ecoenchants.mmo.MMOEnchantment;
import org.bukkit.entity.Player;

public class Augment extends MMOEnchantment {
    public Augment() {
        super("augment", EnchantmentType.NORMAL);
    }

    @Override
    public void onArmorEquip(Player player, int level, ArmorEquipEvent event) {
        
    }
}
