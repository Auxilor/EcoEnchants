package com.willfp.ecoenchants.enchant

import com.willfp.eco.core.cache.EcoCache
import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.gui.addCustomSlots
import com.willfp.eco.core.gui.addPageChanger
import com.willfp.eco.core.gui.component.ConfigButton
import com.willfp.eco.core.gui.component.PagedAreaComponent
import com.willfp.eco.core.gui.component.addComponent
import com.willfp.eco.core.gui.component.addPagedArea
import com.willfp.eco.core.gui.getSlotArea
import com.willfp.eco.core.gui.getSlotLocations
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.page.Page
import com.willfp.eco.core.gui.page.PageChanger
import com.willfp.eco.core.gui.setMask
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.EnchantedBookBuilder
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.items.isEcoEmpty
import com.willfp.eco.core.sound.PlayableSound
import com.willfp.eco.util.formatEco
import com.willfp.eco.util.lineWrap
import com.willfp.ecoenchants.display.EnchantSorter.sortForDisplay
import com.willfp.ecoenchants.display.getFormattedDescription
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.display.hideStoredEnchants
import com.willfp.ecoenchants.dragdrop.dragAndDropPriceDisplay
import com.willfp.ecoenchants.dragdrop.isDragAndDropEnabled
import com.willfp.ecoenchants.enchant.DiscoveryType
import com.willfp.ecoenchants.plugin
import com.willfp.ecoenchants.target.EnchantmentTargets.applicableEnchantments
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.type.EnchantmentTypes
import com.willfp.ecoenchants.target.EnchantmentTargets
import kotlin.math.ceil

object EnchantGUI {
    // Rebuilt on reload while players on other threads may be opening them.
    @Volatile
    private lateinit var menu: Menu
    @Volatile
    private var groupMenu: Menu? = null
    private val enchantInfoMenus = EcoCache.builder<Pair<EcoEnchant, Int>, Menu>().build()
    @Volatile
    private var allEnchantsSorted: List<Enchantment> = emptyList()

