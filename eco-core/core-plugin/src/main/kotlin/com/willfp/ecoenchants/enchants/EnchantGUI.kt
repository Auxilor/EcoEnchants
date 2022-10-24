package com.willfp.ecoenchants.enchants

import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.config.updating.ConfigUpdater
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
import com.willfp.eco.core.items.isEmpty
import com.willfp.eco.util.StringUtils
import com.willfp.eco.util.formatEco
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.display.EnchantSorter.sortForDisplay
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.target.EnchantmentTargets.applicableEnchantments
import org.apache.commons.lang.WordUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import kotlin.math.ceil

object EnchantGUI {
    private lateinit var menu: Menu
    private val enchantInfoMenus = Caffeine.newBuilder().build<EcoEnchant, Menu>()

    @JvmStatic
    @ConfigUpdater
    fun update(plugin: EcoEnchantsPlugin) {
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
                if (atCaptive.isEmpty || atCaptive == null || atCaptive.type == Material.BOOK) {
                    menu.setState(player, "enchants", EcoEnchants.values().sortForDisplay())
                } else {
                    menu.setState(
                        player,
                        "enchants",
                        atCaptive.applicableEnchantments.sortForDisplay()
                            .subtract(atCaptive.fast().enchants.keys)
                            .toList()
                    )
                }

                if (menu.getPage(player) > menu.getMaxPage(player)) {
                    menu.setState(player, Page.PAGE_KEY, 1)
                }
            }

            val pane = EnchantmentScrollPane(plugin)

            addComponent(
                plugin.configYml.getInt("enchant-gui.enchant-area.row"),
                plugin.configYml.getInt("enchant-gui.enchant-area.column"),
                pane
            )

            for (direction in PageChanger.Direction.values()) {
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

            for (config in plugin.configYml.getSubsections("level-gui.custom-slots")) {
                setSlot(
                    config.getInt("row"),
                    config.getInt("column"),
                    ConfigSlot(config)
                )
            }
        }
    }

    fun openGUI(player: Player) {
        menu.open(player)
    }

    fun openInfoGUI(player: Player, enchant: EcoEnchant, plugin: EcoEnchantsPlugin) {
        enchantInfoMenus.get(enchant) {
            menu(plugin.configYml.getInt("enchantinfo.rows")) {
                title = enchant.getFormattedName(0)

                setSlot(
                    plugin.configYml.getInt("enchantinfo.item.row"),
                    plugin.configYml.getInt("enchantinfo.item.column"),
                    enchant.getInformationSlot(plugin)
                )

                setMask(
                    FillerMask(
                        MaskItems.fromItemNames(plugin.configYml.getStrings("enchantinfo.mask.items")),
                        *plugin.configYml.getStrings("enchantinfo.mask.pattern").toTypedArray()
                    )
                )

                for (config in plugin.configYml.getSubsections("level-gui.custom-slots")) {
                    setSlot(
                        config.getInt("row"),
                        config.getInt("column"),
                        ConfigSlot(config)
                    )
                }
            }
        }.open(player)
    }
}

private class EnchantmentScrollPane(
    private val plugin: EcoEnchantsPlugin
) : GUIComponent {
    private val defaultSlot = slot(Items.lookup(plugin.configYml.getString("enchant-gui.empty-item")))

    override fun getSlotAt(row: Int, column: Int, player: Player, menu: Menu): Slot? {
        val index = column + ((row - 1) * columns) - 1
        val page = menu.getPage(player)

        val enchants = menu.getState<List<EcoEnchant>>(player, "enchants") ?: return defaultSlot
        if (enchants.isEmpty()) {
            return defaultSlot
        }

        val enchant = enchants.getOrNull(index + size * (page - 1)) ?: return defaultSlot

        return enchant.getInformationSlot(plugin)
    }

    override fun getRows() = plugin.configYml.getInt("enchant-gui.enchant-area.height")
    override fun getColumns() = plugin.configYml.getInt("enchant-gui.enchant-area.width")

    val size = rows * columns
}

private val cachedEnchantmentSlots = Caffeine.newBuilder()
    .build<EcoEnchant, Slot>()

private fun EcoEnchant.getInformationSlot(plugin: EcoEnchantsPlugin): Slot {
    return cachedEnchantmentSlots.get(this) {
        val level = if (plugin.configYml.getBool("enchantinfo.item.show-max-level")) {
            enchant.maxLevel
        } else {
            1
        }

        slot(
            EnchantedBookBuilder()
                .addStoredEnchantment(enchant, level)
                .writeMetaKey(
                    plugin.namespacedKeyFactory.create("force-describe"),
                    PersistentDataType.INTEGER,
                    1
                )
                .addLoreLines {
                    // This is horrific and I should refactor it.
                    val wrappableMap = mutableMapOf<String, String>()

                    fun String.toWrappable(): String {
                        val unspaced = this.replace(" ", "{_}")
                        val uncolored = ChatColor.stripColor(unspaced)!!

                        wrappableMap[uncolored] = this
                        return uncolored
                    }

                    fun String.replaceInWrappable(): String {
                        var processed = this
                        for ((wrappable, original) in wrappableMap) {
                            processed = processed.replace(wrappable, original)
                        }
                        return processed
                    }

                    plugin.configYml.getStrings("enchantinfo.item.lore")
                        .map {
                            it.replace("%max_level%", enchant.maxLevel.toString())
                                .replace("%rarity%", enchant.enchantmentRarity.displayName.toWrappable())
                                .replace(
                                    "%targets%",
                                    enchant.targets.joinToString(", ") { target -> target.displayName.toWrappable() }
                                )
                                .replace(
                                    "%conflicts%",
                                    if (enchant.conflictsWithEverything) {
                                        plugin.langYml.getFormattedString("all-conflicts").toWrappable()
                                    } else {
                                        enchant.conflicts.joinToString(", ") { conflict ->
                                            conflict.wrap().getFormattedName(0).toWrappable()
                                        }.ifEmpty { plugin.langYml.getFormattedString("no-conflicts").toWrappable() }
                                    }
                                )
                        }
                        .flatMap {
                            WordUtils.wrap(it, 45, "\n", false)
                                .lines()
                                .map { s -> s.replaceInWrappable() }
                                .mapIndexed { index, s ->
                                    if (index == 0) s
                                    else StringUtils.format(
                                        plugin.configYml.getString("enchantinfo.item.line-wrap-format") + s
                                    )
                                }
                        }
                        .formatEco()
                }
                .build()
        )
    }
}
