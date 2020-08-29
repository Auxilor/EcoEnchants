package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class FragilityCurse extends EcoEnchant {
    public FragilityCurse() {
        super(
                new EcoEnchantBuilder("fragility_curse", EnchantmentType.CURSE, new Target.Applicable[]{Target.Applicable.ALL}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();

        if(!HasEnchant.item(item, this)) return;

        int min = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "minimum-extra-durability");
        int max = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "maximum-extra-durability");

        event.setDamage(event.getDamage() * Rand.randInt(min, max));
    }
}
