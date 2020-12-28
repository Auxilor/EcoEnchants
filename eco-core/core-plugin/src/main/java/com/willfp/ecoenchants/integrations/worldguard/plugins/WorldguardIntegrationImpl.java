package com.willfp.ecoenchants.integrations.worldguard.plugins;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.integrations.worldguard.WorldguardWrapper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorldguardIntegrationImpl implements WorldguardWrapper {
    private static final FlagRegistry REGISTRY = WorldGuard.getInstance().getFlagRegistry();

    @Override
    public void registerFlag(@NotNull final String name,
                             final boolean def) {
        StateFlag flag = new StateFlag(name, def);
        if (REGISTRY.get(name) == null) {
            REGISTRY.register(flag);
        }
    }

    @Override
    public boolean enabledForPlayer(@NotNull final EcoEnchant enchant,
                                    @NotNull final Player player,
                                    @NotNull final Location location) {
        if (WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(WorldGuardPlugin.inst().wrapPlayer(player), BukkitAdapter.adapt(location.getWorld()))) {
            return true;
        }

        return WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery()
                .queryState(BukkitAdapter.adapt(location), WorldGuardPlugin.inst().wrapPlayer(player), (StateFlag) REGISTRY.get(enchant.getKey().getKey() + "-enabled")) == StateFlag.State.ALLOW;
    }

    @Override
    public String getPluginName() {
        return "WorldGuard";
    }
}
