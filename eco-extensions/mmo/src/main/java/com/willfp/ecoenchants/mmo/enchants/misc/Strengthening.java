package com.willfp.ecoenchants.mmo.enchants.misc;

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

public class Strengthening extends MMOEnchantment {
    private static final String KEY = "ecoenchants_bonus_strength";

    public Strengthening() {
        super("strengthening", EnchantmentType.NORMAL);
    }

    @Override
    public void onArmorEquip(@NotNull Player player, int level, @NotNull ArmorEquipEvent event) {
        MMOPlayerData data = MMOPlayerData.get(player);

        data.getStatMap().getInstance(SharedStat.ATTACK_DAMAGE).remove(KEY);

        if (level == 0) {
            MMOLib.plugin.getStats().runUpdates(data.getStatMap());
            return;
        }

        double multiplier = (this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "multiplier") * level) * data.getStatMap().getInstance(SharedStat.ATTACK_DAMAGE).getBase();

        data.getStatMap().getInstance(SharedStat.ATTACK_DAMAGE).addModifier(KEY, new StatModifier(multiplier));

        MMOLib.plugin.getStats().runUpdates(data.getStatMap());
    }
}
