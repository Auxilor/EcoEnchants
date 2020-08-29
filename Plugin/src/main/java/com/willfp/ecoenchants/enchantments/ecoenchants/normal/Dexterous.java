package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
public class Dexterous extends EcoEnchant {
    public Dexterous() {
        super(
                new EcoEnchantBuilder("dexterous", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.SWORD, Target.Applicable.AXE}, 4.0)
        );
    }

    // START OF LISTENERS


    @EventHandler
    public void onDextHold(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if (!EnchantChecks.item(item, this)) {
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
            return;
        }


        int level = EnchantChecks.getItemLevel(item, this);
        double bonus = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "add-speed-per-level");
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0 + (level * bonus));
    }
}
