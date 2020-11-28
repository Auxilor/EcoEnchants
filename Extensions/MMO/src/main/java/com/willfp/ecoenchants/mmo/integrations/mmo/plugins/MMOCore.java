package com.willfp.ecoenchants.mmo.integrations.mmo.plugins;

import com.willfp.ecoenchants.mmo.integrations.mmo.MMOIntegration;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.stats.StatType;
import org.bukkit.entity.Player;

public class MMOCore implements MMOIntegration {
    @Override
    public double getMana(Player player) {
        return PlayerData.get(player).getMana();
    }

    @Override
    public void setMana(Player player, double amount) {
        PlayerData.get(player).setMana(amount);
    }

    @Override
    public void giveMana(Player player, double amount) {
        PlayerData.get(player).giveMana(amount);
    }

    @Override
    public double getMaxMana(Player player) {
        return PlayerData.get(player).getStats().getStat(StatType.MAX_MANA);
    }

    @Override
    public double getStamina(Player player) {
        return PlayerData.get(player).getStamina();
    }

    @Override
    public void setStamina(Player player, double amount) {
        PlayerData.get(player).setStamina(amount);
    }

    @Override
    public void giveStamina(Player player, double amount) {
        PlayerData.get(player).giveStamina(amount);
    }

    @Override
    public double getMaxStamina(Player player) {
        return PlayerData.get(player).getStats().getStat(StatType.MAX_STAMINA);
    }

    @Override
    public String getPluginName() {
        return "MMOCore";
    }
}
