package com.willfp.eco.util.integrations.anticheat.plugins;

import com.willfp.eco.util.integrations.anticheat.AnticheatWrapper;
import me.konsolas.aac.api.AACAPI;
import me.konsolas.aac.api.AACExemption;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AnticheatAAC implements AnticheatWrapper, Listener {
    /**
     * AAC exemption for EcoEnchants.
     */
    private final AACExemption ecoEnchantsExemption = new AACExemption("EcoEnchants");

    /**
     * AAC api.
     */
    private final AACAPI api = Objects.requireNonNull(Bukkit.getServicesManager().load(AACAPI.class));

    @Override
    public String getPluginName() {
        return "AAC";
    }

    @Override
    public void exempt(@NotNull final Player player) {
        api.addExemption(player, ecoEnchantsExemption);
    }

    @Override
    public void unexempt(@NotNull final Player player) {
        api.removeExemption(player, ecoEnchantsExemption);
    }
}
