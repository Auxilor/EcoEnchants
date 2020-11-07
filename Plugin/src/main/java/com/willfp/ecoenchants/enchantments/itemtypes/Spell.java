package com.willfp.ecoenchants.enchantments.itemtypes;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.SpellRunnable;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Wrapper for Spell enchantments
 */
public abstract class Spell extends EcoEnchant {
    private final HashMap<UUID, SpellRunnable> cooldownTracker = new HashMap<>();

    protected Spell(String key, Prerequisite... prerequisites) {
        super(key, EnchantmentType.SPELL, prerequisites);
    }

    public int getCooldownTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "cooldown");
    }

    @EventHandler
    public void onRightClickEventHandler(PlayerInteractEvent event) {
        if(!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            return;

        Player player = event.getPlayer();

        if(!EnchantChecks.mainhand(player, this))
            return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        if(!cooldownTracker.containsKey(player.getUniqueId()))
            cooldownTracker.put(player.getUniqueId(), new SpellRunnable(player, this));

        SpellRunnable runnable = cooldownTracker.get(player.getUniqueId());
        runnable.setTask(() -> {
            this.onRightClick(player, level, event);
        });

        long msLeft = runnable.getEndTime() - System.currentTimeMillis();

        long secondsLeft = (long) Math.ceil((double) msLeft / 1000);

        if(msLeft > 0) {
            String message = ConfigManager.getLang().getMessage("on-cooldown").replaceAll("%seconds%", String.valueOf(secondsLeft)).replaceAll("%name%", EnchantmentCache.getEntry(this).getRawName());
            player.sendMessage(message);
            return;
        }

        String message = ConfigManager.getLang().getMessage("used-spell").replaceAll("%name%", EnchantmentCache.getEntry(this).getRawName());
        player.sendMessage(message);
        runnable.run();
    }

    public abstract void onRightClick(Player player, int level, PlayerInteractEvent event);
}
