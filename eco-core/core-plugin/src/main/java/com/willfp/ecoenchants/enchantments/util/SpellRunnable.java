package com.willfp.ecoenchants.enchantments.util;


import com.willfp.eco.util.lambda.Callable;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.entity.Player;

public class SpellRunnable {
    private final Spell spell;
    private final Player player;
    private long endTime = 0;
    private Callable callable = () -> {
    };

    public SpellRunnable(Spell spell, Player player) {
        this.spell = spell;
        this.player = player;
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
        endTime = System.currentTimeMillis() + (long) ((spell.getCooldownTime() * 1000L) * Spell.getCooldownMultiplier(player));
    }

    public void setTask(Callable callable) {
        this.callable = callable;
    }
}
