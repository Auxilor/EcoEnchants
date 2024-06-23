package com.willfp.ecoenchants.mechanics

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.items.toSNBT
import com.willfp.eco.core.proxy.ProxyConstants
import com.willfp.eco.util.StringUtils
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.wrap
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import java.util.*
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt

data class AnvilResult(
    val result: ItemStack?,
    val xp: Int?
)

fun Int.infiniteIfNegative() = if (this < 1) Int.MAX_VALUE else this

private val FAIL = AnvilResult(null, null)

interface OpenInventoryProxy {
    fun getOpenInventory(player: Player): Any
}

class AnvilSupport(
    private val plugin: EcoPlugin
) : Listener {
    /**
     * Map to prevent incrementing cost several times as inventory events are fired 3 times.
     */
    private val antiRepeat = mutableSetOf<UUID>()

    /**
     * Class for AnvilGUI wrappers to ignore them.
     */
    private val anvilGuiClass = "net.wesjd.anvilgui.version.Wrapper" +
            ProxyConstants.NMS_VERSION.substring(1) +
            "\$AnvilContainer"

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onAnvilPrepare(event: PrepareAnvilEvent) {
        val player = event.viewers.getOrNull(0) as? Player ?: return

        if (this.plugin.getProxy(OpenInventoryProxy::class.java)
                .getOpenInventory(player)::class.java.toString() == anvilGuiClass
        ) {
            return
        }

        if (antiRepeat.contains(player.uniqueId)) {
            return
        }

        antiRepeat.add(player.uniqueId)

        this.plugin.scheduler.run {
            antiRepeat.remove(player.uniqueId)

            val left = event.inventory.getItem(0)?.clone()
            val old = left?.clone()
            val right = event.inventory.getItem(1)?.clone()

            event.result = null
            event.inventory.setItem(2, null)

            val result = doMerge(
                left,
                right,
                event.inventory.renameText ?: "",
                player
            )

            val price = result.xp ?: 0
            val outItem = result.result ?: ItemStack(Material.AIR)

            val oldCost = event.inventory.repairCost

            val oldLeft = event.inventory.getItem(0)

            if (result == FAIL) {
                return@run
            }

            if (oldLeft == null || oldLeft.type != outItem.type) {
                return@run
            }

            if (left == old) {
                return@run
            }

            var cost = oldCost + price

            // Unbelievably specific edge case
            if (oldCost == -price) {
                cost = price
            }

            // Cost could be less than zero at times, so I include that here.
            if (cost <= 0) {
                return@run
            }

            /*
            Transplanted anti-dupe bodge from pre-recode.
             */
            val leftEnchants = left?.fast()?.getEnchants(true) ?: emptyMap()
            val outEnchants = outItem.fast().getEnchants(true)

            if (event.inventory.getItem(1) == null && leftEnchants != outEnchants) {
                return@run
            }

            if (plugin.configYml.getBool("anvil.use-rework-penalty")) {
                val repairCost = outItem.fast().repairCost
                outItem.fast().repairCost = (repairCost + 1) * 2 - 1
            }

            event.inventory.maximumRepairCost = plugin.configYml.getInt("anvil.max-repair-cost").infiniteIfNegative()
            event.inventory.repairCost = cost
            event.result = outItem
            event.inventory.setItem(2, outItem)
        }
    }

    private fun doMerge(
        left: ItemStack?,
        right: ItemStack?,
        itemName: String,
        player: Player
    ): AnvilResult {
        if (left == null || left.type == Material.AIR) {
            return FAIL
        }

        val formattedItemName = if (player.hasPermission("ecoenchants.anvil.color")) {
            StringUtils.format(itemName)
        } else {
            ChatColor.stripColor(itemName)
        }.let { if (it.isNullOrEmpty()) left.fast().displayName else it }

        val permanenceCurse = EcoEnchants.getByID("permanence_curse")

        if (permanenceCurse != null) {
            if (left.fast().getEnchants(true).containsKey(permanenceCurse.enchantment)) {
                return FAIL
            }
        }

        if (right == null || right.type == Material.AIR) {
            if (left.fast().displayName == formattedItemName) {
                return FAIL
            }

            left.fast().displayName =
                formattedItemName.let { "§o$it" } // Not a great way to make it italic, but it works

            return AnvilResult(left, 0)
        }

        val leftMeta = left.itemMeta
        val rightMeta = right.itemMeta

        var unitRepairCost = 0

        // Unit repair
        if (left.type != right.type) {
            if (right.type.canUnitRepair(left.type) && leftMeta is Damageable) {
                val perUnit = ceil(left.type.maxDurability / 4.0).toInt()

                val max = ceil(leftMeta.damage.toDouble() / perUnit).toInt()
                val toDeduct = min(max, right.amount)

                unitRepairCost = toDeduct

                if (toDeduct <= 0) {
                    return FAIL
                } else {
                    val newDamage = leftMeta.damage - toDeduct * perUnit
                    leftMeta.damage = newDamage.coerceAtLeast(0) // Prevent negative damage

                    right.amount -= toDeduct
                }
            } else {
                if (right.type != Material.ENCHANTED_BOOK) {
                    return FAIL
                }
            }
        }

        left.fast().displayName = formattedItemName.let { "§o$it" } // Same again, it works though

        val leftEnchants = left.fast().getEnchants(true)
        val rightEnchants = right.fast().getEnchants(true)

        val outEnchants = leftEnchants.toMutableMap()

        for ((enchant, level) in rightEnchants) {
            if (outEnchants.containsKey(enchant)) {
                val currentLevel = outEnchants[enchant]!!
                outEnchants[enchant] = if (level == currentLevel) {
                    min(enchant.maxLevel, level + 1)
                } else {
                    max(level, currentLevel)
                }
            } else {
                // Running .wrap() to use EcoEnchantLike canEnchantItem logic
                if (enchant.wrap().canEnchantItem(left)) {
                    if (outEnchants.size < plugin.configYml.getInt("anvil.enchant-limit").infiniteIfNegative()) {
                        outEnchants[enchant] = level
                    }
                }
            }
        }

        // Item repair - extra check for unit repair cost to prevent weird damage
        // Enchanted books seem to be damageable? Not quite sure why. Anyway, there's an extra check.
        if (leftMeta is Damageable && rightMeta is Damageable && unitRepairCost == 0 && rightMeta !is EnchantmentStorageMeta) {
            val maxDamage = left.type.maxDurability.toInt()
            val leftDurability = maxDamage - leftMeta.damage
            val rightDurability = maxDamage - rightMeta.damage
            val damage = maxDamage - max(maxDamage, leftDurability + rightDurability)

            leftMeta.damage = damage.coerceAtLeast(0) // Prevent negative damage
        }

        if (leftMeta is EnchantmentStorageMeta) {
            for (storedEnchant in leftMeta.storedEnchants.keys.toSet()) {
                leftMeta.removeStoredEnchant(storedEnchant)
            }

            for ((enchant, level) in outEnchants) {
                leftMeta.addStoredEnchant(enchant, level, true)
            }
        } else {
            for (storedEnchant in leftMeta.enchants.keys.toSet()) {
                leftMeta.removeEnchant(storedEnchant)
            }

            for ((enchant, level) in outEnchants) {
                leftMeta.addEnchant(enchant, level, true)
            }
        }

        left.itemMeta = leftMeta

        val enchantLevelDiff = abs(leftEnchants.values.sum() - outEnchants.values.sum())
        val xpCost =
            plugin.configYml.getDouble("anvil.cost-exponent").pow(enchantLevelDiff) * enchantLevelDiff + unitRepairCost

        return AnvilResult(left, xpCost.roundToInt())
    }
}


