package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FragilityCurse extends EcoEnchant {
    public FragilityCurse() {
        super(
                "fragility_curse", EnchantmentType.CURSE
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return NumberUtils.format((
                this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "minimum-extra-durability")
                        + this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "maximum-extra-durability")
        ) / 2D);
    }

    @EventHandler
    public void onItemDamage(@NotNull final PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();

        if (!EnchantChecks.item(item, this)) {
            return;
        }

        if (!this.areRequirementsMet(event.getPlayer())) {
            return;
        }

        if (this.getDisabledWorlds().contains(event.getPlayer().getWorld())) {
            return;
        }

        int min = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "minimum-extra-durability");
        int max = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "maximum-extra-durability");

        event.setDamage(event.getDamage() * NumberUtils.randInt(min, max));
    }
}
