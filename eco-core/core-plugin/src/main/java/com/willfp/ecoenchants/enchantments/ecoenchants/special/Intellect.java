package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.core.events.NaturalExpGainEvent;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Intellect extends EcoEnchant {

    private static final List<Player> toPrevent = new ArrayList<>();

    public Intellect() {
        super(
                "intellect", EnchantmentType.SPECIAL
        );
    }

    @EventHandler
    public void onExpChange(@NotNull final NaturalExpGainEvent event) {
        Player player = event.getExpChangeEvent().getPlayer();

        if (event.getExpChangeEvent().getAmount() < 0) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        int level = EnchantChecks.getMainhandLevel(player, this);

        if (level == 0) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        int newValue = toPrevent.contains(player) ? event.getExpChangeEvent().getAmount() :
        (int) Math.ceil(event.getExpChangeEvent().getAmount() *
                (1 + (level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bonus-per-point"))));

        event.getExpChangeEvent().setAmount(newValue);

        if (newValue > player.getExpToLevel()) {
            if (!toPrevent.contains(player)){
                toPrevent.add(player);
            }
        } else {
            toPrevent.remove(player);
        }
    }
}
