package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;

@SuppressWarnings("deprecation")
public class Aerial extends EcoEnchant {
    public Aerial() {
        super(
                new EcoEnchantBuilder("aerial", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.BOW, Target.Applicable.CROSSBOW}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (event.getEntityType() != EntityType.ARROW)
            return;

        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        Player player = (Player) event.getEntity().getShooter();

        if(player.isOnGround())
            return;

        if (!HasEnchant.playerHeld(player, this)) return;
        int level = HasEnchant.getPlayerLevel(player, this);

        if (!(event.getEntity() instanceof Arrow)) return;
        Arrow a = (Arrow) event.getEntity();
        a.setMetadata("from-aerial", new FixedMetadataValue(Main.getInstance(), level));
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow))
            return;

        if (!event.getDamager().hasMetadata("from-aerial"))
            return;

        int level = event.getDamager().getMetadata("from-aerial").get(0).asInt();

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level");
        double reduction = 1 + (multiplier * level);
        event.setDamage(damage * reduction);
    }
}
