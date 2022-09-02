package com.willfp.ecoenchants.mechanics

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.proxy.ProxyConstants
import com.willfp.eco.util.StringUtils
import com.willfp.ecoenchants.enchants.EcoEnchants
import com.willfp.ecoenchants.proxy.proxies.OpenInventoryProxy
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.jetbrains.annotations.NotNull
import java.util.*
import kotlin.math.abs
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

@Suppress("DEPRECATION")
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

    /**
     * Called when items are placed into an anvil.
     *
     * @param event The event to listen to.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onAnvilPrepare(@NotNull event: PrepareAnvilEvent) {
        val player = event.viewers.getOrNull(0) as? Player ?: return

        if (this.plugin.getProxy(OpenInventoryProxy::class.java).getOpenInventory(player)::class.java.toString() == anvilGuiClass) {
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

            if (oldLeft == null || oldLeft.type != outItem.type) {
                return@run
            }

            if (result == FAIL || left == old) {
                return@run
            }

            val cost = oldCost + price

            if (cost == 0) {
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
            if (left.fast().getEnchants(true).containsKey(permanenceCurse)) {
                return FAIL
            }
        }

        if (right == null || right.type == Material.AIR) {
            if (left.fast().displayName == formattedItemName) {
                return FAIL
            }

            left.fast().displayName = formattedItemName.let { "§o$it" } // Not a great way to make it italic, but it works

            return AnvilResult(left, 0)
        }

        if (left.type != right.type) {
            if (right.type != Material.ENCHANTED_BOOK) {
                return FAIL
            }
        }

        left.fast().displayName = formattedItemName.let { "§o$it" } // Same again, it works though

        val leftEnchants = left.fast().getEnchants(true)
        val rightEnchants = right.fast().getEnchants(true)

        val outEnchants = leftEnchants.toMutableMap()

        for ((enchant, level) in rightEnchants) {
            if (enchant.canEnchantItem(left) && !outEnchants.containsKey(enchant)) {
                if (outEnchants.size < plugin.configYml.getInt("anvil.enchant-limit").infiniteIfNegative()) {
                    outEnchants[enchant] = level
                }
            }

            if (outEnchants.containsKey(enchant)) {
                val currentLevel = outEnchants[enchant]!!
                outEnchants[enchant] = if (level == currentLevel) {
                    min(enchant.maxLevel, level + 1)
                } else {
                    max(level, currentLevel)
                }
            }
        }

        val leftMeta = left.itemMeta
        val rightMeta = right.itemMeta

        if (leftMeta is Damageable && rightMeta is Damageable) {
            val maxDamage = left.type.maxDurability.toInt()
            val leftDurability = maxDamage - leftMeta.damage
            val rightDurability = maxDamage - rightMeta.damage
            val damage = maxDamage - max(maxDamage, leftDurability + rightDurability)

            leftMeta.damage = damage
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
        val xpCost = plugin.configYml.getDouble("anvil.cost-exponent").pow(enchantLevelDiff) * enchantLevelDiff

        return AnvilResult(left, xpCost.roundToInt())
    }
}
