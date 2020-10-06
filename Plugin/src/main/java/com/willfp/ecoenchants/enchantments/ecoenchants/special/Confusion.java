package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.nms.Cooldown;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public final class Confusion extends EcoEnchant {
    public Confusion() {
        super(
                new EcoEnchantBuilder("confusion", EnchantmentType.SPECIAL,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity uncastVictim, int level, EntityDamageByEntityEvent event) {
        if(!(uncastVictim instanceof Player)) return;
        Player victim = (Player) uncastVictim;

        if(attacker instanceof Player) {
            if (Cooldown.getCooldown((Player) attacker) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged"))
                return;
        }
        if(!EnchantmentUtils.passedChance(this, level))
            return;

        List<ItemStack> hotbar = new ArrayList<ItemStack>();
        for (int i = 0; i < 9; i++) {
            hotbar.add(victim.getInventory().getItem(i));
        }
        Collections.shuffle(hotbar);
        int i2 = 0;
        for (ItemStack item : hotbar) {
            victim.getInventory().setItem(i2, item);
            i2++;
        }
    }
}
