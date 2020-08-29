package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Weakening extends EcoEnchant {
    public Weakening() {
        super(
                new EcoEnchantBuilder("weakening", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.SWORD, Target.Applicable.AXE}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        if (!(event.getEntity() instanceof LivingEntity))
            return;

        Player player = (Player) event.getDamager();

        LivingEntity victim = (LivingEntity) event.getEntity();

        if (!AntigriefManager.canInjure(player, victim)) return;

        if (!HasEnchant.playerHeld(player, this)) return;

        int level = HasEnchant.getPlayerLevel(player, this);

        int ticksPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level");
        int ticks = ticksPerLevel * level;

        victim.setMetadata("weak", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), true));

        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
            victim.removeMetadata("weak", EcoEnchantsPlugin.getInstance());
        }, ticks);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof LivingEntity))
            return;

        LivingEntity victim = (LivingEntity) event.getEntity();

        if(!victim.hasMetadata("weak"))
            return;

        event.setDamage(event.getDamage() * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier-while-weak"));
    }
}
