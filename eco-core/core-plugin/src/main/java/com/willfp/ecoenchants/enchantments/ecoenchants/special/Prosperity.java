package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.core.events.ArmorChangeEvent;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Prosperity extends EcoEnchant {
    private final AttributeModifier modifier = new AttributeModifier(UUID.nameUUIDFromBytes("prosperity".getBytes()), this.getKey().getKey(), 1, AttributeModifier.Operation.ADD_NUMBER);

    public Prosperity() {
        super(
                "prosperity", EnchantmentType.SPECIAL
        );
    }

    @EventHandler
    public void onArmorEquip(@NotNull final ArmorChangeEvent event) {
        Player player = event.getPlayer();

        if (!this.areRequirementsMet(player)) {
            return;
        }

        int points = EnchantChecks.getArmorPoints(player, this);

        AttributeInstance inst = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        assert inst != null;

        inst.setBaseValue(inst.getDefaultValue());

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            points = 0;
        }

        inst.removeModifier(modifier);

        if (player.getHealth() >= inst.getValue()) {
            this.getPlugin().getScheduler().runLater(() -> {
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            }, 1);
        }

        if (points > 0) {
            inst.addModifier(
                    new AttributeModifier(
                            UUID.nameUUIDFromBytes("prosperity".getBytes()),
                            this.getKey().getKey(),
                            this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "health-per-point") * points,
                            AttributeModifier.Operation.ADD_NUMBER
                    )
            );
        }
    }
}
