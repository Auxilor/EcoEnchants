package com.willfp.eco.util.integrations.anticheat.plugins;

import com.willfp.eco.util.integrations.anticheat.AnticheatWrapper;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AnticheatNCP implements AnticheatWrapper {
    private final Set<UUID> exempt = new HashSet<>();

    @Override
    public String getPluginName() {
        return "NCP";
    }

    @Override
    public void exempt(@NotNull final Player player) {
        if (!NCPExemptionManager.isExempted(player, CheckType.ALL)) {
            return;
        }

        if (exempt.add(player.getUniqueId())) {
            NCPExemptionManager.exemptPermanently(player, CheckType.ALL);
        }
    }

    @Override
    public void unexempt(@NotNull final Player player) {
        if (exempt.remove(player.getUniqueId())) {
            NCPExemptionManager.unexempt(player, CheckType.ALL);
        }
    }
}
