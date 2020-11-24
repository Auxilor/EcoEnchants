package com.willfp.ecoenchants.enchantments.util;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.util.DurabilityUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unchecked")
public class EnchantChecks {
    public static boolean item(ItemStack item, Enchantment enchantment) {
        return getItemLevel(item, enchantment) != 0;
    }

    public static int getItemLevel(ItemStack item, Enchantment enchantment) {
        if(item == null) return 0;
        if(item.getType().equals(Material.AIR)) return 0;
        if(!item.hasItemMeta()) return 0;
        if(item.getItemMeta() == null) return 0;

        return item.getItemMeta().getEnchantLevel(enchantment);
    }

    public static Map<EcoEnchant, Integer> getEnchantsOnItem(ItemStack item) {
        if(item == null) return new HashMap<>();
        if(item.getType().equals(Material.AIR)) return new HashMap<>();
        if(!item.hasItemMeta()) return new HashMap<>();
        if(item.getItemMeta() == null) return new HashMap<>();

        Map<EcoEnchant, Integer> ecoEnchants = new HashMap<>();
        item.getEnchantments().forEach(((enchantment, integer) -> {
            if(EcoEnchants.getFromEnchantment(enchantment) != null) {
                ecoEnchants.put(EcoEnchants.getFromEnchantment(enchantment), integer);
            }
        }));

        return ecoEnchants;
    }

    public static boolean arrow(Arrow arrow, Enchantment enchantment) {
        return getArrowLevel(arrow, enchantment) != 0;
    }

    public static int getArrowLevel(Arrow arrow, Enchantment enchantment) {
        if (arrow.getMetadata("enchantments").isEmpty()) return 0;

        MetadataValue enchantmentsMetaValue = arrow.getMetadata("enchantments").get(0);
        if (!(enchantmentsMetaValue.value() instanceof Map))
            return 0;

        Map<Enchantment, Integer> enchantments = (Map<Enchantment, Integer>) enchantmentsMetaValue.value();
        if (enchantments == null) return 0;
        if(!enchantments.containsKey(enchantment)) return 0;
        return enchantments.get(enchantment);
    }

    public static Map<EcoEnchant, Integer> getEnchantsOnArrow(Arrow arrow) {
        if (arrow.getMetadata("enchantments").isEmpty()) return new HashMap<>();

        MetadataValue enchantmentsMetaValue = arrow.getMetadata("enchantments").get(0);
        if (!(enchantmentsMetaValue.value() instanceof Map))
            return new HashMap<>();

        Map<EcoEnchant, Integer> ecoEnchants = new HashMap<>();
        ((Map<Enchantment, Integer>) enchantmentsMetaValue.value()).forEach(((enchantment, integer) -> {
            if(EcoEnchants.getFromEnchantment(enchantment) != null) {
                ecoEnchants.put(EcoEnchants.getFromEnchantment(enchantment), integer);
            }
        }));

        return ecoEnchants;
    }

    public static boolean mainhand(LivingEntity entity, Enchantment enchantment) {
        return getMainhandLevel(entity, enchantment) != 0;
    }

    public static int getMainhandLevel(LivingEntity entity, Enchantment enchantment) {
        if(entity.getEquipment() == null)
            return 0;

        ItemStack item = entity.getEquipment().getItemInMainHand();

        return getItemLevel(item, enchantment);
    }

    public static Map<EcoEnchant, Integer> getEnchantsOnMainhand(LivingEntity entity) {
        if(entity.getEquipment() == null)
            return new HashMap<>();

        ItemStack item = entity.getEquipment().getItemInMainHand();

        if(item == null) return new HashMap<>();

        Map<EcoEnchant, Integer> ecoEnchants = new HashMap<>();

        item.getEnchantments().forEach(((enchantment, integer) -> {
            if(EcoEnchants.getFromEnchantment(enchantment) != null) {
                ecoEnchants.put(EcoEnchants.getFromEnchantment(enchantment), integer);
            }
        }));

        return ecoEnchants;
    }

    public static boolean offhand(LivingEntity entity, Enchantment enchantment) {
        return getOffhandLevel(entity, enchantment) != 0;
    }

    public static int getOffhandLevel(LivingEntity entity, Enchantment enchantment) {
        if(entity.getEquipment() == null)
            return 0;

        ItemStack item = entity.getEquipment().getItemInOffHand();

        return getItemLevel(item, enchantment);
    }

