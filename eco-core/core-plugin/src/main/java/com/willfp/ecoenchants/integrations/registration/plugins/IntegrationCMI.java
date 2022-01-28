package com.willfp.ecoenchants.integrations.registration.plugins;

import com.willfp.ecoenchants.integrations.registration.RegistrationWrapper;
import net.Zrips.CMILib.Enchants.CMIEnchantment;

public class IntegrationCMI implements RegistrationWrapper {
    @Override
    public void registerAllEnchantments() {
        CMIEnchantment.initialize();
        CMIEnchantment.saveEnchants();
    }

    @Override
    public String getPluginName() {
        return "CMI";
    }
}
