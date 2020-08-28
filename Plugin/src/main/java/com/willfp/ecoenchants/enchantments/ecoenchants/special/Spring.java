package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.google.common.collect.Sets;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;
public class Spring extends EcoEnchant {
    public Spring() {
        super(
                new EcoEnchantBuilder("spring", EnchantmentType.SPECIAL, Target.Applicable.BOOTS, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        if (!HasEnchant.playerBoots(player, this)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    private final Set<UUID> prevPlayersOnGround = Sets.newHashSet();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getVelocity().getY() > 0) {
            float jumpVelocity = 0.42f;
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                jumpVelocity += ((float) player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F;
            }
            jumpVelocity = Float.parseFloat(df.format(jumpVelocity).replace(',', '.'));
            if (e.getPlayer().getLocation().getBlock().getType() != Material.LADDER && prevPlayersOnGround.contains(player.getUniqueId())) {
                if (!player.isOnGround() && Float.compare((float) player.getVelocity().getY(), jumpVelocity) == 0) {
                    if (!HasEnchant.playerBoots(player, this)) return;

                    double multiplier = 0.5 + (double) ((HasEnchant.getPlayerBootsLevel(player, this) * HasEnchant.getPlayerBootsLevel(player, this)) / 4 - 0.2) / 3;
                    player.setVelocity(player.getLocation().getDirection().multiply(multiplier).setY(multiplier));
                }
            }
        }
        if (player.isOnGround()) {
            prevPlayersOnGround.add(player.getUniqueId());
        } else {
            prevPlayersOnGround.remove(player.getUniqueId());
        }
    }
}