    internal fun reload() {
        cachedEnchantmentSlots.invalidateAll()
        enchantInfoMenus.invalidateAll()
        allEnchantsSorted = EcoEnchants.values().map { it.enchantment }.sortForDisplay()

        menu = menu(plugin.configYml.getInt("enchant-gui.rows")) {
            title = plugin.configYml.getFormattedString("enchant-gui.title")

            allowChangingHeldItem()

            setMask(plugin.configYml, "enchant-gui")

            for (location in plugin.configYml.getSlotLocations("enchant-gui.info")) {
                setSlot(
                    location.row,
                    location.column,
                    slot(
                        ItemStackBuilder(Items.lookup(plugin.configYml.getString("enchant-gui.info.item")))
                            .addLoreLines(plugin.configYml.getStrings("enchant-gui.info.lore"))
                            .build()
                    )
                )
            }

            val captiveRow = plugin.configYml.getInt("enchant-gui.item-row")
            val captiveColumn = plugin.configYml.getInt("enchant-gui.item-column")

            setSlot(
                captiveRow, captiveColumn, slot(ItemStack(Material.AIR)) {
                    setCaptive(true)
                }
            )

            onRender { player, menu ->
                val atCaptive = menu.getCaptiveItem(player, captiveRow, captiveColumn)
                val hasItem = !atCaptive.isEcoEmpty && atCaptive != null && atCaptive.type != Material.BOOK

                val canSeeHidden = player.hasPermission("ecoenchants.seehidden")
                val baseEnchants = if (!hasItem) {
                    EcoEnchants.values().filter { !it.isHiddenFromGui || canSeeHidden }.map { it.enchantment }.sortForDisplay()
                } else {
                    atCaptive.applicableEnchantments.filter { !it.isHiddenFromGui || canSeeHidden }.map { it.enchantment }.sortForDisplay()
                        .subtract(atCaptive.fast().enchants.keys)
                        .toList()
                }

                // Apply group filter if a groupId is set in menu state
                val groupId = menu.getState<String>(player, "groupId")
                val filteredEnchants = if (groupId != null) {
                    val groupBy = plugin.configYml.getString("enchant-gui.group-by")
                    baseEnchants.filter { enchantment ->
                        val wrapped = enchantment.wrap()
                        when (groupBy) {
                            "type" -> wrapped.type.id == groupId
                            "rarity" -> wrapped.enchantmentRarity.id == groupId
                            "target" -> wrapped is EcoEnchant && wrapped.targets.any { it.id == groupId }
                            else -> true
                        }
                    }
                } else {
                    baseEnchants
                }

                menu.setState(player, "enchants", filteredEnchants.mapNotNull { it.ecoEnchant })

                // Reset to page 1 when an item is placed or removed from the captive slot
                val previousHasItem = menu.getState<Boolean>(player, "hasItem") ?: false
                if (hasItem != previousHasItem) {
                    menu.setState(player, Page.PAGE_KEY, 1)
                }
                menu.setState(player, "hasItem", hasItem)

                // Safety net: also reset if the current page now exceeds the new max.
                // Compute directly from filteredEnchants to avoid a stale getMaxPage() value
                // (maxPages may be evaluated before onRender fires).
                val perPage = plugin.configYml.getInt("enchant-gui.enchant-area.width") * plugin.configYml.getInt("enchant-gui.enchant-area.height")
                val maxPage = if (filteredEnchants.isEmpty()) {
                    0
                } else {
                    ceil(filteredEnchants.size.toDouble() / perPage).toInt()
                }
                if (menu.getPage(player) > maxPage) {
                    menu.setState(player, Page.PAGE_KEY, 1)
                }
            }

            addPagedArea(EnchantmentScrollPane())

            val pageChangeSound = PlayableSound.create(
                plugin.configYml.getSubsection("enchant-gui.page-change.sound")
            )

            for (direction in PageChanger.Direction.entries) {
                val directionName = direction.name.lowercase()

                addPageChanger(
                    plugin.configYml,
                    "enchant-gui.page-change.$directionName",
                    direction,
                    pageChangeSound
                )
            }

            if (plugin.configYml.getBool("enchant-gui.close-button.enabled")) {
                for (location in plugin.configYml.getSlotLocations("enchant-gui.close-button")) {
                    setSlot(
                        location.row,
                        location.column,
                        slot(
                            ItemStackBuilder(Items.lookup(plugin.configYml.getString("enchant-gui.close-button.item")))
                                .setDisplayName(plugin.configYml.getFormattedString("enchant-gui.close-button.name"))
                                .addLoreLines(plugin.configYml.getStrings("enchant-gui.close-button.lore"))
                                .build()
                        ) {
                            onLeftClick { event, _ -> event.whoClicked.closeInventory() }
                        }
                    )
                }
            }

            // Back button to return to the group selection menu
            if (plugin.configYml.getBool("enchant-gui.grouped")) {
                addComponent(
                    ConfigButton(
                        plugin.configYml.getSubsection("enchant-gui.back-button"),
                        enabledByDefault = false
                    ) { player, _, _, _ ->
                        val groupGui = groupMenu ?: return@ConfigButton
                        // Return captive items to the player before navigating back
                        val captiveItems = menu.getCaptiveItems(player)
                        if (captiveItems.isNotEmpty()) {
                            DropQueue(player)
                                .addItems(captiveItems)
                                .forceTelekinesis()
                                .push()
                        }
                        groupGui.open(player)
                    }
                )
            }

            onClose { event, menu ->
                val player = event.player as Player
                DropQueue(player)
                    .addItems(menu.getCaptiveItems(player))
                    .forceTelekinesis()
                    .push()
            }

            addCustomSlots(plugin.configYml, "enchant-gui")
        }

        // Build the group selection menu (only when grouped mode is enabled)
        if (plugin.configYml.getBool("enchant-gui.grouped")) {
            groupMenu = menu(plugin.configYml.getInt("group-gui.rows")) {
                title = plugin.configYml.getFormattedString("group-gui.title")

                setMask(plugin.configYml, "group-gui")

                // Add a clickable slot for each configured group
                for (config in plugin.configYml.getSubsections("group-gui.groups")) {
                    val groupId = config.getString("id")

                    // Validate the group ID exists in the registry matching the group-by axis
                    val groupBy = plugin.configYml.getString("enchant-gui.group-by")
                    val valid = when (groupBy) {
                        "type" -> EnchantmentTypes[groupId] != null
                        "rarity" -> EnchantmentRarities[groupId] != null
                        "target" -> EnchantmentTargets[groupId] != null
                        else -> false
                    }

                    if (!valid) {
                        continue
                    }

                    for (location in config.getSlotLocations()) {
                        setSlot(
                            location.row,
                            location.column,
                            slot(
                                ItemStackBuilder(Items.lookup(config.getString("item")))
                                    .addLoreLines(config.getStrings("lore"))
                                    .build()
                            ) {
                                onLeftClick { event, _ ->
                                    openGroupGUI(event.whoClicked as Player, groupId)
                                }
                            }
                        )
                    }
                }

                // Custom decorator slots for the group menu
                addCustomSlots(plugin.configYml, "group-gui")
            }
        } else {
            groupMenu = null
        }
    }

    fun openGUI(player: Player) {
        val groupMenu = groupMenu

        if (plugin.configYml.getBool("enchant-gui.grouped") && groupMenu != null) {
            groupMenu.open(player)
        } else {
            menu.open(player)
        }
    }

    fun openInfoGUI(player: Player, enchant: EcoEnchant, level: Int = -1) {
        val effectiveLevel = if (level == -1) {
            if (plugin.configYml.getBool("enchantinfo.item.show-max-level")) enchant.maximumLevel else 1
        } else {
            level.coerceIn(1, enchant.maximumLevel)
        }

        enchantInfoMenus.get(enchant to effectiveLevel) {
            menu(plugin.configYml.getInt("enchantinfo.rows")) {
                title = enchant.getFormattedName(effectiveLevel)

                for (location in plugin.configYml.getSlotLocations("enchantinfo.item")) {
                    setSlot(location.row, location.column, enchant.getInformationSlot(player, effectiveLevel))
                }

                setMask(plugin.configYml, "enchantinfo")

                addCustomSlots(plugin.configYml, "enchantinfo")
            }
        }.open(player)
    }

