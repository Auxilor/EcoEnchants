package com.willfp.ecoenchants.enchantments.ecoenchants.spell;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Charge extends Spell {
    public Charge() {
        super("charge");
    }

    @Override
    public boolean onUse(@NotNull final Player player,
                         final int level,
                         @NotNull final PlayerInteractEvent event) {
        AnticheatManager.exemptPlayer(player);

        Vector velocity = player.getEyeLocation().getDirection().clone();
        velocity.normalize();
        velocity.multiply(level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity-per-level"));
        velocity.setY(player.getEyeLocation().getDirection().clone().getY() + 0.2);
        player.setVelocity(safenVector(velocity));

        this.getPlugin().getScheduler().runLater(() -> AnticheatManager.unexemptPlayer(player), 10);

        return true;
    }

    private Vector safenVector(@NotNull final Vector vector) {
        if (Math.abs(vector.getX()) > 4) {
            vector.setX(vector.getX() < 0 ? -3.9 : 3.9);
        }

        if (Math.abs(vector.getY()) > 4) {
            vector.setY(vector.getY() < 0 ? -3.9 : 3.9);
        }

        if (Math.abs(vector.getZ()) > 4) {
            vector.setZ(vector.getZ() < 0 ? -3.9 : 3.9);
        }

        return vector;
    }
}
