package com.willfp.ecoenchants.enchantments.itemtypes;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.SpellRunnable;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Wrapper for Spell enchantments
 */
public abstract class Spell extends EcoEnchant {
    private final HashMap<UUID, SpellRunnable> cooldownTracker = new HashMap<>();
    private final Set<UUID> runningSpell = new HashSet<>();
    private static final List<Material> leftClickItems = Arrays.asList(
            Material.FISHING_ROD,
            Material.BOW
    );

    protected Spell(String key, Prerequisite... prerequisites) {
        this(key, EcoEnchantsPlugin.class, prerequisites);
    }

    protected Spell(String key, Class<?> plugin, Prerequisite... prerequisites) {
        super(key, EnchantmentType.SPELL, plugin, prerequisites);
    }

    public int getCooldownTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "cooldown");
    }

    public final Sound getActivationSound() {
        return Sound.valueOf(this.getConfig().getString(EcoEnchants.CONFIG_LOCATION + "activation-sound").toUpperCase());
    }

    @EventHandler
    public void onUseEventHandler(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(runningSpell.contains(player.getUniqueId())) return;
        runningSpell.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> runningSpell.remove(player.getUniqueId()), 5);

        if(leftClickItems.contains(player.getInventory().getItemInMainHand().getType())) {
            if(!(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
                return;
            }
        } else {
            if(!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
                return;
            }
        }

        if(!EnchantChecks.mainhand(player, this))
            return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        if(!cooldownTracker.containsKey(player.getUniqueId()))
            cooldownTracker.put(player.getUniqueId(), new SpellRunnable(player, this));

        SpellRunnable runnable = cooldownTracker.get(player.getUniqueId());
        runnable.setTask(() -> {
            this.onUse(player, level, event);
        });

        int cooldown = getCooldown(this, player);

        if(cooldown > 0) {
            String message = ConfigManager.getLang().getMessage("on-cooldown").replaceAll("%seconds%", String.valueOf(cooldown)).replaceAll("%name%", EnchantmentCache.getEntry(this).getRawName());
            player.sendMessage(message);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0.5f);
            return;
        }

        String message = ConfigManager.getLang().getMessage("used-spell").replaceAll("%name%", EnchantmentCache.getEntry(this).getRawName());
        player.sendMessage(message);
        player.playSound(player.getLocation(), this.getActivationSound(), SoundCategory.PLAYERS, 1, 1);
        runnable.run();
    }

    public abstract void onUse(Player player, int level, PlayerInteractEvent event);

    public static int getCooldown(Spell spell, Player player) {
        if(!spell.cooldownTracker.containsKey(player.getUniqueId()))
            spell.cooldownTracker.put(player.getUniqueId(), new SpellRunnable(player, spell));

        SpellRunnable runnable = spell.cooldownTracker.get(player.getUniqueId());

        long msLeft = runnable.getEndTime() - System.currentTimeMillis();

        long secondsLeft = (long) Math.ceil((double) msLeft / 1000);

        return new Long(secondsLeft).intValue();
    }
}
