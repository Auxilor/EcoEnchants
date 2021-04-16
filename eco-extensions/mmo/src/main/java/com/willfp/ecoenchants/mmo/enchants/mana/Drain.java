package com.willfp.ecoenchants.mmo.enchants.mana;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.mmo.integrations.mmo.MMOManager;
import com.willfp.ecoenchants.mmo.structure.MMOSpell;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class Drain extends MMOSpell {
    public Drain() {
        super("drain");
    }

    @Override
    public boolean onUse(Player player, int level, PlayerInteractEvent event) {
        double radius = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius-per-level") * level;
        double amount = 1 - ((this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percentage-per-level") / 100) * level);

        player.getNearbyEntities(radius, radius, radius).forEach(entity -> {
            if (!(entity instanceof Player))
                return;

            Player victim = (Player) entity;
            MMOManager.setMana(victim, MMOManager.getMana(player) * amount);
        });

        return true;
    }
}
