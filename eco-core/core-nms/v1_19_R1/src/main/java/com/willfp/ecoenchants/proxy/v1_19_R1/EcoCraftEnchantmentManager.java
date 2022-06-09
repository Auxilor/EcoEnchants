package com.willfp.ecoenchants.proxy.v1_19_R1;

import com.willfp.ecoenchants.enchantments.support.vanilla.VanillaEnchantmentMetadata;
import com.willfp.ecoenchants.enchantments.support.vanilla.VanillaEnchantments;
import com.willfp.ecoenchants.proxy.proxies.EcoCraftEnchantmentManagerProxy;
import com.willfp.ecoenchants.proxy.v1_19_R1.enchants.EcoCraftEnchantment;
import net.minecraft.core.IRegistry;
import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftNamespacedKey;

import java.util.Map;

public final class EcoCraftEnchantmentManager implements EcoCraftEnchantmentManagerProxy {
    @Override
    public void registerNewCraftEnchantments() {
        Map<org.bukkit.enchantments.Enchantment, VanillaEnchantmentMetadata> metadataMap = VanillaEnchantments.getMetadataMap();

        for (Enchantment enchantment : IRegistry.W) {
            NamespacedKey key = CraftNamespacedKey.fromMinecraft(IRegistry.W.b(enchantment));
            VanillaEnchantmentMetadata metadata = metadataMap.get(org.bukkit.enchantments.Enchantment.getByKey(key));
            new EcoCraftEnchantment(enchantment, metadata).register();
        }
    }
}
