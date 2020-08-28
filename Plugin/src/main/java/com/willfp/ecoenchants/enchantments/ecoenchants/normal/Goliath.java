package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Goliath extends EcoEnchant {
    public Goliath() {
        super(
                new EcoEnchantBuilder("goliath", EnchantmentType.NORMAL, Target.Applicable.SWORD, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void goliathHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;

        Player player = (Player) event.getDamager();

        LivingEntity victim = (LivingEntity) event.getEntity();

        if (!HasEnchant.playerHeld(player, this)) return;

        if (victim.getHealth() <= player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
            return;

        int level = HasEnchant.getPlayerLevel(player, this);
        double timesMoreHealth = victim.getHealth() / player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level");
        event.setDamage(event.getDamage() * ((level * multiplier * timesMoreHealth) + 1));
    }
}
