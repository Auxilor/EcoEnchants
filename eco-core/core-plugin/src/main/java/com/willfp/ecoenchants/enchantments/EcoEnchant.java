package com.willfp.ecoenchants.enchantments;

import com.willfp.eco.core.Prerequisite;
import com.willfp.eco.core.config.interfaces.Config;
import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.eco.util.StringUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.config.configs.BaseEnchantmentConfig;
import com.willfp.ecoenchants.config.configs.EnchantmentConfig;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.custom.CustomEcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.enchantments.util.Watcher;
import com.willfp.ecoenchants.enchantments.util.requirements.Requirement;
import com.willfp.ecoenchants.enchantments.util.requirements.Requirements;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings({"deprecation", "RedundantSuppression"})
public abstract class EcoEnchant extends Enchantment implements Listener, Watcher {
    /**
     * Instance of EcoEnchants for enchantments to be able to access.
     */
    @Getter
    private final EcoEnchantsPlugin plugin = EcoEnchantsPlugin.getInstance();

    /**
     * The permission/config name of the enchantment.
     */
    @Getter
    private final String permissionName;

    /**
     * The type of the enchantment.
     */
    @Getter
    private final EnchantmentType type;

    /**
     * The enchantment's config.
     */
    @Getter
    private final EnchantmentConfig config;

    /**
     * The targets of the enchantment.
     */
    @Getter
    private final Set<EnchantmentTarget> targets = new HashSet<>();

    /**
     * The materials of the targets.
     */
    @Getter
    private final Set<Material> targetMaterials = new HashSet<>();

    /**
     * The names of the worlds that this enchantment is disabled in.
     */
    @Getter
    private final Set<String> disabledWorldNames = new HashSet<>();

    /**
     * The worlds that this enchantment is disabled in.
     */
    @Getter
    private final List<World> disabledWorlds = new ArrayList<>();

    /**
     * The display name of the enchantment.
     */
    @Getter
    private String displayName;

    /**
     * The description of the enchantment.
     */
    @Getter
    private String description;

    /**
     * If the enchantment can be removed in a grindstone.
     */
    @Getter
    private boolean grindstoneable;

    /**
     * If the enchantment can be obtained from an enchanting table.
     */
    @Getter
    private boolean availableFromTable;

    /**
     * If the enchantment can be obtained from a villager.
     */
    @Getter
    private boolean availableFromVillager;

    /**
     * If the enchantment can be obtained from a loot chest.
     */
    @Getter
    private boolean availableFromLoot;

    /**
     * The maximum level for the enchantment to be obtained naturally.
     */
    private int maxLevel;

    /**
     * The enchantments that conflict with this enchantment.
     */
    @Getter
    private Set<Enchantment> conflicts;

    /**
     * The rarity of the enchantment.
     */
    @Getter
    private EnchantmentRarity enchantmentRarity;

    /**
     * If the enchantment is enabled.
     */
    private boolean enabled;

    /**
     * Custom option flags for the enchantment.
     */
    private final List<String> flags = new ArrayList<>();

    /**
     * All the requirements needed in order to use the enchantment.
     */
    private final Map<Requirement, List<String>> requirements = new HashMap<>();

    /**
     * Cached players to see if they meet requirements.
     */
    private final Map<UUID, Boolean> cachedRequirements = new HashMap<>();

    /**
     * The requirement lore shown if the player doesn't meet the requirements.
     */
    @Getter
    private final List<String> requirementLore = new ArrayList<>();