private val repair = mapOf<Collection<Material>, Collection<Material>>(
    Pair(
        Tag.PLANKS.values,
        listOf(
            Material.WOODEN_SWORD,
            Material.WOODEN_PICKAXE,
            Material.WOODEN_AXE,
            Material.WOODEN_SHOVEL,
            Material.WOODEN_HOE,
            Material.SHIELD
        )
    ),
    Pair(
        listOf(Material.LEATHER),
        listOf(
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS
        )
    ),
    Pair(
        listOf(
            Material.COBBLESTONE,
            Material.COBBLED_DEEPSLATE,
            Material.BLACKSTONE
        ),
        listOf(
            Material.STONE_SWORD,
            Material.STONE_PICKAXE,
            Material.STONE_AXE,
            Material.STONE_SHOVEL,
            Material.STONE_HOE
        )
    ),
    Pair(
        listOf(
            Material.IRON_INGOT
        ),
        listOf(
            Material.IRON_HELMET,
            Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS,
            Material.IRON_BOOTS,
            Material.CHAINMAIL_HELMET,
            Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS,
            Material.CHAINMAIL_BOOTS,
            Material.IRON_SWORD,
            Material.IRON_PICKAXE,
            Material.IRON_AXE,
            Material.IRON_SHOVEL,
            Material.IRON_HOE
        )
    ),
    Pair(
        listOf(
            Material.GOLD_INGOT
        ),
        listOf(
            Material.GOLDEN_HELMET,
            Material.GOLDEN_CHESTPLATE,
            Material.GOLDEN_LEGGINGS,
            Material.GOLDEN_BOOTS,
            Material.GOLDEN_SWORD,
            Material.GOLDEN_PICKAXE,
            Material.GOLDEN_AXE,
            Material.GOLDEN_SHOVEL,
            Material.GOLDEN_HOE
        )
    ),
    Pair(
        listOf(
            Material.DIAMOND
        ),
        listOf(
            Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS,
            Material.DIAMOND_SWORD,
            Material.DIAMOND_PICKAXE,
            Material.DIAMOND_AXE,
            Material.DIAMOND_SHOVEL,
            Material.DIAMOND_HOE
        )
    ),
    Pair(
        listOf(
            Material.NETHERITE_INGOT
        ),
        listOf(
            Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS,
            Material.NETHERITE_SWORD,
            Material.NETHERITE_PICKAXE,
            Material.NETHERITE_AXE,
            Material.NETHERITE_SHOVEL,
            Material.NETHERITE_HOE
        )
    ),
    Pair(
        listOf(
            Material.SCUTE
        ),
        listOf(
            Material.TURTLE_HELMET
        )
    ),
    Pair(
        listOf(
            Material.PHANTOM_MEMBRANE
        ),
        listOf(
            Material.ELYTRA
        )
    )
)

fun Material.canUnitRepair(other: Material): Boolean {
    for ((units, repairable) in repair) {
        if (this in units) {
            return other in repairable
        }
    }

    return false
}