    public static Map<EcoEnchant, Integer> getEnchantsOnOffhand(LivingEntity entity) {
        if(entity.getEquipment() == null)
            return new HashMap<>();

        ItemStack item = entity.getEquipment().getItemInOffHand();

        if(item == null) return new HashMap<>();

        Map<EcoEnchant, Integer> ecoEnchants = new HashMap<>();

        item.getEnchantments().forEach(((enchantment, integer) -> {
            if(EcoEnchants.getFromEnchantment(enchantment) != null) {
                ecoEnchants.put(EcoEnchants.getFromEnchantment(enchantment), integer);
            }
        }));

        return ecoEnchants;
    }

    public static int getArmorPoints(LivingEntity entity, Enchantment enchantment) {
        return getArmorPoints(entity, enchantment, 0);
    }

    public static int getArmorPoints(LivingEntity entity, Enchantment enchantment, int damage) {
        if(entity.getEquipment() == null)
            return 0;

        boolean isPlayer = entity instanceof Player;

        AtomicInteger armorPoints = new AtomicInteger(0);
        List<ItemStack> armor = Arrays.asList(entity.getEquipment().getArmorContents());
        armor.forEach((itemStack -> {
            int level = getItemLevel(itemStack, enchantment);
            if(level != 0) {
                armorPoints.addAndGet(getItemLevel(itemStack, enchantment));
                if(damage > 0 && isPlayer) {
                    Player player = (Player) entity;
                    if(itemStack.equals(entity.getEquipment().getHelmet())) {
                        DurabilityUtils.damageItem(player, player.getInventory().getHelmet(), level, 39);
                    }
                    if(itemStack.equals(entity.getEquipment().getChestplate())) {
                        DurabilityUtils.damageItem(player, player.getInventory().getChestplate(), level, 38);
                    }
                    if(itemStack.equals(entity.getEquipment().getLeggings())) {
                        DurabilityUtils.damageItem(player, player.getInventory().getLeggings(), level, 37);
                    }
                    if(itemStack.equals(entity.getEquipment().getBoots())) {
                        DurabilityUtils.damageItem(player, player.getInventory().getBoots(), level, 36);
                    }
                }
            }
        }));

        return armorPoints.get();
    }

    public static Map<EcoEnchant, Integer> getEnchantsOnArmor(LivingEntity entity) {
        if(entity.getEquipment() == null)
            return new HashMap<>();

        AtomicInteger armorPoints = new AtomicInteger(0);
        List<ItemStack> armor = Arrays.asList(entity.getEquipment().getArmorContents());

        Map<EcoEnchant, Integer> ecoEnchants = new HashMap<>();

        armor.forEach((itemStack -> {
            ecoEnchants.putAll(EnchantChecks.getEnchantsOnItem(itemStack));
        }));

        return ecoEnchants;
    }

    public static boolean helmet(LivingEntity entity, Enchantment enchantment) {
        return getHelmetLevel(entity, enchantment) != 0;
    }

    public static int getHelmetLevel(LivingEntity entity, Enchantment enchantment) {
        if(entity.getEquipment() == null)
            return 0;

        ItemStack item = entity.getEquipment().getHelmet();

        return getItemLevel(item, enchantment);
    }

    public static boolean chestplate(LivingEntity entity, Enchantment enchantment) {
        return getChestplateLevel(entity, enchantment) != 0;
    }

    public static int getChestplateLevel(LivingEntity entity, Enchantment enchantment) {
        if(entity.getEquipment() == null)
            return 0;

        ItemStack item = entity.getEquipment().getChestplate();

        return getItemLevel(item, enchantment);
    }

    public static boolean leggings(LivingEntity entity, Enchantment enchantment) {
        return getLeggingsLevel(entity, enchantment) != 0;
    }

    public static int getLeggingsLevel(LivingEntity entity, Enchantment enchantment) {
        if(entity.getEquipment() == null)
            return 0;

        ItemStack item = entity.getEquipment().getLeggings();

        return getItemLevel(item, enchantment);
    }

    public static boolean boots(LivingEntity entity, Enchantment enchantment) {
        return getBootsLevel(entity, enchantment) != 0;
    }

    public static int getBootsLevel(LivingEntity entity, Enchantment enchantment) {
        if(entity.getEquipment() == null)
            return 0;

        ItemStack item = entity.getEquipment().getBoots();

        return getItemLevel(item, enchantment);
    }
}