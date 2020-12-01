package com.willfp.ecoenchants.enchantments.util;

import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import com.willfp.ecoenchants.util.interfaces.Callable;

public class SpellRunnable {
    private final Spell spell;
    private long endTime = 0;
    private Callable callable = () -> {};

    public SpellRunnable(Spell spell) {
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
        endTime = System.currentTimeMillis() + (spell.getCooldownTime()* 1000L);
    }

    public void setTask(Callable callable) {
        this.callable = callable;
    }
}