    protected EcoEnchant(@NotNull final String key,
                         @NotNull final EnchantmentType type,
                         @Nullable final Config overrideConfig,
                         @NotNull final Prerequisite... prerequisites) {
        super(NamespacedKey.minecraft(key));

        this.type = type;
        this.permissionName = key.replace("_", "");
        this.config = new EnchantmentConfig(
                Objects.requireNonNullElseGet(overrideConfig, () -> new BaseEnchantmentConfig(
                        this.permissionName,
                        this.getClass(),
                        this,
                        this.getPlugin()
                )),
                this.permissionName,
                this,
                this.getPlugin()
        );

        if (Bukkit.getPluginManager().getPermission("ecoenchants.fromtable." + permissionName) == null) {
            Permission permission = new Permission(
                    "ecoenchants.fromtable." + permissionName,
                    "Allows getting " + permissionName + " from an Enchanting Table",
                    PermissionDefault.TRUE
            );
            permission.addParent(Objects.requireNonNull(Bukkit.getPluginManager().getPermission("ecoenchants.fromtable.*")), true);
            Bukkit.getPluginManager().addPermission(permission);
        }

        if (type.getRequiredToExtend() != null && !type.getRequiredToExtend().isInstance(this) && !(this instanceof CustomEcoEnchant)) {
            return;
        }

        if (!Prerequisite.areMet(prerequisites)) {
            return;
        }

        enabled = Objects.requireNonNullElse(config.getBoolOrNull("enabled"), true);

        if (!this.isEnabled() && this.getPlugin().getConfigYml().getBool("advanced.hard-disable.enabled")) {
            return;
        }

        this.update();

        EcoEnchants.addNewEcoEnchant(this);
    }

    /**
     * Create a new EcoEnchant.
     *
     * @param key           The key name of the enchantment
     * @param type          The type of the enchantment
     * @param prerequisites Optional {@link Prerequisite}s that must be met
     */
    protected EcoEnchant(@NotNull final String key,
                         @NotNull final EnchantmentType type,
                         @NotNull final Prerequisite... prerequisites) {
        this(key, type, null, prerequisites);
    }

    /**
     * Update the enchantment based off config values.
     * This can be overridden but may lead to unexpected behavior.
     */
    public void update() {
        config.loadFromLang();
        enchantmentRarity = config.getRarity();
        Validate.notNull(enchantmentRarity, "Rarity specified in " + this.permissionName + " is invalid!");
        conflicts = config.getEnchantments(EcoEnchants.GENERAL_LOCATION + "conflicts");
        grindstoneable = config.getBool(EcoEnchants.GENERAL_LOCATION + "grindstoneable");
        availableFromTable = config.getBool(EcoEnchants.OBTAINING_LOCATION + "table");
        availableFromVillager = config.getBool(EcoEnchants.OBTAINING_LOCATION + "villager");
        availableFromLoot = config.getBool(EcoEnchants.OBTAINING_LOCATION + "loot");
        maxLevel = config.getInt(EcoEnchants.GENERAL_LOCATION + "maximum-level", 1);
        displayName = config.getFormattedString("name");
        description = config.getString("description");
        disabledWorldNames.clear();
        disabledWorldNames.addAll(config.getStrings(EcoEnchants.GENERAL_LOCATION + "disabled-in-worlds"));
        disabledWorlds.clear();
        List<String> worldNames = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            worldNames.add(world.getName().toLowerCase());
        }
        List<String> disabledExistingWorldNames = disabledWorldNames.stream().filter(s -> worldNames.contains(s.toLowerCase())).toList();
        disabledWorlds.addAll(Bukkit.getWorlds().stream().filter(world -> disabledExistingWorldNames.contains(world.getName().toLowerCase())).toList());
        targets.clear();
        targetMaterials.clear();
        targets.addAll(config.getTargets());
        targets.forEach(enchantmentTarget -> targetMaterials.addAll(enchantmentTarget.getMaterials()));
        enabled = config.getBool("enabled");
        flags.clear();
        flags.addAll(config.getStrings(EcoEnchants.GENERAL_LOCATION + "flags"));
        EnchantmentUtils.registerPlaceholders(this);
        for (String req : config.getStrings(EcoEnchants.GENERAL_LOCATION + "requirements.list")) {
            List<String> split = Arrays.asList(req.split(":"));
            if (split.size() < 2) {
                continue;
            }

            Requirement requirement = Requirements.getByID(split.get(0).toLowerCase());

            this.requirements.put(requirement, split.subList(1, split.size()));
        }
        requirementLore.clear();
        requirementLore.addAll(config.getStrings(EcoEnchants.GENERAL_LOCATION + "requirements.not-met-lore"));

