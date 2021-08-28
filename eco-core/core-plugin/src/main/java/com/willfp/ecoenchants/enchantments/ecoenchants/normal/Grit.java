package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.DurabilityUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

public class Grit extends EcoEnchant {
    public Grit() {
        super(
                "grit", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onGritHurt(@NotNull final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        if (!this.areRequirementsMet(attacker)) {
            return;
        }

        if (!AntigriefManager.canInjure(attacker, player)) {
            return;
        }

        int totalGritPoints = EnchantChecks.getArmorPoints(player, this, 0);

        if (totalGritPoints == 0) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (!(attacker.getInventory().getItemInMainHand() instanceof Damageable)) {
            return;
        }

        int damage = (int) Math.ceil(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level") * totalGritPoints);

        DurabilityUtils.damageItem(attacker, attacker.getInventory().getItemInMainHand(), damage, attacker.getInventory().getHeldItemSlot());
    }

}
