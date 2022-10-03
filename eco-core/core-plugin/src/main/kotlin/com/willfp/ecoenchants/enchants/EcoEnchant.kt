package com.willfp.ecoenchants.enchants

import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.config.ConfigType
import com.willfp.eco.core.config.TransientConfig
import com.willfp.eco.core.config.config
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.placeholder.PlayerStaticPlaceholder
import com.willfp.eco.util.StringUtils
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.mechanics.infiniteIfNegative
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.target.EnchantLookup.getEnchantLevel
import com.willfp.ecoenchants.target.EnchantmentTargets
import com.willfp.ecoenchants.target.TargetSlot
import com.willfp.ecoenchants.type.EnchantmentTypes
import com.willfp.libreforge.conditions.Conditions
import com.willfp.libreforge.conditions.ConfiguredCondition
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.event.Listener
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import java.io.File
import java.util.Objects


@Suppress("DEPRECATION")
abstract class EcoEnchant(
    val id: String,
    configProvider: (EcoEnchant) -> Config,
    protected val plugin: EcoEnchantsPlugin
) : Enchantment(NamespacedKey.minecraft(id)), EcoEnchantLike {
    final override val config by lazy { configProvider(this) }
    override val enchant by lazy { this }

    private val levels = Caffeine.newBuilder()
        .build<Int, EcoEnchantLevel>()

    override val displayName = config.getFormattedString("display-name")
    override val unformattedDisplayName = config.getString("display-name")

    val conditions: Set<ConfiguredCondition>

    val targets = config.getStrings("targets")
        .mapNotNull { EnchantmentTargets.getByID(it) }

    val slots: Set<TargetSlot>
        get() = targets.map { it.slot }.toSet()

    override val type = EnchantmentTypes.getByID(config.getString("type")) ?: EnchantmentTypes.values().first()

    override val enchantmentRarity =
        EnchantmentRarities.getByID(config.getString("rarity")) ?: EnchantmentRarities.values().first()

    private val conflictNames = config.getStrings("conflicts")

    val conflicts: Collection<Enchantment>
        get() = conflictNames.mapNotNull { getByKey(NamespacedKey.minecraft(it)) }

    constructor(
        config: Config,
        plugin: EcoEnchantsPlugin
    ) : this(config.getString("id"), { config }, plugin)

    constructor(
        id: String,
        config: Config,
        plugin: EcoEnchantsPlugin
    ) : this(id, { config }, plugin)

    @JvmOverloads
    constructor(
        id: String,
        plugin: EcoEnchantsPlugin,
        force: Boolean = true
    ) : this(
        id,
        {
            if (force) {
                EnchantmentConfig(id, it::class.java, plugin)
            } else {
                val file = File(plugin.dataFolder, "enchants")
                    .walk()
                    .firstOrNull { file -> file.nameWithoutExtension == id }

                if (file == null) {
                    // If config is deleted, don't register it
                    config {
                        "dont-register" to true
                    }
                } else {
                    TransientConfig(file, ConfigType.YAML)
                }
            }
        },
        plugin
    )

    init {
        checkDependencies()

        config.injectPlaceholders(
            PlayerStaticPlaceholder(
                "level"
            ) { p ->
                p.getEnchantLevel(this).toString()
            }
        )

        conditions = if (plugin.isLoaded) config.getSubsections("conditions").mapNotNull {
            Conditions.compile(it, "Enchantment $id")
        }.toSet() else emptySet()

        if (Bukkit.getPluginManager().getPermission("ecoenchants.fromtable.$id") == null) {
            val permission = Permission(
                "ecoenchants.fromtable.$id",
                "Allows getting $id from an Enchanting Table",
                PermissionDefault.TRUE
            )

            if (Bukkit.getPluginManager().getPermission("ecoenchants.fromtable.*") == null) {
                Bukkit.getPluginManager().addPermission(
                    Permission(
                        "ecoenchants.fromtable.*",
                        "Allows getting all enchantments from an Enchanting Table",
                        PermissionDefault.TRUE
                    )
                )
            }

            permission.addParent(
                Bukkit.getPluginManager().getPermission("ecoenchants.fromtable.*")!!,
                true
            )

            Bukkit.getPluginManager().addPermission(permission)
        }

        // Non-forced enchantments with deleted configs will have this as their entire config (see above),
        // and that way the enchantment isn't registered.
        if (!config.getBool("dont-register")) {
            register()
            if (plugin.isEnabled) {
                doOnInit()
            }
        }
    }

    private fun checkDependencies() {
        val missingPlugins = mutableListOf<String>()

        for (dependency in config.getStrings("dependencies")) {
            if (!Bukkit.getPluginManager().plugins.map { it.name }.containsIgnoreCase(dependency)) {
                missingPlugins += dependency
            }
        }

        if (missingPlugins.isNotEmpty()) {
            config.set("dont-register", true) // Just in case.
            throw MissingDependencyException(missingPlugins)
        }
    }

    private fun doOnInit() {
        onInit()
    }

    protected open fun onInit() {
        // Override when needed
    }

    private fun register() {
        EcoEnchants.addNewEnchant(this)
    }

    fun getLevel(level: Int): EcoEnchantLevel = levels.get(level) {
        createLevel(it)
    }

    open fun createLevel(level: Int) =
        EcoEnchantLevel(this, level, emptySet(), conditions)

    fun registerListener(listener: Listener) {
        this.plugin.eventManager.registerListener(listener)
    }

    @Deprecated(
        message = "getName is a legacy Spigot API",
        replaceWith = ReplaceWith("this.displayName(level)")
    )
    override fun getName(): String = this.id.uppercase()

    override fun getMaxLevel(): Int = this.config.getInt("max-level")

    override fun getStartLevel(): Int = 1

    @Deprecated(
        message = "getItemTargets is an incompatible Spigot API",
        replaceWith = ReplaceWith("this.targets")
    )
    override fun getItemTarget(): EnchantmentTarget = EnchantmentTarget.ALL

    val isEnchantable: Boolean = this.config.getBool("enchantable")

    @Deprecated(
        message = "Treasure enchantments do not exist in EcoEnchants",
        replaceWith = ReplaceWith("this.isEnchantable")
    )
    override fun isTreasure(): Boolean = !isEnchantable

    override fun isCursed(): Boolean {
        return false
    }

    fun conflictsWithDirectly(other: Enchantment): Boolean {
        return conflictNames.containsIgnoreCase(other.key.key)
    }

    override fun conflictsWith(other: Enchantment): Boolean {
        if (conflictsWithDirectly(other)) {
            return true
        }

        if (other !is EcoEnchant) {
            return false
        }

        return other.conflictsWithDirectly(this)
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        if (
            item.fast().getEnchants(true).keys
                .map { it.wrap() }
                .count { it.type == this.type } >= this.type.limit
        ) {
            return false
        }

        if (item.fast().getEnchants(true).any { (enchant, _) -> enchant.conflictsWithDeep(this) }) {
            return false
        }

        if (item.fast().getEnchants(true).size >= plugin.configYml.getInt("anvil.enchant-limit").infiniteIfNegative()) {
            return false
        }

        if (item.type == Material.ENCHANTED_BOOK) {
            return true
        }

        return targets.any { it.matches(item) }
    }

    override fun displayName(level: Int): Component {
        return StringUtils.toComponent(this.wrap().getFormattedName(level))
    }

    override fun isTradeable(): Boolean = this.config.getBool("tradeable")

    override fun isDiscoverable(): Boolean = this.config.getBool("discoverable")

    @Deprecated(
        message = "EcoEnchants do not have damage increase, this method is for sharpness/boa/smite",
        replaceWith = ReplaceWith("0.0f")
    )
    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float = 0.0f

    @Deprecated(
        message = "getActiveSlots is an incompatible Paper API",
        replaceWith = ReplaceWith("this.slots")
    )
    override fun getActiveSlots() = emptySet<EquipmentSlot>()

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated(
        message = "Enchant display overrides this system"
    )
    override fun translationKey(): String = "ecoenchants:enchantment.$id"

    @Deprecated(
        message = "EcoEnchants uses a custom system for enchantment rarity",
        replaceWith = ReplaceWith("this.enchantRarity")
    )
    override fun getRarity(): io.papermc.paper.enchantments.EnchantmentRarity {
        return io.papermc.paper.enchantments.EnchantmentRarity.RARE
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is EcoEnchant) {
            return false
        }

        return other.id == this.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun toString(): String {
        return "EcoEnchant{$key}"
    }
}
