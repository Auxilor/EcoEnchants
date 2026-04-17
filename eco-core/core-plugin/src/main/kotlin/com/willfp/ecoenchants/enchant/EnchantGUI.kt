package com.willfp.ecoenchants.enchant

import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.config.base.LangYml
import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.gui.GUIComponent
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.menu.MenuLayer
import com.willfp.eco.core.gui.page.Page
import com.willfp.eco.core.gui.page.PageChanger
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.gui.slot.ConfigSlot
import com.willfp.eco.core.gui.slot.FillerMask
import com.willfp.eco.core.gui.slot.MaskItems
import com.willfp.eco.core.gui.slot.Slot
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.EnchantedBookBuilder
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.items.isEcoEmpty
import com.willfp.eco.util.formatEco
import com.willfp.eco.util.lineWrap
import com.willfp.ecoenchants.display.EnchantSorter.sortForDisplay
import com.willfp.ecoenchants.display.HideStoredEnchantsProxy
import com.willfp.ecoenchants.display.getFormattedDescription
import com.willfp.ecoenchants.display.getFormattedName
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
    private lateinit var menu: Menu
    private var groupMenu: Menu? = null
    private val enchantInfoMenus = Caffeine.newBuilder().build<EcoEnchant, Menu>()
    private var allEnchantsSorted: List<Enchantment> = emptyList()

    internal fun reload() {
        cachedEnchantmentSlots.invalidateAll()
        enchantInfoMenus.invalidateAll()
        allEnchantsSorted = EcoEnchants.values().map { it.enchantment }.sortForDisplay()

        menu = menu(plugin.configYml.getInt("enchant-gui.rows")) {
            title = plugin.configYml.getFormattedString("enchant-gui.title")

            allowChangingHeldItem()

            setMask(
                FillerMask(
                    MaskItems.fromItemNames(
                        plugin.configYml.getStrings("enchant-gui.mask.items")
                    ),
                    *plugin.configYml.getStrings("enchant-gui.mask.pattern").toTypedArray()
                )
            )

            setSlot(
                plugin.configYml.getInt("enchant-gui.info.row"),
                plugin.configYml.getInt("enchant-gui.info.column"),
                slot(
                    ItemStackBuilder(Items.lookup(plugin.configYml.getString("enchant-gui.info.item")))
                        .addLoreLines(plugin.configYml.getStrings("enchant-gui.info.lore"))
                        .build()
                )
            )

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

                val baseEnchants = if (!hasItem) {
                    EcoEnchants.values().map { it.enchantment }.sortForDisplay()
                } else {
                    atCaptive.applicableEnchantments.map { it.enchantment }.sortForDisplay()
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

                menu.setState(player, "enchants", filteredEnchants)

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

            val pane = EnchantmentScrollPane()

            addComponent(
                plugin.configYml.getInt("enchant-gui.enchant-area.row"),
                plugin.configYml.getInt("enchant-gui.enchant-area.column"),
                pane
            )

            for (direction in PageChanger.Direction.entries) {
                val directionName = direction.name.lowercase()

                addComponent(
                    MenuLayer.TOP,
                    plugin.configYml.getInt("enchant-gui.page-change.$directionName.row"),
                    plugin.configYml.getInt("enchant-gui.page-change.$directionName.column"),
                    PageChanger(
                        Items.lookup(plugin.configYml.getString("enchant-gui.page-change.$directionName.item")).item,
                        direction
                    )
                )
            }

            if (plugin.configYml.getBool("enchant-gui.close-button.enabled")) {
                setSlot(
                    plugin.configYml.getInt("enchant-gui.close-button.row"),
                    plugin.configYml.getInt("enchant-gui.close-button.column"),
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

            // Back button to return to the group selection menu
            if (plugin.configYml.getBool("enchant-gui.grouped")
                && plugin.configYml.getBool("enchant-gui.back-button.enabled")) {
                setSlot(
                    plugin.configYml.getInt("enchant-gui.back-button.row"),
                    plugin.configYml.getInt("enchant-gui.back-button.column"),
                    slot(
                        ItemStackBuilder(Items.lookup(plugin.configYml.getString("enchant-gui.back-button.item")))
                            .addLoreLines(plugin.configYml.getStrings("enchant-gui.back-button.lore"))
                            .build()
                    ) {
                        onLeftClick { event, _ ->
                            val player = event.whoClicked as Player
                            // Return captive items to the player before navigating back
                            val captiveItems = menu.getCaptiveItems(player)
                            if (captiveItems.isNotEmpty()) {
                                DropQueue(player)
                                    .addItems(captiveItems)
                                    .forceTelekinesis()
                                    .push()
                            }
                            groupMenu?.open(player)
                        }
                    }
                )
            }

            maxPages { player ->
                val enchants = menu.getState<List<EcoEnchant>>(player, "enchants") ?: emptyList()
                val total = enchants.size
                val perPage = pane.size

                val pages = if (total == 0) {
                    0
                } else {
                    ceil(total.toDouble() / perPage).toInt()
                }
                pages
            }

            onClose { event, menu ->
                val player = event.player as Player
                DropQueue(player)
                    .addItems(menu.getCaptiveItems(player))
                    .forceTelekinesis()
                    .push()
            }

            for (config in plugin.configYml.getSubsections("enchant-gui.custom-slots")) {
                setSlot(
                    config.getInt("row"),
                    config.getInt("column"),
                    ConfigSlot(config)
                )
            }
        }

        // Build the group selection menu (only when grouped mode is enabled)
        if (plugin.configYml.getBool("enchant-gui.grouped")) {
            groupMenu = menu(plugin.configYml.getInt("group-gui.rows")) {
                title = plugin.configYml.getFormattedString("group-gui.title")

                setMask(
                    FillerMask(
                        MaskItems.fromItemNames(
                            plugin.configYml.getStrings("group-gui.mask.items")
                        ),
                        *plugin.configYml.getStrings("group-gui.mask.pattern").toTypedArray()
                    )
                )

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

                    setSlot(
                        config.getInt("row"),
                        config.getInt("column"),
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

                // Custom decorator slots for the group menu
                for (config in plugin.configYml.getSubsections("group-gui.custom-slots")) {
                    setSlot(
                        config.getInt("row"),
                        config.getInt("column"),
                        ConfigSlot(config)
                    )
                }
            }
        } else {
            groupMenu = null
        }
    }

    fun openGUI(player: Player) {
        if (plugin.configYml.getBool("enchant-gui.grouped") && groupMenu != null) {
            groupMenu!!.open(player)
        } else {
            menu.open(player)
        }
    }

    fun openInfoGUI(player: Player, enchant: EcoEnchant) {
        enchantInfoMenus.get(enchant) {
            menu(plugin.configYml.getInt("enchantinfo.rows")) {
                title = enchant.getFormattedName(0)

                setSlot(
                    plugin.configYml.getInt("enchantinfo.item.row"),
                    plugin.configYml.getInt("enchantinfo.item.column"),
                    enchant.getInformationSlot(player)
                )

                setMask(
                    FillerMask(
                        MaskItems.fromItemNames(plugin.configYml.getStrings("enchantinfo.mask.items")),
                        *plugin.configYml.getStrings("enchantinfo.mask.pattern").toTypedArray()
                    )
                )

                for (config in plugin.configYml.getSubsections("enchantinfo.custom-slots")) {
                    setSlot(
                        config.getInt("row"),
                        config.getInt("column"),
                        ConfigSlot(config)
                    )
                }
            }
        }.open(player)
    }

    private fun openGroupGUI(player: Player, groupId: String) {
        menu.open(player)
        menu.setState(player, "groupId", groupId)
        menu.setState(player, Page.PAGE_KEY, 1)
    }
}

private class EnchantmentScrollPane : GUIComponent {
    private val defaultSlot = slot(Items.lookup(plugin.configYml.getString("enchant-gui.empty-item")))

    override fun getSlotAt(row: Int, column: Int, player: Player, menu: Menu): Slot {
        val index = column + ((row - 1) * columns) - 1
        val page = menu.getPage(player)

        val enchants = menu.getState<List<EcoEnchant>>(player, "enchants") ?: return defaultSlot
        if (enchants.isEmpty()) {
            return defaultSlot
        }

        val enchant = enchants.getOrNull(index + size * (page - 1)) ?: return defaultSlot

        return enchant.getInformationSlot(player)
    }

    override fun getRows() = plugin.configYml.getInt("enchant-gui.enchant-area.height")
    override fun getColumns() = plugin.configYml.getInt("enchant-gui.enchant-area.width")

    val size = rows * columns
}

private val cachedEnchantmentSlots = Caffeine.newBuilder()
    .build<EcoEnchant, Slot>()

private fun EcoEnchant.getInformationSlot(player: Player): Slot {
    return cachedEnchantmentSlots.get(this) { it ->
        val level = if (plugin.configYml.getBool("enchantinfo.item.show-max-level")) {
            it.maximumLevel
        } else {
            1
        }

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
                                .replace("%tradeable%", this.isObtainableThroughTrading.parseYesOrNo(plugin.langYml))
                                .replace(
                                    "%discoverable%",
                                    this.isObtainableThroughDiscovery.parseYesOrNo(plugin.langYml)
                                )
                                .replace(
                                    "%enchantable%",
                                    this.isObtainableThroughEnchanting.parseYesOrNo(plugin.langYml)
                                )
                        }
                        .formatEco()
                        .flatMap {
                            it.lineWrap(32, true)
                        }
                }
                .build()
                .fast()
                .apply {
                    plugin.getProxy(HideStoredEnchantsProxy::class.java).hideStoredEnchants(this)
                }
                .unwrap()
        )
    }
}

fun Boolean.parseYesOrNo(langYml: LangYml): String {
    return if (this) langYml.getFormattedString("yes") else langYml.getFormattedString("no")
}
