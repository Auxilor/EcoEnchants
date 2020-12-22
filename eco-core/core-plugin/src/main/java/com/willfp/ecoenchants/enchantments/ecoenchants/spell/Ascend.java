package com.willfp.ecoenchants.enchantments.ecoenchants.spell;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ascend extends Spell {
    private static final String IGNORE_FALL_KEY = "ignore-fall-damage";

    public Ascend() {
        super("ascend");
    }

    @Override
    public void onUse(Player player, int level, PlayerInteractEvent event) {
        int ticks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level") * level;
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, ticks, this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "power") - 1,false,false));
        player.setMetadata(IGNORE_FALL_KEY, new FixedMetadataValue(this.plugin, true));
        this.plugin.getScheduler().runLater(() -> player.removeMetadata(IGNORE_FALL_KEY, this.plugin), ticks * 4L);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
            return;

        if(!event.getEntity().hasMetadata(IGNORE_FALL_KEY))
            return;

        event.setCancelled(true);
    }
}
