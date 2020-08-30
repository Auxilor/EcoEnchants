package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Graceful extends EcoEnchant {
    public Graceful() {
        super(
                new EcoEnchantBuilder("graceful", EnchantmentType.NORMAL, Target.Applicable.BOOTS, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onFall(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isOnGround())
            return;

        if(player.getLocation().clone().add(0, -1, 0).getBlock().getType().equals(Material.AIR))
            return;

        if(!EnchantChecks.boots(player, this)) return;
        int level = EnchantChecks.getBootsLevel(player, this);

        if (Rand.randFloat(0, 1) > level * 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level"))
            return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 1, false, false, false));
    }
}
