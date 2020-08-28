package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
public class Ignite extends EcoEnchant {
    public Ignite() {
        super(
                new EcoEnchantBuilder("ignite", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.BOW, Target.Applicable.CROSSBOW}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onLand(ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.ARROW)
            return;

        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        if(event.getHitBlock() == null)
            return;

        Block block = event.getHitBlock();

        Player player = (Player) event.getEntity().getShooter();

        if (!HasEnchant.playerHeld(player, this)) return;

        if (!(event.getEntity() instanceof Arrow)) return;

        int level = HasEnchant.getPlayerLevel(player, this);

        float power = (float) (0.5 + (level * 0.5));

        if (!AntigriefManager.canBreakBlock(player, block))
            return;

        if (Rand.randFloat(0, 1) > level * 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level"))
            return;

        BlockFace face = event.getHitBlockFace();

        assert face != null;

        block.getRelative(face).setType(Material.FIRE);
    }
}