        postUpdate();
        this.register();
        this.clearCachedRequirements();
    }

    protected void postUpdate() {
        // Unused as some enchantments may have postUpdate tasks, however most won't.
    }

    /**
     * Register the enchantment with spigot.
     * Only used internally.
     */
    public void register() {
        EnchantmentUtils.register(this);
    }

    /**
     * Clear requirements cache.
     */
    public void clearCachedRequirements() {
        this.cachedRequirements.clear();
    }

    /**
     * Format the %value% placeholder in description lore.
     *
     * @param level The level of the enchantment.
     * @return The placeholder.
     */
    public String getPlaceholder(final int level) {
        return "unknown";
    }

    /**
     * Does the player meet the requirements to use this enchantment.
     *
     * @param entity The entity.
     * @return If the requirements are met.
     */
    public boolean areRequirementsMet(@NotNull final LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return true;
        }

        if (cachedRequirements.containsKey(player.getUniqueId())) {
            return cachedRequirements.get(player.getUniqueId());
        }

        for (Map.Entry<Requirement, List<String>> entry : requirements.entrySet()) {
            if (!entry.getKey().isMetBy(player, entry.getValue())) {
                cachedRequirements.put(player.getUniqueId(), false);
                return false;
            }
        }

        cachedRequirements.put(player.getUniqueId(), true);
        return true;
    }

    /**
     * Get description of enchantment line-wrapped.
     *
     * @return The description.
     */
    public List<String> getWrappedDescription() {
        return Arrays.asList(WordUtils.wrap(description, this.getPlugin().getConfigYml().getInt("lore.describe.wrap"), "\n", false).split("\\r?\\n"));
    }

    /**
     * If enchantment conflicts with any enchantment in collection.
     *
     * @param enchantments The collection to test against.
     * @return If there are any conflicts.
     */
    public boolean conflictsWithAny(@NotNull final Collection<? extends Enchantment> enchantments) {
        return conflicts.stream().anyMatch(enchantments::contains);
    }

    /**
     * If enchantment has specified flag.
     *
     * @param flag The flag.
     * @return If the enchantment has the flag.
     */
    public boolean hasFlag(@NotNull final String flag) {
        return this.flags.contains(flag);
    }

    /**
     * If enchantment is enabled.
     *
     * @return If enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Get the internal name of the enchantment.
     *
     * @return The name.
     * @deprecated Exists for parity.
     */
    @NotNull
    @Deprecated
    public String getName() {
        return this.getKey().getKey().toUpperCase();
    }

    /**
     * Get max level of enchantment.
     *
     * @return The max level.
     */
    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * @return 1
     */
    @Override
    public int getStartLevel() {
        return 1;
    }

    /**
     * Do not use this method.
     * Only here for compatibility with {@link Enchantment}.
     *
     * @return Returns {@link EnchantmentTarget#ALL}. Do not use.
     * @deprecated {@link EnchantmentTarget} is not supported due to its lack of flexibility. Use {@link EcoEnchant#getTargets()} instead.
     */
    @Override
    @Deprecated
    public @NotNull org.bukkit.enchantments.EnchantmentTarget getItemTarget() {
        return org.bukkit.enchantments.EnchantmentTarget.ALL;
    }

    /**
     * Treasure enchantments do not exist in EcoEnchants.
     *
     * @return false.
     * @see EnchantmentType#SPECIAL
     * @deprecated Treasure enchantments do not exist. Use {@link EcoEnchant#getType()} instead.
     */
    @Override
    @Deprecated
    public boolean isTreasure() {
        return this.type.equals(EnchantmentType.SPECIAL);
    }

    /**
     * While this method works, it is not recommended to use it.
     *
     * @return Returns if enchantment is cursed.
     * @see EnchantmentType#CURSE
     * @deprecated Use {@link EcoEnchant#getType()} instead.
     */
    @Override
    @Deprecated
    public boolean isCursed() {
        return this.type.equals(EnchantmentType.CURSE);
    }

    /**
     * Get if enchantment conflicts with specified enchantment.
     *
     * @param enchantment The enchantment to test against.
     * @return If conflicts.
     */
    @Override
    public boolean conflictsWith(@NotNull final Enchantment enchantment) {
        if (enchantment instanceof EcoEnchant) {
            return conflicts.contains(enchantment) || ((EcoEnchant) enchantment).conflicts.contains(this);
        }
        return conflicts.contains(enchantment);
    }

    /**
     * If enchantment can be applied to item.
     *
     * @param itemStack The {@link ItemStack} to test against.
     * @return If can be applied.
     */
    @Override
    public boolean canEnchantItem(@NotNull final ItemStack itemStack) {
        if (this.type.isSingular() && EcoEnchants.hasAnyOfType(itemStack, this.type)) {
            return FastItemStack.wrap(itemStack).getEnchantmentLevel(this, true) > 0;
        }

        if (itemStack.getType() == Material.BOOK || itemStack.getType() == Material.ENCHANTED_BOOK) {
            return true;
        }

        return targetMaterials.contains(itemStack.getType());
    }

    /**
     * Paper parity.
     * <p>
     * You should use EnchantmentCache instead.
     *
     * @param level The level.
     * @return The display name.
     * @deprecated Use {@link EnchantmentCache#getEntry(Enchantment)} instead.
     */
    @Deprecated
    @Override
    public @NotNull Component displayName(final int level) {
        return StringUtils.toComponent(EnchantmentCache.getEntry(this).getNameWithLevel(level));
    }

    /**
     * Paper parity.
     * <p>
     * You should use {@link EcoEnchant#isAvailableFromVillager()} instead.
     *
     * @return If tradeable.
     * @deprecated Use {@link EcoEnchant#isAvailableFromVillager()} instead.
     */
    @Deprecated
    @Override
    public boolean isTradeable() {
        return this.isAvailableFromVillager();
    }

    /**
     * Paper parity.
     * <p>
     * You should use {@link EcoEnchant#isAvailableFromLoot()} instead.
     *
     * @return If discoverable.
     * @deprecated Use {@link EcoEnchant#isAvailableFromLoot()} instead.
     */
    @Deprecated
    @Override
    public boolean isDiscoverable() {
        return this.isAvailableFromLoot();
    }

    /**
     * Paper parity.
     * <p>
     * EcoEnchants has its own systems for everything like this. Will always return 0.
     *
     * @param level          The level.
     * @param entityCategory The category.
     * @return 0
     * @deprecated EcoEnchants has its own systems for this.
     */
    @Deprecated
    @Override
    public float getDamageIncrease(final int level,
                                   @NotNull final EntityCategory entityCategory) {
        return 0;
    }

    /**
     * Paper parity.
     * <p>
     * EcoEnchants has its own systems for targets.
     * <p>
     * Use {@link EcoEnchant#getTargets()} instead.
     *
     * @return An empty set.
     * @deprecated Use {@link EcoEnchant#getTargets()}.
     */
    @Deprecated
    @Override
    public @NotNull Set<EquipmentSlot> getActiveSlots() {
        return new HashSet<>();
    }

    /**
     * Paper parity.
     * <p>
     * eco / EcoEnchants recodes display entirely.
     *
     * @return A translation key.
     * @deprecated Useless method, all items will be display differently using eco.
     */
    @Deprecated
    @Override
    public @NotNull String translationKey() {
        return "ecoenchants:enchantment." + this.getKey().getKey();
    }

    /**
     * Paper parity.
     * <p>
     * EcoEnchants has its own systems for rarity.
     * <p>
     * Use {@link EcoEnchant#getEnchantmentRarity()} instead.
     *
     * @return {@link io.papermc.paper.enchantments.EnchantmentRarity#COMMON}.
     * @deprecated Use {@link EcoEnchant#getEnchantmentRarity()}.
     */
    @Deprecated
    @Override
    public @NotNull io.papermc.paper.enchantments.EnchantmentRarity getRarity() {
        return io.papermc.paper.enchantments.EnchantmentRarity.COMMON;
    }

    @Override
    public String toString() {
        return "EcoEnchant{" + this.getKey() + "}";
    }
}
