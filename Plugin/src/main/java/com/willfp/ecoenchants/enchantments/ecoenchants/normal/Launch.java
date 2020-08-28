package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
public class Launch extends EcoEnchant {
    public Launch() {
        super(
                new EcoEnchantBuilder("launch", EnchantmentType.NORMAL, Target.Applicable.ELYTRA, 4.0)
        );
    }

    // START OF LISTENERS
    @EventHandler
    public void onFireworkUse(PlayerInteractEvent event) {
        if(event.getItem() == null) return;

        if(!event.getItem().getType().equals(Material.FIREWORK_ROCKET))
            return;

        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR))
            return;

        Player player = event.getPlayer();

        if(!player.isGliding())
            return;

        if(!HasEnchant.playerElytra(player, this)) return;

        int level = HasEnchant.getPlayerChestplateLevel(player, this);
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double boost = 1 + (multiplier * level);

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> player.setVelocity(player.getVelocity().multiply(boost)), 1);
    }
}
