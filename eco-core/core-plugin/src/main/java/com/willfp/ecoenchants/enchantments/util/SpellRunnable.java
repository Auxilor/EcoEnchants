package com.willfp.ecoenchants.enchantments.util;


import com.willfp.eco.util.lambda.Callable;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpellRunnable {
    /**
     * The spell that this runnable is for.
     */
    @Getter
    private final Spell spell;

    /**
     * The player that this runnable is for.
     */
    private final Player player;

    /**
     * The end time of the runnable, in unix time.
     */
    @Getter
    private long endTime = 0;

    /**
     * The actual task to be executed.
     * <p>
     * Must be set before execution.
     */
    @Setter
    private Callable callable = () -> {
        // Empty as must be set using this#setTask
    };

    /**
     * Create a new Spell Runnable.
     *
     * @param spell  The spell.
     * @param player The player.
     */
    public SpellRunnable(@NotNull final Spell spell,
                         @NotNull final Player player) {
        this.spell = spell;
        this.player = player;
    }

    /**
     * Run the runnable.
     */
    public void run() {
        callable.call();
        updateEndTime();
    }

    /**
     * Update the end time of the spell runnable.
     */
    public void updateEndTime() {
        endTime = System.currentTimeMillis() + (long) ((spell.getCooldownTime() * 1000L) * Spell.getCooldownMultiplier(player));
    }
}
