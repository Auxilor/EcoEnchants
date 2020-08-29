package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Lightning;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Electroshock extends EcoEnchant {
    public Electroshock() {
        super(
                new EcoEnchantBuilder("electroshock", EnchantmentType.NORMAL, Target.Applicable.SHIELD, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onElectroshock(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

        if(!(event.getDamager() instanceof LivingEntity))
            return;

        Player player = (Player) event.getEntity();

        LivingEntity victim = (LivingEntity) event.getDamager();

        if(!player.isBlocking()) return;

        if(!AntigriefManager.canInjure(player, victim)) return;

        int level;
        if(!HasEnchant.playerOffhand(player, this) && !HasEnchant.playerHeld(player, this)) return;
        if(HasEnchant.playerOffhand(player, this)) level = HasEnchant.getPlayerOffhandLevel(player, this);
        else level = HasEnchant.getPlayerLevel(player, this);

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");
        double damage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage");

        double finalChance = (chance * level) / 100;
        if(Rand.randFloat(0, 1) > finalChance) return;

        Lightning.strike(victim, damage);
    }
}
