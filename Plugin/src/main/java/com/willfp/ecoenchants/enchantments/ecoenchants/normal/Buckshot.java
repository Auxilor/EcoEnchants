package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
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
public class Buckshot extends EcoEnchant {
    public Buckshot() {
        super(
                new EcoEnchantBuilder("buckshot", EnchantmentType.NORMAL, Target.Applicable.BOW, 4.0)
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

        if (!HasEnchant.playerHeld(player, this)) return;
        int level = HasEnchant.getPlayerLevel(player, this);

        event.getProjectile().remove();
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f);

        int numberPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "amount-per-level");
        int number = numberPerLevel * level;
        double spread = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "spread-per-level");
        spread *= level;

        for (int i = 0; i < number; i += 1) {

            Vector velocity = event.getProjectile().getVelocity().clone();

            velocity.add(new Vector(Rand.randFloat(-spread, spread), Rand.randFloat(-spread, spread), Rand.randFloat(-spread, spread)));

            Arrow arrow = player.launchProjectile(Arrow.class, velocity);
            if(HasEnchant.playerHeld(player, Enchantment.ARROW_FIRE)) arrow.setFireTicks(Integer.MAX_VALUE);
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        }
    }
}
