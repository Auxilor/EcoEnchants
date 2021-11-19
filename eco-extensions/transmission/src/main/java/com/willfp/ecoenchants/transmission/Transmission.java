package com.willfp.ecoenchants.transmission;


import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Transmission extends Spell {
    public Transmission() {
        super("transmission");
    }

    @Override
    public boolean onUse(@NotNull final Player player,
                      final int level,
                      @NotNull final PlayerInteractEvent event) {
        double distance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance");

        Vector vector = player.getEyeLocation().getDirection().clone();
        vector.normalize();
        vector.multiply(distance);

        Location end = player.getEyeLocation().clone().add(vector);

        if (player.rayTraceBlocks(distance) != null) {
            player.sendMessage(this.getPlugin().getLangYml().getPrefix() + this.getConfig().getFormattedString(EcoEnchants.CONFIG_LOCATION + "block-in-path"));
            return false;
        }

        player.teleport(end);

        return true;
    }
}
