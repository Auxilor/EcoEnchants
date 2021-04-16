package com.willfp.ecoenchants.mmo.enchants.stamina;

import com.willfp.eco.core.events.ArmorEquipEvent;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.mmo.structure.MMOEnchantment;
import net.mmogroup.mmolib.MMOLib;
import net.mmogroup.mmolib.api.player.MMOPlayerData;
import net.mmogroup.mmolib.api.stat.SharedStat;
import net.mmogroup.mmolib.api.stat.modifier.StatModifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Athletic extends MMOEnchantment {
    private static final String KEY = "ecoenchants_bonus_stamina";

    public Athletic() {
        super("athletic", EnchantmentType.NORMAL);
    }

    @Override
    public void onArmorEquip(@NotNull Player player, int level, @NotNull ArmorEquipEvent event) {
        MMOPlayerData data = MMOPlayerData.get(player);

        data.getStatMap().getInstance(SharedStat.MAX_STAMINA).remove(KEY);

        if (level == 0) {
            MMOLib.plugin.getStats().runUpdates(data.getStatMap());
            return;
        }

        int stamina = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "stamina-per-level") * level;

        data.getStatMap().getInstance(SharedStat.MAX_STAMINA).addModifier(KEY, new StatModifier(stamina));

        MMOLib.plugin.getStats().runUpdates(data.getStatMap());
    }
}
