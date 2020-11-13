package com.willfp.ecoenchants.integrations.placeholder.plugins;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.integrations.placeholder.PlaceholderIntegration;
import com.willfp.ecoenchants.integrations.placeholder.PlaceholderManager;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import javax.annotation.PostConstruct;

public class PlaceholderIntegrationPAPI extends PlaceholderExpansion implements PlaceholderIntegration {
    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "Auxilor";
    }

    @Override
    public String getIdentifier() {
        return "ecoenchants";
    }

    @Override
    public String getVersion() {
        return EcoEnchantsPlugin.getInstance().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if(player == null)
            return "";

        return PlaceholderManager.getResult(player, identifier);
    }

    @Override
    public void registerIntegration() {
        this.register();
    }

    @Override
    public String getPluginName() {
        return "PlaceholderAPI";
    }

    @Override
    public String translate(String text, Player player) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
