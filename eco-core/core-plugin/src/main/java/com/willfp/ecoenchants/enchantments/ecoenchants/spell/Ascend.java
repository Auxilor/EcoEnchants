package com.willfp.ecoenchants.enchantments.ecoenchants.spell;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class Ascend extends Spell {
    private static final String IGNORE_FALL_KEY = "ignore-fall-damage";

    public Ascend() {
        super("ascend");
    }

    @Override
    public boolean onUse(@NotNull final Player player,
                         final int level,
                         @NotNull final PlayerInteractEvent event) {
        int ticks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level") * level;
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, ticks, this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "power") - 1, false, false));
        player.setMetadata(IGNORE_FALL_KEY, this.getPlugin().getMetadataValueFactory().create(true));
        this.getPlugin().getScheduler().runLater(() -> player.removeMetadata(IGNORE_FALL_KEY, this.getPlugin()), ticks * 4L);

        return true;
    }

    @EventHandler
    public void onFallDamage(@NotNull final EntityDamageEvent event) {
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            return;
        }

        if (!event.getEntity().hasMetadata(IGNORE_FALL_KEY)) {
            return;
        }

        event.setCancelled(true);
    }
}
