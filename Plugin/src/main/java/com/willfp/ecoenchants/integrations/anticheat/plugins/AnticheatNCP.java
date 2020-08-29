package com.willfp.ecoenchants.integrations.anticheat.plugins;

import com.willfp.ecoenchants.integrations.anticheat.AnticheatWrapper;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class AnticheatNCP implements AnticheatWrapper {
    private final Set<UUID> exempt = new HashSet<>();

    @Override
    public String getPluginName() {
        return "NCP";
    }

    @Override
    public void exempt(Player player) {
        if(!NCPExemptionManager.isExempted(player, CheckType.ALL)) {
            return;
        }

        if(exempt.add(player.getUniqueId())) {
            NCPExemptionManager.exemptPermanently(player, CheckType.ALL);
        }
    }

    @Override
    public void unexempt(Player player) {
        if(exempt.remove(player.getUniqueId())) {
            NCPExemptionManager.unexempt(player, CheckType.ALL);
        }
    }
}
