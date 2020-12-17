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

public class WorldguardIntegrationImpl implements WorldguardWrapper {
    private static final FlagRegistry REGISTRY = WorldGuard.getInstance().getFlagRegistry();

    @Override
    public void registerFlag(String name, boolean def) {
        StateFlag flag = new StateFlag(name, def);
        if(REGISTRY.get(name) == null) {
            REGISTRY.register(flag);
        }
    }

    @Override
    public boolean enabledForPlayer(EcoEnchant enchant, Player player, Location location) {
        if(WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(WorldGuardPlugin.inst().wrapPlayer(player), BukkitAdapter.adapt(location.getWorld()))) return true;
        return WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().testState(BukkitAdapter.adapt(location), WorldGuardPlugin.inst().wrapPlayer(player), (StateFlag) REGISTRY.get(enchant.getKey().getKey() + "-enabled"));
    }
}
