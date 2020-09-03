package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
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
        if (!(event.getEntity() instanceof Arrow))
            return;

        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        if(event.getHitBlock() == null)
            return;

        Block block = event.getHitBlock();
        Arrow arrow = (Arrow) event.getEntity();
        Player player = (Player) event.getEntity().getShooter();

        if (!EnchantChecks.arrow(arrow, this)) return;

        int level = EnchantChecks.getArrowLevel(arrow, this);

        float power = (float) (0.5 + (level * 0.5));

        if (!AntigriefManager.canBreakBlock(player, block))
            return;

        if (NumberUtils.randFloat(0, 1) > level * 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level"))
            return;

        BlockFace face = event.getHitBlockFace();

        assert face != null;

        block.getRelative(face).setType(Material.FIRE);
    }
}