package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Dexterous extends EcoEnchant {
    public Dexterous() {
        super(
                "dexterous", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onDextHold(@NotNull final PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if (!EnchantChecks.item(item, this)) {
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0);
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        int level = EnchantChecks.getItemLevel(item, this);
        double bonus = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "add-speed-per-level");
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.0 + (level * bonus));
    }
}
