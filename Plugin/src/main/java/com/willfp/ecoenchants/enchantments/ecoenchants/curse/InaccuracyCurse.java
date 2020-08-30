package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class InaccuracyCurse extends EcoEnchant {
    public InaccuracyCurse() {
        super(
                new EcoEnchantBuilder("inaccuracy_curse", EnchantmentType.CURSE, Target.Applicable.BOW, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getProjectile().getType() != EntityType.ARROW)
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (!EnchantChecks.mainhand(player, this)) return;

        double spread = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "spread");

        Vector velocity = event.getProjectile().getVelocity().clone();

        velocity.add(new Vector(Rand.randFloat(-spread, spread), Rand.randFloat(-spread, spread), Rand.randFloat(-spread, spread)));
        event.getProjectile().setVelocity(velocity);
    }
}
