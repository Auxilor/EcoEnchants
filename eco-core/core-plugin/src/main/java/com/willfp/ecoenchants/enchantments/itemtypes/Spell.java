package com.willfp.ecoenchants.enchantments.itemtypes;

import com.willfp.eco.core.Prerequisite;
import com.willfp.eco.core.integrations.placeholder.PlaceholderEntry;
import com.willfp.eco.core.integrations.placeholder.PlaceholderManager;
import com.willfp.eco.util.StringUtils;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.SpellActivateEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Tag;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("deprecation")
public abstract class Spell extends EcoEnchant {
    /**
     * Items that must be left-clicked to activate spells for.
     */
    private static final List<Material> LEFT_CLICK_ITEMS = Arrays.asList(
            Material.FISHING_ROD,
            Material.BOW,
            Material.CROSSBOW,
            Material.TRIDENT
    );

    /**
     * Items that don't cause spells to activate when right clicked.
     */
    private static final List<Material> BLACKLIST_CLICKED_BLOCKS = new ArrayList<>(Arrays.asList(
            Material.CRAFTING_TABLE,
            Material.GRINDSTONE,
            Material.ENCHANTING_TABLE,
            Material.FURNACE,
            Material.SMITHING_TABLE,
            Material.LEVER,
            Material.REPEATER,
            Material.COMPARATOR,
            Material.RESPAWN_ANCHOR,
            Material.NOTE_BLOCK,
            Material.ITEM_FRAME,
            Material.CHEST,
            Material.BARREL,
            Material.BEACON,
            Material.LECTERN,
            Material.FLETCHING_TABLE,
            Material.SMITHING_TABLE,
            Material.STONECUTTER,
            Material.SMOKER,
            Material.BLAST_FURNACE,
            Material.BREWING_STAND,
            Material.DISPENSER,
            Material.DROPPER,
            Material.FIRE
    ));

    static {
        BLACKLIST_CLICKED_BLOCKS.addAll(Tag.BUTTONS.getValues());
        BLACKLIST_CLICKED_BLOCKS.addAll(Tag.BEDS.getValues());
        BLACKLIST_CLICKED_BLOCKS.addAll(Tag.DOORS.getValues());
        BLACKLIST_CLICKED_BLOCKS.addAll(Tag.FENCE_GATES.getValues());
        BLACKLIST_CLICKED_BLOCKS.addAll(Tag.TRAPDOORS.getValues());
        BLACKLIST_CLICKED_BLOCKS.addAll(Tag.ANVIL.getValues());
        BLACKLIST_CLICKED_BLOCKS.addAll(Tag.SHULKER_BOXES.getValues());
    }

    /**
     * The cooldown end times linked to players.
     */
    private final Map<UUID, Long> tracker = new HashMap<>();
    /**
     * Players currently running spells - prevents listener firing twice.
     */
    private final Set<UUID> preventDuplicateList = new HashSet<>();

    /**
     * Create a new spell enchantment.
     *
     * @param key           The key name of the enchantment
     * @param prerequisites Optional {@link Prerequisite}s that must be met
     */
    protected Spell(@NotNull final String key,
                    @NotNull final Prerequisite... prerequisites) {
        super(key, EnchantmentType.SPELL, prerequisites);

        PlaceholderManager.registerPlaceholder(
                new PlaceholderEntry(
                        this.getPermissionName() + "_" + "cooldown",
                        player -> StringUtils.internalToString(getCooldown(this, player))
                )
        );
    }

    /**
     * Utility method to get a player's cooldown time of a specific spell.
     *
     * @param spell  The spell to query.
     * @param player The player to query.
     * @return The time left in seconds before next use.
     */
    public static int getCooldown(@NotNull final Spell spell,
                                  @NotNull final Player player) {
        if (!spell.tracker.containsKey(player.getUniqueId())) {
            return 0;
        }

        long msLeft = spell.tracker.get(player.getUniqueId()) - System.currentTimeMillis();

        long secondsLeft = (long) Math.ceil((double) msLeft / 1000);

        return NumberConversions.toInt(secondsLeft);
    }

