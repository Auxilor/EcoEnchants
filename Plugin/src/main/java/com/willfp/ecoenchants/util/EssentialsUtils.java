package com.willfp.ecoenchants.util;

import com.earth2me.essentials.Enchantments;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EssentialsUtils {
    private static Map<String, Enchantment> essentialsEnchantmentsMap;
    private static boolean hasCheckedEnchantmentsMap;

    @Nullable
    public static Map<String, Enchantment> getEnchantmentsMap() {
        if (hasCheckedEnchantmentsMap) return essentialsEnchantmentsMap;

        if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            try {
                Class<?> essEnchantmentsClass = Enchantments.class;

                Object enchantments = FieldUtils.readDeclaredStaticField(essEnchantmentsClass, "ENCHANTMENTS", true);
                if (enchantments instanceof Map) {
                    hasCheckedEnchantmentsMap = true;
                    //noinspection unchecked - we know the type of it
                    return essentialsEnchantmentsMap = (Map<String, Enchantment>) enchantments;
                }
            } catch (IllegalAccessException e) {
                hasCheckedEnchantmentsMap = true;
                return null;
            }
        }
        hasCheckedEnchantmentsMap = true;
        return null;
    }
}
