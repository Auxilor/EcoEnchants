package com.willfp.ecoenchants.autosell;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

@UtilityClass
public class EconomyHandler {
    /**
     * The instance.
     */
    @Getter
    private static Economy instance = null;

    @Getter
    @Setter
    private static boolean enabled = false;

    /**
     * Initialize the economy manager.
     *
     * @return If was successful.
     */
    public boolean init() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        instance = rsp.getProvider();
        return true;
    }
}
