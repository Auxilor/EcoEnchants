package com.willfp.ecoenchants.mmo.enchants.stamina;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.mmo.structure.MMOEnchantment;
import net.Indyuce.mmocore.api.event.PlayerRegenResourceEvent;
import net.Indyuce.mmocore.api.player.profess.resource.PlayerResource;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Endurance extends MMOEnchantment {
    public Endurance() {
        super("endurance", EnchantmentType.NORMAL);
    }

    @EventHandler
    public void onRegainMana(PlayerRegenResourceEvent event) {
        if (!event.getResource().equals(PlayerResource.STAMINA))
            return;

        Player player = event.getPlayer();

        int levels = EnchantChecks.getArmorPoints(player, this);
        if (levels == 0) return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier") * levels;
        event.setAmount(event.getAmount() * (multiplier + 1));
    }
}
