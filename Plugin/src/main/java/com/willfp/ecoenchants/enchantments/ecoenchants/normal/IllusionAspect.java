package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.checks.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Cooldown;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
public class IllusionAspect extends EcoEnchant {
    public IllusionAspect() {
        super(
                new EcoEnchantBuilder("illusion_aspect", EnchantmentType.NORMAL, Target.Applicable.SWORD, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void illusionAspectHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getDamager();

        Player victim = (Player) event.getEntity();

        if(!AntigriefManager.canInjure(player, victim)) return;

        if (!EnchantChecks.mainhand(player, this)) return;

        if (Cooldown.getCooldown(player) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged"))
            return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");
        if (Rand.randFloat(0, 1) > level * 0.01 * chance)
            return;

        victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, level * 10 + 15, level));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, level * 10 + 15, level));
    }
}
