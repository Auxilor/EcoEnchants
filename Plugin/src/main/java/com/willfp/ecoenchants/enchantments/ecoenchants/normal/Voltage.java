package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public final class Voltage extends EcoEnchant {
    public Voltage() {
        super(
                "voltage", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    private static final Material[] items = {
            Material.IRON_HELMET,
            Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS,
            Material.IRON_BOOTS,

            Material.GOLDEN_HELMET,
            Material.GOLDEN_CHESTPLATE,
            Material.GOLDEN_LEGGINGS,
            Material.GOLDEN_BOOTS,

            Material.CHAINMAIL_HELMET,
            Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS,
            Material.CHAINMAIL_BOOTS,
    };

    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        EntityEquipment equipment = victim.getEquipment();
        if(equipment == null) return;

        int pieces = 0;
        for (ItemStack armorPiece : equipment.getArmorContents()) {
            if(armorPiece == null) continue;
            if(Arrays.asList(items).contains(armorPiece.getType())) pieces++;
        }

        if(pieces == 0) return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");

        event.setDamage(event.getDamage() * (1 + (level * multiplier * pieces)));
    }
}
