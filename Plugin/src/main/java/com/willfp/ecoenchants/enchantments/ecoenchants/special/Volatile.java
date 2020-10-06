package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Cooldown;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public final class Volatile extends EcoEnchant {
    public Volatile() {
        super(
                new EcoEnchantBuilder("volatile", EnchantmentType.SPECIAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity uncastAttacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(!(uncastAttacker instanceof Player)) return;

        Player attacker = (Player) uncastAttacker;

        if (Cooldown.getCooldown(attacker) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged"))
            return;
        if(!EnchantmentUtils.passedChance(this, level))
            return;

        boolean fire = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "fire");
        boolean breakblocks = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "break-blocks");

        float power = (float) (0.5 + (level * 0.5));

        if (!AntigriefManager.canCreateExplosion(attacker, event.getEntity().getLocation())) return;
        if (breakblocks) {
            if (!AntigriefManager.canBreakBlock(attacker, event.getEntity().getLocation().getWorld().getBlockAt(event.getEntity().getLocation())))
                return;
        }

        double distance = attacker.getLocation().distance(victim.getLocation());
        Location explosionLoc = victim.getEyeLocation();

        victim.getWorld().createExplosion(explosionLoc, power, fire, breakblocks);
    }
}
