package com.willfp.ecoenchants.enchantments.util;


import com.willfp.eco.util.lambda.Callable;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpellRunnable {
    private final Spell spell;
    private final Player player;
    private long endTime = 0;
    private Callable callable = () -> {
        // Empty as must be set using this#setTask
    };

    public SpellRunnable(@NotNull final Spell spell,
                         @NotNull final Player player) {
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

    public void setTask(@NotNull final Callable callable) {
        this.callable = callable;
    }
}
