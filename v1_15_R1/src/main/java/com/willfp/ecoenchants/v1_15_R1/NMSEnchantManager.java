package com.willfp.ecoenchants.v1_15_R1;

import com.willfp.ecoenchants.API.NMSEnchantManagerWrapper;
import net.minecraft.server.v1_15_R1.Enchantment;
import net.minecraft.server.v1_15_R1.Enchantments;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NMSEnchantManager implements NMSEnchantManagerWrapper {
    private Map<org.bukkit.enchantments.Enchantment, NMSEnchant> relatedMap = new HashMap<>();


    @Override
    public void init(org.bukkit.enchantments.Enchantment enchantment) {
        try {
            Field byIdField = org.bukkit.enchantments.Enchantment.class.getDeclaredField("byKey");
            Field byNameField = org.bukkit.enchantments.Enchantment.class.getDeclaredField("byName");
            byIdField.setAccessible(true);
            byNameField.setAccessible(true);
            Map<NamespacedKey, org.bukkit.enchantments.Enchantment> byKey = (Map<NamespacedKey, org.bukkit.enchantments.Enchantment>) byIdField.get(null);
            Map<String, org.bukkit.enchantments.Enchantment> byName = (Map<String, org.bukkit.enchantments.Enchantment>) byNameField.get(null);
            byKey.remove(enchantment.getKey());
            byName.remove(enchantment.getName());

            Map<String, org.bukkit.enchantments.Enchantment> byNameClone = new HashMap<>(byName);
            for(Map.Entry<String, org.bukkit.enchantments.Enchantment> entry : byNameClone.entrySet()) {
                if(entry.getValue().getKey().equals(enchantment.getKey())) {
                    byName.remove(entry.getKey());
                }
            }

            Field f = org.bukkit.enchantments.Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            f.setAccessible(false);

            if(IRegistry.ENCHANTMENT.keySet().contains(new MinecraftKey(enchantment.getKey().getNamespace(), enchantment.getKey().getKey()))) {
                byName.put(enchantment.getName(), enchantment);
                byKey.put(enchantment.getKey(), enchantment);
                if(relatedMap.get(enchantment) != null) {
                    relatedMap.get(enchantment).update(enchantment);
                }

                return;
            }

            relatedMap.put(enchantment, new NMSEnchant(enchantment));

            Enchantment nmsEnchant = new NMSEnchant(enchantment);

            Method method = Enchantments.class.getDeclaredMethod("a", String.class, Enchantment.class);
            method.setAccessible(true);
            method.invoke(null, enchantment.getKey().getKey(), nmsEnchant);

            CraftEnchantWrapper wrappedEnchantment = new CraftEnchantWrapper(nmsEnchant, enchantment);

            org.bukkit.enchantments.Enchantment.registerEnchantment(wrappedEnchantment);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void debug() {
        Bukkit.getLogger().info("IRegistry: " + IRegistry.ENCHANTMENT.keySet().toString());
    }
}
