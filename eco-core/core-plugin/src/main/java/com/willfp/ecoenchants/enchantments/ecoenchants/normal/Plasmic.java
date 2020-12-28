package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.optional.Prerequisite;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Plasmic extends EcoEnchant {
    public Plasmic() {
        super(
                "plasmic", EnchantmentType.NORMAL
        );
    }
    private static final Material[] ITEMS;

    static {
        if (Prerequisite.MINIMUM_1_16.isMet()) {
            ITEMS = new Material[]{
                    Material.DIAMOND_HELMET,
                    Material.DIAMOND_CHESTPLATE,
                    Material.DIAMOND_LEGGINGS,
                    Material.DIAMOND_BOOTS,

                    Material.NETHERITE_HELMET,
                    Material.NETHERITE_CHESTPLATE,
                    Material.NETHERITE_LEGGINGS,
                    Material.NETHERITE_BOOTS
            };
        } else {
            ITEMS = new Material[]{
                    Material.DIAMOND_HELMET,
                    Material.DIAMOND_CHESTPLATE,
                    Material.DIAMOND_LEGGINGS,
                    Material.DIAMOND_BOOTS
            };
        }
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        EntityEquipment equipment = victim.getEquipment();
        if (equipment == null) {
            return;
        }

        int pieces = 0;
        for (ItemStack armorPiece : equipment.getArmorContents()) {
            if (armorPiece == null) {
                continue;
            }
            if (Arrays.asList(ITEMS).contains(armorPiece.getType())) {
                pieces++;
            }
        }

        if (pieces == 0) {
            return;
        }

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");

        event.setDamage(event.getDamage() * (1 + (level * multiplier * pieces)));
    }
}
