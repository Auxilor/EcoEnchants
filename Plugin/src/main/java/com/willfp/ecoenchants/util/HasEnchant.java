package com.willfp.ecoenchants.util;

import com.willfp.ecoenchants.nms.Target;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class HasEnchant {

    /**
     * If player is holding item with Enchantment
     *
     * @param player  The player
     * @param enchant The Enchantment to test
     * @return If the player is holding the item
     */
    public static boolean playerHeld(Player player, Enchantment enchant) {
        if(player == null)
            return false;

        if(player.getInventory().getItemInMainHand() == null)
            return false;

        if(!player.getInventory().getItemInMainHand().hasItemMeta())
            return false;

        return Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).hasEnchant(enchant);
    }

    /**
     * If horse has enchantment on armor
     *
     * @param horse   The horse
     * @param enchant The Enchantment to test
     * @return If the player is holding the item
     */
    public static boolean horseArmor(Horse horse, Enchantment enchant) {
        if(horse == null)
            return false;

        if(horse.getInventory().getArmor() == null)
            return false;

        if(!horse.getInventory().getArmor().hasItemMeta())
            return false;

        return Objects.requireNonNull(horse.getInventory().getArmor().getItemMeta()).hasEnchant(enchant);
    }

    /**
     * If player is holding item in offhand with Enchantment
     *
     * @param player  The player
     * @param enchant The Enchantment to test
     * @return If the player is holding the item in offhand
     */
    public static boolean playerOffhand(Player player, Enchantment enchant) {
        if(player == null)
            return false;

        if(player.getInventory().getItemInOffHand() == null)
            return false;

        if(!player.getInventory().getItemInOffHand().hasItemMeta())
            return false;

        return Objects.requireNonNull(player.getInventory().getItemInOffHand().getItemMeta()).hasEnchant(enchant);
    }

    /**
     * If player has helmet with Enchantment
     *
     * @param player  The player
     * @param enchant The Enchantment to test
     * @return If the player has the enchantment on their helmet
     */
    public static boolean playerHelmet(Player player, Enchantment enchant) {
        if(player == null)
            return false;

        if(player.getInventory().getHelmet() == null)
            return false;

        if(!player.getInventory().getHelmet().hasItemMeta())
            return false;

        return Objects.requireNonNull(player.getInventory().getHelmet().getItemMeta()).hasEnchant(enchant);
    }

    /**
     * If player has boots with Enchantment
     *
     * @param player  The player
     * @param enchant The Enchantment to test
     * @return If the player has the enchantment on their boots
     */
    public static boolean playerBoots(Player player, Enchantment enchant) {
        if(player == null)
            return false;

        if(player.getInventory().getBoots() == null)
            return false;

        if(!player.getInventory().getBoots().hasItemMeta())
            return false;

        return Objects.requireNonNull(player.getInventory().getBoots().getItemMeta()).hasEnchant(enchant);
    }

    /**
     * If player has elytra with Enchantment
     *
     * @param player  The player
     * @param enchant The Enchantment to test
     * @return If the player has the enchantment on their elytra
     */
    public static boolean playerElytra(Player player, Enchantment enchant) {
        if(player == null)
            return false;

        if(player.getInventory().getChestplate() == null)
            return false;

        if(!Target.Applicable.ELYTRA.getMaterials().contains(player.getInventory().getChestplate().getType()))
            return false;

        if(!player.getInventory().getChestplate().hasItemMeta())
            return false;

        return Objects.requireNonNull(player.getInventory().getChestplate().getItemMeta()).hasEnchant(enchant);
    }

    /**
     * If item has Enchantment
     *
     * @param item    The item
     * @param enchant The Enchantment to test
     * @return If item has Enchantment
     */
    public static boolean item(ItemStack item, Enchantment enchant) {

        if(item == null)
            return false;

        if(!item.hasItemMeta())
            return false;

        if(item.getItemMeta() == null)
            return false;

        return item.getItemMeta().hasEnchant(enchant);
    }

    /**
     * Get level of player held item
     *
     * @param player  The player
     * @param enchant The Enchantment to test
     * @return The level
     */
    public static int getPlayerLevel(Player player, Enchantment enchant) {
        return player.getInventory().getItemInMainHand().getEnchantmentLevel(enchant);
    }

    /**
     * Get level of horse armor
     *
     * @param horse   The horse
     * @param enchant The Enchantment to test
     * @return The level
     */
    public static int getHorseLevel(Horse horse, Enchantment enchant) {
        return Objects.requireNonNull(horse.getInventory().getArmor()).getEnchantmentLevel(enchant);
    }

    /**
     * Get level of player held item in offhand
     *
     * @param player  The player
     * @param enchant The Enchantment to test
     * @return The level
     */
    public static int getPlayerOffhandLevel(Player player, Enchantment enchant) {
        return player.getInventory().getItemInOffHand().getEnchantmentLevel(enchant);
    }

    /**
     * Get level of player boots
     *
     * @param player  The player
     * @param enchant The Enchantment to test
     * @return The level
     */
    public static int getPlayerBootsLevel(Player player, Enchantment enchant) {
        assert player.getInventory().getBoots() != null;
        return player.getInventory().getBoots().getEnchantmentLevel(enchant);
    }

    /**
     * Get level of player helmet
     *
     * @param player  The player
     * @param enchant The Enchantment to test
     * @return The level
     */
    public static int getPlayerHelmetLevel(Player player, Enchantment enchant) {
        assert player.getInventory().getHelmet() != null;
        return player.getInventory().getHelmet().getEnchantmentLevel(enchant);
    }

    /**
     * Get level of player chestplate/elytra
     *
     * @param player  The player
     * @param enchant The Enchantment to test
     * @return The level
     */
    public static int getPlayerChestplateLevel(Player player, Enchantment enchant) {
        assert player.getInventory().getChestplate() != null;
        return player.getInventory().getChestplate().getEnchantmentLevel(enchant);
    }

    /**
     * Get level of item
     *
     * @param item    The item
     * @param enchant The Enchantment to test
     * @return The level
     */
    public static int getItemLevel(ItemStack item, Enchantment enchant) {
        assert item.getItemMeta() != null;
        return item.getItemMeta().getEnchantLevel(enchant);
    }

    /**
     * Get total levels of player armor
     *
     * @param player  The player
     * @param enchant The Enchantment to test
     * @param damage  Damage the armor
     * @return The total levels
     */
    public static int getArmorPoints(Player player, Enchantment enchant, boolean damage) {
        ArrayList<ItemStack> armor = new ArrayList<ItemStack>(Arrays.asList(player.getInventory().getArmorContents()));
        if(armor.isEmpty())
            return 0;


        int points = 0;

        for(ItemStack armorPiece : armor) {
            if(armorPiece == null)
                continue;
            if(armorPiece.containsEnchantment(enchant)) {
                points += armorPiece.getEnchantmentLevel(enchant);

                if(damage) {
                    if(Target.Applicable.HELMET.getMaterials().contains(armorPiece.getType())) {
                        ItemDurability.damageItem(player, player.getInventory().getHelmet(), 1, 39);
                    }
                    if(Target.Applicable.CHESTPLATE.getMaterials().contains(armorPiece.getType())) {
                        ItemDurability.damageItem(player, player.getInventory().getChestplate(), 1, 38);
                    }
                    if(Target.Applicable.LEGGINGS.getMaterials().contains(armorPiece.getType())) {
                        ItemDurability.damageItem(player, player.getInventory().getLeggings(), 1, 37);
                    }
                    if(Target.Applicable.BOOTS.getMaterials().contains(armorPiece.getType())) {
                        ItemDurability.damageItem(player, player.getInventory().getBoots(), 1, 36);
                    }
                }
            }
        }

        return points;
    }
}
