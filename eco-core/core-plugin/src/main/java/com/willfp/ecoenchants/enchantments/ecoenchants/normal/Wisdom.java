package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.events.NaturalExpGainEvent;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public class Wisdom extends EcoEnchant {
    public Wisdom() {
        super(
                "wisdom", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onExpChange(@NotNull final NaturalExpGainEvent event) {
        Player player = event.getExpChangeEvent().getPlayer();

        if (event.getExpChangeEvent().getAmount() < 0) {
            return;
        }

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (player.getOpenInventory().getTopInventory().getType() == InventoryType.GRINDSTONE) {
            return;
        }

        int level = EnchantChecks.getMainhandLevel(player, this);

        event.getExpChangeEvent().setAmount((int) Math.ceil(event.getExpChangeEvent().getAmount() * (1 + (level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bonus-per-point")))));
    }
}
