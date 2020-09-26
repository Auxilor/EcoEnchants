package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Cooldown;
import com.willfp.ecoenchants.util.ItemDurability;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
public class Abrasion extends EcoEnchant {
    public Abrasion() {
        super(
                new EcoEnchantBuilder("abrasion", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity uncastVictim, int level, EntityDamageByEntityEvent event) {
        if(!(uncastVictim instanceof Player)) return;
        Player victim = (Player) uncastVictim;

        boolean notcharged = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged");
        if (attacker instanceof Player && Cooldown.getCooldown((Player) attacker) != 1.0f && !notcharged)
            return;

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
