package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Frenzy extends EcoEnchant {
    public Frenzy() {
        super(
                "frenzy", EnchantmentType.SPECIAL
        );
    }
    // START OF LISTENERS

    @EventHandler
    public void onFrenzyKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null)
            return;

        Player player = event.getEntity().getKiller();

        if (!EnchantChecks.mainhand(player, this)) return;

        if(this.getDisabledWorlds().contains(player.getWorld())) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        int duration = (int) (level * 20 * this.getConfig().getDouble((EcoEnchants.CONFIG_LOCATION + "seconds-per-level")));

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, level, true, true, true));
    }
}
