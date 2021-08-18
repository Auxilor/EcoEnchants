package com.willfp.ecoenchants.integrations.registration.plugins;

import com.earth2me.essentials.Enchantments;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.registration.RegistrationWrapper;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

@SuppressWarnings("unchecked")
public class IntegrationEssentials implements RegistrationWrapper {
    @Override
    public void registerAllEnchantments() {
        try {
            for (EcoEnchant enchantment : EcoEnchants.values()) {
                ((Map<String, Enchantment>) FieldUtils.readDeclaredStaticField(Enchantments.class, "ENCHANTMENTS", true)).put(enchantment.getKey().getKey(), enchantment);
                ((Map<String, Enchantment>) FieldUtils.readDeclaredStaticField(Enchantments.class, "ENCHANTMENTS", true)).put(enchantment.getPermissionName(), enchantment);
            }
        } catch (IllegalAccessException ignored) {
            // Ignore reflective errors that won't happen.
        }
    }

    @Override
    public String getPluginName() {
        return "Essentials";
    }
}
