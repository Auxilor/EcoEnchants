package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class Launch extends EcoEnchant {
    public Launch() {
        super(
                "launch", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS
    @EventHandler
    public void onFireworkUse(@NotNull final PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }

        if (!event.getItem().getType().equals(Material.FIREWORK_ROCKET)) {
            return;
        }

        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }

        Player player = event.getPlayer();

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (!player.isGliding()) {
            return;
        }

        if (!EnchantChecks.chestplate(player, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        int level = EnchantChecks.getChestplateLevel(player, this);
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double boost = 1 + (multiplier * level);

        this.getPlugin().getScheduler().run(() -> player.setVelocity(player.getVelocity().multiply(boost)));
    }
}
