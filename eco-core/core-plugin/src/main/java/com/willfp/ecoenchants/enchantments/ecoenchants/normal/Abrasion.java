package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.proxy.ProxyFactory;
import com.willfp.eco.core.proxy.proxies.CooldownProxy;
import com.willfp.eco.util.DurabilityUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
public class Abrasion extends EcoEnchant {
    public Abrasion() {
        super(
                "abrasion", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity uncastVictim, int level, EntityDamageByEntityEvent event) {
        if(!(uncastVictim instanceof Player)) return;
        Player victim = (Player) uncastVictim;

        boolean notcharged = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged");
        if (attacker instanceof Player && new ProxyFactory<>(CooldownProxy.class).getProxy().getAttackCooldown((Player) attacker) != 1.0f && !notcharged)
            return;

        ArrayList<ItemStack> armor = new ArrayList<>(Arrays.asList(victim.getInventory().getArmorContents()));
        if (armor.isEmpty())
            return;

        for (ItemStack armorPiece : armor) {
            if (armorPiece == null)
                continue;

            if(armorPiece.equals(victim.getInventory().getHelmet())) {
                DurabilityUtils.damageItem(victim, victim.getInventory().getHelmet(), level, 39);
            }
            if(armorPiece.equals(victim.getInventory().getChestplate())) {
                DurabilityUtils.damageItem(victim, victim.getInventory().getChestplate(), level, 38);
            }
            if(armorPiece.equals(victim.getInventory().getLeggings())) {
                DurabilityUtils.damageItem(victim, victim.getInventory().getLeggings(), level, 37);
            }
            if(armorPiece.equals(victim.getInventory().getBoots())) {
                DurabilityUtils.damageItem(victim, victim.getInventory().getBoots(), level, 36);
            }
        }
    }
}
