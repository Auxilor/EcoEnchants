package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Sycophant extends EcoEnchant {
    public Sycophant() {
        super(
                new EcoEnchantBuilder("sycophant", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onBlock(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if(!player.isBlocking()) return;

        int level;
        if (!EnchantChecks.offhand(player, this) && !EnchantChecks.mainhand(player, this)) return;
        if(EnchantChecks.offhand(player, this)) level = EnchantChecks.getOffhandLevel(player, this);
        else level = EnchantChecks.getMainhandLevel(player, this);

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level");
        double amountToHeal = damage * level * multiplier;
        double newHealth = player.getHealth() + amountToHeal;
        if (newHealth > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            newHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        }
        player.setHealth(newHealth);
    }
}
