package com.willfp.ecoenchants.integrations.plugins

import com.willfp.ecoenchants.integrations.EnchantRegistrationIntegration
import net.Zrips.CMILib.Enchants.CMIEnchantment

class CMIIntegration: EnchantRegistrationIntegration {
    override fun registerEnchants() {
        CMIEnchantment.initialize()
        CMIEnchantment.saveEnchants()
    }

    override fun getPluginName() = "CMI"
}
