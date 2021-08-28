package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.VectorUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Reel extends EcoEnchant {
    public Reel() {
        super(
                "reel", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onFish(@NotNull final PlayerFishEvent event) {
        if (!event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) {
            return;
        }

        if (!(event.getCaught() instanceof LivingEntity victim)) {
            return;
        }

        Player player = event.getPlayer();

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (victim.hasMetadata("NPC")) {
            return;
        }

        if (!AntigriefManager.canInjure(player, victim)) {
            return;
        }

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        int level = EnchantChecks.getMainhandLevel(player, this);

        double baseMultiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity-multiplier");
        Vector vector = player.getLocation().toVector().clone().subtract(victim.getLocation().toVector()).normalize().multiply(level * baseMultiplier);
        if (VectorUtils.isFinite(vector)) {
            victim.setVelocity(vector);
        }
    }
}
