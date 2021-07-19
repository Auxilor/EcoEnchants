package com.willfp.ecoenchants.enchantments.ecoenchants.spell;

import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class Vitalize extends Spell {
    public Vitalize() {
        super("vitalize");
    }

    @Override
    public boolean onUse(@NotNull final Player player,
                         final int level,
                         @NotNull final PlayerInteractEvent event) {
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

        if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "spawn-heart-particles")) {
            spawnParticles(player);
        }

        return true;
    }

    private void spawnParticles(@NotNull final Player player) {
        Location location = player.getLocation().clone();

        location.add(0, 1, 0);

        int limit = NumberUtils.randInt(8, 13);

        for (int i = 0; i < limit; i++) {
            Location spawnLoc = location.clone();
            spawnLoc.add(
                    NumberUtils.randFloat(-1.2, 1.2),
                    NumberUtils.randFloat(-0.3, 1.2),
                    NumberUtils.randFloat(-1.2, 1.2)
            );

            spawnLoc.getWorld().spawnParticle(
                    Particle.HEART,
                    spawnLoc,
                    1
            );
        }
    }
}
