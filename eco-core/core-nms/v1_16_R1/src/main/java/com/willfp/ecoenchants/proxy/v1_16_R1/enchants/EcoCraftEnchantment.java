package com.willfp.ecoenchants.proxy.v1_16_R1.enchants;

import com.willfp.ecoenchants.enchantments.support.vanilla.VanillaEnchantmentMetadata;
import net.minecraft.server.v1_16_R1.Enchantment;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R1.enchantments.CraftEnchantment;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class EcoCraftEnchantment extends CraftEnchantment {
    private final VanillaEnchantmentMetadata metadata;

    public EcoCraftEnchantment(@NotNull final Enchantment target,
                               @NotNull final VanillaEnchantmentMetadata metadata) {
        super(target);
        this.metadata = metadata;
    }

    @Override
    public int getMaxLevel() {
        return metadata.getMaxLevel() == null ? this.getHandle().getMaxLevel() : metadata.getMaxLevel();
    }

    public void register() {
        try {
            Field byIdField = org.bukkit.enchantments.Enchantment.class.getDeclaredField("byKey");
            Field byNameField = org.bukkit.enchantments.Enchantment.class.getDeclaredField("byName");
            byIdField.setAccessible(true);
            byNameField.setAccessible(true);
            Map<NamespacedKey, org.bukkit.enchantments.Enchantment> byKey = (Map<NamespacedKey, org.bukkit.enchantments.Enchantment>) byIdField.get(null);
            Map<String, org.bukkit.enchantments.Enchantment> byName = (Map<String, org.bukkit.enchantments.Enchantment>) byNameField.get(null);
            byKey.remove(this.getKey());
            byName.remove(this.getName());

            Map<String, org.bukkit.enchantments.Enchantment> byNameClone = new HashMap<>(byName);
            for (Map.Entry<String, org.bukkit.enchantments.Enchantment> entry : byNameClone.entrySet()) {
                if (entry.getValue().getKey().equals(this.getKey())) {
                    byName.remove(entry.getKey());
                }
            }

            Field f = org.bukkit.enchantments.Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            f.setAccessible(false);

            org.bukkit.enchantments.Enchantment.registerEnchantment(this);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
    }
}
