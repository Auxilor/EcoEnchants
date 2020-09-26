package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.util.ItemDurability;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class Corrosive extends EcoEnchant {
    public Corrosive() {
        super(
                new EcoEnchantBuilder("corrosive", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity uncastVictim, Arrow arrow, int level, EntityDamageByEntityEvent event) {
        if(!(uncastVictim instanceof Player)) return;
        Player victim = (Player) uncastVictim;

        ArrayList<ItemStack> armor = new ArrayList<ItemStack>(Arrays.asList(victim.getInventory().getArmorContents()));
        if (armor.isEmpty())
            return;

        for (ItemStack armorPiece : armor) {
            if (armorPiece == null)
                continue;


            if(armorPiece.equals(victim.getInventory().getHelmet())) {
                ItemDurability.damageItem(victim, victim.getInventory().getHelmet(), level, 39);
            }
            if(armorPiece.equals(victim.getInventory().getChestplate())) {
                ItemDurability.damageItem(victim, victim.getInventory().getChestplate(), level, 38);
            }
            if(armorPiece.equals(victim.getInventory().getLeggings())) {
                ItemDurability.damageItem(victim, victim.getInventory().getLeggings(), level, 37);
            }
            if(armorPiece.equals(victim.getInventory().getBoots())) {
                ItemDurability.damageItem(victim, victim.getInventory().getBoots(), level, 36);
            }
        }
    }
}
