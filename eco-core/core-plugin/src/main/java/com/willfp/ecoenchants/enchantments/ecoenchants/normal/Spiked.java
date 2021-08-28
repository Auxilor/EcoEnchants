package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.NotNull;

public class Spiked extends EcoEnchant {
    public Spiked() {
        super(
                "spiked", EnchantmentType.NORMAL
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

        double damagePerLevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level");
        double damage = damagePerLevel * level;
        victim.damage(damage, player);
    }
}
