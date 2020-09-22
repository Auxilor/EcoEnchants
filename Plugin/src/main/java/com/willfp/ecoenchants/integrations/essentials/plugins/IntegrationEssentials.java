package com.willfp.ecoenchants.integrations.essentials.plugins;

import com.earth2me.essentials.Enchantments;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.essentials.EssentialsWrapper;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

@SuppressWarnings("unchecked")
public final class IntegrationEssentials implements EssentialsWrapper {
    @Override
    public void registerAllEnchantments() {
        try {
            for (Enchantment enchantment : EcoEnchants.getAll()) {
                ((Map<String, Enchantment>) FieldUtils.readDeclaredStaticField(Enchantments.class, "ENCHANTMENTS", true)).put(enchantment.getKey().getKey(), enchantment);
            }
        } catch (IllegalAccessException ignored) {}
    }

    @Override
    public String getPluginName() {
        return "Essentials";
    }
}
