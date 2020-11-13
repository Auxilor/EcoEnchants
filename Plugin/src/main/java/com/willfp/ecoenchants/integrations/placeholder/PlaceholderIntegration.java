package com.willfp.ecoenchants.integrations.placeholder;

import com.willfp.ecoenchants.integrations.Integration;
import org.bukkit.entity.Player;

public interface PlaceholderIntegration extends Integration {
    void registerIntegration();
    String translate(String text, Player player);
}
