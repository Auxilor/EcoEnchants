package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class HarmlessnessCurse extends EcoEnchant {
    public HarmlessnessCurse() {
        super(
                new EcoEnchantBuilder("harmlessness_curse", EnchantmentType.CURSE,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if (NumberUtils.randFloat(0, 1) > 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance"))
            return;

        event.setDamage(0);
        event.setCancelled(true);
    }
}
