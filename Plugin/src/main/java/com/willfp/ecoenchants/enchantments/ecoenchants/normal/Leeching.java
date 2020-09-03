package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Leeching extends EcoEnchant {
    public Leeching() {
        super(
                new EcoEnchantBuilder("leeching", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void leechingHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if (event.isCancelled())
            return;

        Player player = (Player) event.getDamager();

        if(!AntigriefManager.canInjure(player, (LivingEntity) event.getEntity())) return;

        if (!EnchantChecks.mainhand(player, this)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

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