    private fun openGroupGUI(player: Player, groupId: String) {
        menu.open(player)
        menu.setState(player, "groupId", groupId)
        menu.setState(player, Page.PAGE_KEY, 1)
    }
}

private class EnchantmentScrollPane : PagedAreaComponent<EcoEnchant>(
    plugin.configYml.getSlotArea("enchant-gui.enchant-area")
) {
    private val defaultSlot = slot(Items.lookup(plugin.configYml.getString("enchant-gui.empty-item")))

    override fun getEntries(player: Player, menu: Menu): List<EcoEnchant> =
        menu.getState<List<EcoEnchant>>(player, "enchants") ?: emptyList()

    override fun getEntrySlot(player: Player, menu: Menu, entry: EcoEnchant): Slot {
        val displayLevel = if (plugin.configYml.getBool("enchantinfo.item.show-max-level")) entry.maximumLevel else 1
        return entry.getInformationSlot(player, displayLevel)
    }

    override fun getEmptySlot(player: Player, menu: Menu): Slot = defaultSlot
}

private val cachedEnchantmentSlots = EcoCache.builder<Pair<EcoEnchant, Int>, Slot>()
    .build()

private fun EcoEnchant.getInformationSlot(player: Player, level: Int): Slot {
    return cachedEnchantmentSlots.get(this to level) {
        slot(
            EnchantedBookBuilder()
                .addStoredEnchantment(enchantment, level)
                .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                .setDisplayName(this.getFormattedName(level))
                .addLoreLines(this.getFormattedDescription(level, player))
                .addLoreLines {
                    plugin.configYml.getStrings("enchantinfo.item.lore")
                        .map {
                            it.replace("%max_level%", enchantment.maxLevel.toString())
                                .replace("%rarity%", this.enchantmentRarity.displayName)
                                .replace(
                                    "%targets%",
                                    this.targets.joinToString(", ") { target -> target.displayName }
                                )
                                .replace(
                                    "%conflicts%",
                                    if (this.conflictsWithEverything) {
                                        plugin.langYml.getFormattedString("all-conflicts")
                                    } else {
                                        this.conflicts.joinToString(", ") { conflict ->
                                            conflict.wrap().getFormattedName(0)
                                        }.ifEmpty { plugin.langYml.getFormattedString("no-conflicts") }
                                    }
                                )
                                .replace(
                                    "%required%",
                                    this.required.joinToString(", ") { required ->
                                        required.wrap().getFormattedName(0)
                                    }.ifEmpty { plugin.langYml.getFormattedString("no-required") }
                                )
                                .replace("%tradeable%", (plugin.configYml.getBool("villager.enabled") && this.isObtainableThroughTrading).parseLangOption("tradeable"))
                                .replace("%discoverable%", (plugin.configYml.getBool("loot.enabled") && this.isObtainableThroughDiscovery).parseDiscoverable())
                                .replace("%discoverable_chests%", (plugin.configYml.getBool("loot.enabled") && this.isObtainableThrough(DiscoveryType.CHESTS)).parseDiscoverable(DiscoveryType.CHESTS))
                                .replace("%discoverable_fishing%", (plugin.configYml.getBool("loot.enabled") && this.isObtainableThrough(DiscoveryType.FISHING)).parseDiscoverable(DiscoveryType.FISHING))
                                .replace("%discoverable_mob_drops%", (plugin.configYml.getBool("loot.enabled") && this.isObtainableThrough(DiscoveryType.MOB_DROPS)).parseDiscoverable(DiscoveryType.MOB_DROPS))
                                .replace("%discoverable_raids%", (plugin.configYml.getBool("loot.enabled") && this.isObtainableThrough(DiscoveryType.RAIDS)).parseDiscoverable(DiscoveryType.RAIDS))
                                .replace("%enchantable%", (plugin.configYml.getBool("enchanting-table.enabled") && this.isObtainableThroughEnchanting).parseLangOption("enchantable"))
                                .replace("%drag_and_drop%", this.isDragAndDropEnabled().parseLangOption("drag-and-drop"))
                                .replace(
                                    "%drag_and_drop_price%",
                                    if (this.isDragAndDropEnabled()) {
                                        this.dragAndDropPriceDisplay(player, level)
                                    } else {
                                        false.parseLangOption("drag-and-drop")
                                    }
                                )
                        }
                        .formatEco()
                        .flatMap {
                            it.lineWrap(32, true)
                        }
                }
                .build()
                .fast()
                .apply { hideStoredEnchants() }
                .unwrap()
        )
    }
}

fun Boolean.parseYesOrNo(): String =
    if (this) plugin.langYml.getFormattedString("yes") else plugin.langYml.getFormattedString("no")

fun Boolean.parseLangOption(path: String): String =
    plugin.langYml.getFormattedString("$path.$this")

fun Boolean.parseDiscoverable(type: DiscoveryType? = null): String {
    val path = if (type != null) "discoverable.${type.configKey}" else "discoverable"
    return this.parseLangOption(path)
}
