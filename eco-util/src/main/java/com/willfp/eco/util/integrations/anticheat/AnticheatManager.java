package com.willfp.eco.util.integrations.anticheat;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class AnticheatManager {
    /**
     * The linked {@link AbstractEcoPlugin} to register anticheat listeners to.
     */
    private final AbstractEcoPlugin plugin = AbstractEcoPlugin.getInstance();

    /**
     * A set of all registered anticheats.
     */
    private final Set<AnticheatWrapper> anticheats = new HashSet<>();

    /**
     * Register a new anticheat.
     *
     * @param anticheat The anticheat to register.
     */
    public void register(@NotNull final AnticheatWrapper anticheat) {
        if (anticheat instanceof Listener) {
            plugin.getEventManager().registerListener((Listener) anticheat);
        }
        anticheats.add(anticheat);
    }

    /**
     * Exempt a player from triggering anticheats.
     *
     * @param player The player to exempt.
     */
    public void exemptPlayer(@NotNull final Player player) {
        anticheats.forEach(anticheat -> anticheat.exempt(player));
    }

    /**
     * Unexempt a player from triggering anticheats.
     * This is ran a tick after it is called to ensure that there are no event timing conflicts.
     *
     * @param player The player to remove the exemption.
     */
    public void unexemptPlayer(@NotNull final Player player) {
        plugin.getScheduler().runLater(() -> {
            anticheats.forEach(anticheat -> anticheat.unexempt(player));
        }, 1);
    }
}
