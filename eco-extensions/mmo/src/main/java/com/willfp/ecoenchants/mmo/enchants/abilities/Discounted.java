package com.willfp.ecoenchants.mmo.enchants.abilities;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.mmo.MMOPrerequisites;
import com.willfp.ecoenchants.mmo.structure.MMOEnchantment;
import net.Indyuce.mmoitems.api.event.AbilityUseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Discounted extends MMOEnchantment {
    public Discounted() {
        super("discounted", EnchantmentType.NORMAL, MMOPrerequisites.HAS_MMOITEMS);
    }

    @EventHandler
    public void onAbility(AbilityUseEvent event) {
        Player player = event.getPlayer();

        if (!EnchantChecks.mainhand(player, this))
            return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        double cost = event.getAbility().getModifier("mana");
        if (cost == 0.0D) return;

        double multiplier = 1 - (this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier") * level);
        cost *= multiplier;

        event.getAbility().setModifier("mana", cost);
    }
}
