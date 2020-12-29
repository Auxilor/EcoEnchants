package com.willfp.eco.util.events.armorequip;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.willfp.eco.util.events.armorequip.ArmorEquipEvent.EquipMethod;

/**
 * @author Arnah
 * @since Jul 30, 2015
 */
@SuppressWarnings("deprecation")
public class ArmorListener implements Listener {
    //Event Priority is highest because other plugins might cancel the events before we check.

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public final void inventoryClick(@NotNull final InventoryClickEvent event) {
        boolean shift = false;
        boolean numberkey = false;
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() == InventoryAction.NOTHING) {
            return;
        }
        if (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
            shift = true;
        }
        if (event.getClick().equals(ClickType.NUMBER_KEY)) {
            numberkey = true;
        }
        if (event.getSlotType() != SlotType.ARMOR && event.getSlotType() != SlotType.QUICKBAR && event.getSlotType() != SlotType.CONTAINER) {
            return;
        }
        if (event.getClickedInventory() != null && !event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            return;
        }
        if (!event.getInventory().getType().equals(InventoryType.CRAFTING) && !event.getInventory().getType().equals(InventoryType.PLAYER)) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        ArmorType newArmorType = ArmorType.matchType(shift ? event.getCurrentItem() : event.getCursor());
        if (!shift && newArmorType != null && event.getRawSlot() != newArmorType.getSlot()) {
            // Used for drag and drop checking to make sure you aren't trying to place a helmet in the boots slot.
            return;
        }
        if (shift) {
            newArmorType = ArmorType.matchType(event.getCurrentItem());
            if (newArmorType != null) {
                boolean equipping = true;
                if (event.getRawSlot() == newArmorType.getSlot()) {
                    equipping = false;
                }
                if (newArmorType.equals(ArmorType.HELMET)
                        && (equipping == isAirOrNull(event.getWhoClicked().getInventory().getHelmet()))
                        || newArmorType.equals(ArmorType.CHESTPLATE)
                        && (equipping == isAirOrNull(event.getWhoClicked().getInventory().getChestplate()))
                        || newArmorType.equals(ArmorType.LEGGINGS)
                        && (equipping == isAirOrNull(event.getWhoClicked().getInventory().getLeggings()))
                        || newArmorType.equals(ArmorType.BOOTS)
                        && (equipping == isAirOrNull(event.getWhoClicked().getInventory().getBoots()))) {
                    ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(
                            (Player) event.getWhoClicked(),
                            EquipMethod.SHIFT_CLICK,
                            newArmorType,
                            equipping ? null : event.getCurrentItem(),
                            equipping ? event.getCurrentItem() : null
                    );
                    Bukkit.getPluginManager().callEvent(armorEquipEvent);
                    if (armorEquipEvent.isCancelled()) {
                        event.setCancelled(true);
                    }
                }
            }
        } else {
            ItemStack newArmorPiece = event.getCursor();
            ItemStack oldArmorPiece = event.getCurrentItem();
            if (numberkey) {
                if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                    ItemStack hotbarItem = event.getClickedInventory().getItem(event.getHotbarButton());
                    if (!isAirOrNull(hotbarItem)) {
                        newArmorType = ArmorType.matchType(hotbarItem);
                        newArmorPiece = hotbarItem;
                        oldArmorPiece = event.getClickedInventory().getItem(event.getSlot());
                    } else {
                        newArmorType = ArmorType.matchType(!isAirOrNull(event.getCurrentItem()) ? event.getCurrentItem() : event.getCursor());
                    }
                }
            } else {
                if (isAirOrNull(event.getCursor()) && !isAirOrNull(event.getCurrentItem())) {
                    newArmorType = ArmorType.matchType(event.getCurrentItem());
                }
            }
            if (newArmorType != null && event.getRawSlot() == newArmorType.getSlot()) {
                ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.PICK_DROP;
                if (event.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey) {
                    method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
                }
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getWhoClicked(), method, newArmorType, oldArmorPiece, newArmorPiece);
                Bukkit.getPluginManager().callEvent(armorEquipEvent);
                if (armorEquipEvent.isCancelled()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteractEvent(@NotNull final PlayerInteractEvent e) {
        if (e.useItemInHand().equals(Result.DENY)) {
            return;
        }
        if (e.getAction() == Action.PHYSICAL) {
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = e.getPlayer();
            if (!e.useInteractedBlock().equals(Result.DENY)) {
                if (e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && !player.isSneaking()) {
                    Material mat = e.getClickedBlock().getType();
                }
            }
            ArmorType newArmorType = ArmorType.matchType(e.getItem());
            if (newArmorType != null) {
                if (newArmorType.equals(ArmorType.HELMET)
                        && isAirOrNull(e.getPlayer().getInventory().getHelmet())
                        || newArmorType.equals(ArmorType.CHESTPLATE)
                        && isAirOrNull(e.getPlayer().getInventory().getChestplate())
                        || newArmorType.equals(ArmorType.LEGGINGS)
                        && isAirOrNull(e.getPlayer().getInventory().getLeggings())
                        || newArmorType.equals(ArmorType.BOOTS)
                        && isAirOrNull(e.getPlayer().getInventory().getBoots())) {
                    ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR, ArmorType.matchType(e.getItem()), null, e.getItem());
                    Bukkit.getPluginManager().callEvent(armorEquipEvent);
                    if (armorEquipEvent.isCancelled()) {
                        e.setCancelled(true);
                        player.updateInventory();
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void inventoryDrag(@NotNull final InventoryDragEvent event) {
        // getType() seems to always be even.
        // Old Cursor gives the item you are equipping
        // Raw slot is the ArmorType slot
        // Can't replace armor using this method making getCursor() useless.
        ArmorType type = ArmorType.matchType(event.getOldCursor());
        if (event.getRawSlots().isEmpty()) {
            return;
        }
        if (type != null && type.getSlot() == event.getRawSlots().stream().findFirst().orElse(0)) {
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getWhoClicked(), EquipMethod.DRAG, type, null, event.getOldCursor());
            Bukkit.getPluginManager().callEvent(armorEquipEvent);
            if (armorEquipEvent.isCancelled()) {
                event.setResult(Result.DENY);
                event.setCancelled(true);
            }
        }
        // Debug shit
    }

    @EventHandler
    public void playerJoinEvent(@NotNull final PlayerJoinEvent event) {
        ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(event.getPlayer(), null, null, null, null);
        Bukkit.getPluginManager().callEvent(armorEquipEvent);
    }

    @EventHandler
    public void playerRespawnEvent(@NotNull final PlayerRespawnEvent event) {
        ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(event.getPlayer(), null, null, null, null);
        Bukkit.getPluginManager().callEvent(armorEquipEvent);
    }

    @EventHandler
    public void itemBreakEvent(@NotNull final PlayerItemBreakEvent event) {
        ArmorType type = ArmorType.matchType(event.getBrokenItem());
        if (type != null) {
            Player p = event.getPlayer();
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, EquipMethod.BROKE, type, event.getBrokenItem(), null);
            Bukkit.getPluginManager().callEvent(armorEquipEvent);
            if (armorEquipEvent.isCancelled()) {
                ItemStack i = event.getBrokenItem().clone();
                i.setAmount(1);
                i.setDurability((short) (i.getDurability() - 1));
                if (type.equals(ArmorType.HELMET)) {
                    p.getInventory().setHelmet(i);
                } else if (type.equals(ArmorType.CHESTPLATE)) {
                    p.getInventory().setChestplate(i);
                } else if (type.equals(ArmorType.LEGGINGS)) {
                    p.getInventory().setLeggings(i);
                } else if (type.equals(ArmorType.BOOTS)) {
                    p.getInventory().setBoots(i);
                }
            }
        }
    }

    @EventHandler
    public void playerDeathEvent(@NotNull final PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (event.getKeepInventory()) {
            return;
        }
        for (ItemStack i : p.getInventory().getArmorContents()) {
            if (!isAirOrNull(i)) {
                Bukkit.getPluginManager().callEvent(new ArmorEquipEvent(p, EquipMethod.DEATH, ArmorType.matchType(i), i, null));
                // No way to cancel a death event.
            }
        }
    }

    public static boolean isAirOrNull(@Nullable final ItemStack item) {
        return item == null || item.getType().equals(Material.AIR);
    }
}
