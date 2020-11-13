package com.willfp.ecoenchants.enchantments.util;

import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import com.willfp.ecoenchants.util.Callable;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpellRunnable {
    private final UUID player;
    private final Spell spell;
    private long endTime = 0;
    private Callable callable = () -> {};

    public SpellRunnable(Player player, Spell spell) {
        this.player = player.getUniqueId();
        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
    }

    public void run() {
        callable.call();
        updateEndTime();
    }

    public long getEndTime() {
        return endTime;
    }

    public void updateEndTime() {
        endTime = System.currentTimeMillis() + (spell.getCooldownTime()*1000);
    }

    public void setTask(Callable callable) {
        this.callable = callable;
    }
}