    /**
     * Get a multiplier for a spell cooldown.
     * <p>
     * Used for perks..
     *
     * @param player The player to query.
     * @return The multiplier.
     */
    public static double getCooldownMultiplier(@NotNull final Player player) {
        if (player.hasPermission("ecoenchants.cooldowntime.quarter")) {
            return 0.25;
        }

        if (player.hasPermission("ecoenchants.cooldowntime.third")) {
            return 0.33;
        }

        if (player.hasPermission("ecoenchants.cooldowntime.half")) {
            return 0.5;
        }

        if (player.hasPermission("ecoenchants.cooldowntime.75")) {
            return 0.75;
        }

        String prefix = "ecoenchants.cooldowntime.";
        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
            String permission = permissionAttachmentInfo.getPermission();
            if (permission.startsWith(prefix)) {
                try {
                    return Double.parseDouble(permission.substring(permission.lastIndexOf(".") + 1)) / 100;
                } catch (NumberFormatException e) {
                    return 1;
                }
            }
        }

        return 1;
    }

    /**
     * Get the cooldown time of the spell (in seconds).
     *
     * @return The time, in seconds.
     */
    public int getCooldownTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "cooldown");
    }

    /**
     * Get the sound to be played on activation.
     *
     * @return The sound.
     */
    public final Sound getActivationSound() {
        return Sound.valueOf(this.getConfig().getString(EcoEnchants.CONFIG_LOCATION + "activation-sound").toUpperCase());
    }

    /**
     * Listener called on spell activation.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onUseEventHandler(@NotNull final PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (preventDuplicateList.contains(player.getUniqueId())) {
            return;
        }
        preventDuplicateList.add(player.getUniqueId());
        this.getPlugin().getScheduler().runLater(() -> preventDuplicateList.remove(player.getUniqueId()), 2);

        if (player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (LEFT_CLICK_ITEMS.contains(player.getInventory().getItemInMainHand().getType())) {
            if (!(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
                return;
            }
            if (requiresBlockClick() && !event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                return;
            }
        } else {
            if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
                return;
            }
            if (requiresBlockClick() && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                return;
            }
        }

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        int level = EnchantChecks.getMainhandLevel(player, this);
        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        int cooldown = getCooldown(this, player);

        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getState() instanceof Container
                    || event.getClickedBlock().getState() instanceof BlockInventoryHolder
                    || BLACKLIST_CLICKED_BLOCKS.contains(event.getClickedBlock().getType())) {
                return;
            }
        }

        if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "not-while-sneaking")) {
            if (player.isSneaking()) {
                return;
            }
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (cooldown > 0) {
            if (!this.hasFlag("no-cooldown-message")) {
                if (this.getPlugin().getConfigYml().getBool("types.special.cooldown-in-actionbar")) {
                    String message = this.getPlugin().getLangYml().getFormattedString("messages.on-cooldown")
                            .replace("%seconds%", String.valueOf(cooldown))
                            .replace("%name%", EnchantmentCache.getEntry(this).getRawName());

                    player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(message)
                    );
                } else {
                    String message = this.getPlugin().getLangYml().getMessage("on-cooldown")
                            .replace("%seconds%", String.valueOf(cooldown))
                            .replace("%name%", EnchantmentCache.getEntry(this).getRawName());
                    player.sendMessage(message);
                }
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 0.5f);
            }
        } else {
            SpellActivateEvent spellActivateEvent = new SpellActivateEvent(player, this);
            Bukkit.getPluginManager().callEvent(spellActivateEvent);

            if (!spellActivateEvent.isCancelled()) {
                if (onUse(player, level, event)) {
                    if (!this.hasFlag("no-use-message")) {
                        String message = this.getPlugin().getLangYml().getMessage("used-spell").replace("%name%", EnchantmentCache.getEntry(this).getRawName());
                        player.sendMessage(message);
                        player.playSound(player.getLocation(), this.getActivationSound(), SoundCategory.PLAYERS, 1, 1);
                    }

                    tracker.remove(player.getUniqueId());
                    tracker.put(player.getUniqueId(), System.currentTimeMillis() + (long) ((this.getCooldownTime() * 1000L) * Spell.getCooldownMultiplier(player)));
                }
            }
        }
    }

    /**
     * Get if the spell requires a block to be clicked to trigger the spell.
     *
     * @return If the spell requires a block to be clicked.
     */
    protected boolean requiresBlockClick() {
        return false;
    }

    /**
     * Actual spell-specific implementations; the functionality.
     *
     * @param player The player who triggered the spell.
     * @param level  The level of the spell on the item.
     * @param event  The event that activated the spell.
     * @return If the spell should be activated.
     */
    public abstract boolean onUse(@NotNull Player player,
                                  int level,
                                  @NotNull PlayerInteractEvent event);
}
