package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.DurabilityUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.Damageable;

public class Grit extends EcoEnchant {
    public Grit() {
        super(
                "grit", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onGritHurt(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        if (!AntigriefManager.canInjure(attacker, player)) return;

        int totalGritPoints = EnchantChecks.getArmorPoints(player, this, 0);

        if (totalGritPoints == 0)
            return;
        if (this.getDisabledWorlds().contains(player.getWorld())) return;

        if (!(attacker.getInventory().getItemInMainHand() instanceof Damageable))
            return;

        int damage = (int) Math.ceil(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level") * totalGritPoints);

        DurabilityUtils.damageItem(player, player.getInventory().getItemInMainHand(), 1, player.getInventory().getHeldItemSlot());
    }

}
