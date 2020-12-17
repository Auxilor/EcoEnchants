package com.willfp.ecoenchants.integrations.worldguard;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface WorldguardWrapper {
    void registerFlag(String name, boolean def);
    boolean enabledForPlayer(EcoEnchant enchant, Player player, Location location);
}
