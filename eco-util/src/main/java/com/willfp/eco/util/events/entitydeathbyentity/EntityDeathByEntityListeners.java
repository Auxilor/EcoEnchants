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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class EntityDeathByEntityListeners extends PluginDependent implements Listener {
    final Set<EntityDeathByEntityBuilder> events = new HashSet<>();

    public EntityDeathByEntityListeners(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;

        LivingEntity victim = (LivingEntity) event.getEntity();

        if (victim.getHealth() > event.getFinalDamage()) return;

        EntityDeathByEntityBuilder builtEvent = new EntityDeathByEntityBuilder();
        builtEvent.setVictim(victim);
        builtEvent.setDamager(event.getDamager());
        events.add(builtEvent);

        new BukkitRunnable() {
            @Override
            public void run() {
                events.remove(builtEvent);
            }
        }.runTaskLater(this.plugin, 1);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity victim = event.getEntity();

        List<ItemStack> drops = event.getDrops();
        int xp = event.getDroppedExp();

        AtomicReference<EntityDeathByEntityBuilder> atomicBuiltEvent = new AtomicReference<>(null);
        EntityDeathByEntityBuilder builtEvent;

        events.forEach((deathByEntityEvent) -> {
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
