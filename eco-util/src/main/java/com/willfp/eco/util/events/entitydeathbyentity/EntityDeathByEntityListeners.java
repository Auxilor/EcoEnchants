package com.willfp.eco.util.events.entitydeathbyentity;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class EntityDeathByEntityListeners extends PluginDependent implements Listener {
    /**
     * The events currently being built.
     */
    private final Set<EntityDeathByEntityBuilder> events = new HashSet<>();

    /**
     * Create a listener associated with an {@link AbstractEcoPlugin}.
     *
     * @param plugin The plugin to associate with.
     */
    @ApiStatus.Internal
    public EntityDeathByEntityListeners(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Called when an entity is damaged by another entity.
     * Used to find the damager.
     *
     * @param event The event to listen for.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(@NotNull final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;

        LivingEntity victim = (LivingEntity) event.getEntity();

        if (victim.getHealth() > event.getFinalDamage()) return;

        EntityDeathByEntityBuilder builtEvent = new EntityDeathByEntityBuilder();
        builtEvent.setVictim(victim);
        builtEvent.setDamager(event.getDamager());
        events.add(builtEvent);

        this.getPlugin().getScheduler().runLater(() -> events.remove(builtEvent), 1);
    }

    /**
     * Called when an entity is killed.
     * Used to find the killer and associate the event.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onEntityDeath(@NotNull final EntityDeathEvent event) {
        LivingEntity victim = event.getEntity();

        List<ItemStack> drops = event.getDrops();
        int xp = event.getDroppedExp();

        AtomicReference<EntityDeathByEntityBuilder> atomicBuiltEvent = new AtomicReference<>(null);
        EntityDeathByEntityBuilder builtEvent;

        events.forEach(deathByEntityEvent -> {
            if (deathByEntityEvent.getVictim().equals(victim)) {
                atomicBuiltEvent.set(deathByEntityEvent);
            }
        });

        if (atomicBuiltEvent.get() == null) return;

        builtEvent = atomicBuiltEvent.get();
        events.remove(builtEvent);
        builtEvent.setDrops(drops);
        builtEvent.setXp(xp);
        builtEvent.setDeathEvent(event);

        builtEvent.push();
    }
}
