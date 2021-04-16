package com.willfp.ecoenchants.mmo.enchants.abilities;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.mmo.MMOPrerequisites;
import com.willfp.ecoenchants.mmo.structure.MMOEnchantment;
import net.Indyuce.mmoitems.api.event.AbilityUseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Recover extends MMOEnchantment {
    public Recover() {
        super("recover", EnchantmentType.NORMAL, MMOPrerequisites.HAS_MMOITEMS);
    }

    @EventHandler
    public void onAbility(AbilityUseEvent event) {
        Player player = event.getPlayer();

        if (!EnchantChecks.mainhand(player, this))
            return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        double cooldown = event.getAbility().getModifier("cooldown");

        if (cooldown == 0.0D) return;

        double multiplier = 1 - (this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier") * level);
        cooldown *= multiplier;

        event.getAbility().setModifier("cooldown", cooldown);
    }
}
