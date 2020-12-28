package com.willfp.ecoenchants.enchantments;


import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.interfaces.Registerable;
import com.willfp.eco.util.optional.Prerequisite;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.config.EcoEnchantsConfigs;
import com.willfp.ecoenchants.config.configs.EnchantmentConfig;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.enchantments.util.Watcher;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "deprecation"})
public abstract class EcoEnchant extends Enchantment implements Listener, Registerable, Watcher {
    /**
     * Instance of EcoEnchants for enchantments to be able to access.
     */
    @Getter(AccessLevel.PROTECTED)
    private final AbstractEcoPlugin plugin = AbstractEcoPlugin.getInstance();

    /**
     * The display name of the enchantment.
     */
    private String name;

    /**
     * The description of the enchantment.
     */
    @Getter
    private String description;

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
    private EnchantmentRarity rarity;

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
     * If the enchantment is enabled.
     */
    @Getter
    private boolean enabled;

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
        super(NamespacedKey.minecraft(key));

        this.type = type;
        this.permissionName = key.replace("_", "");
        EcoEnchantsConfigs.addEnchantmentConfig(new EnchantmentConfig(this.permissionName, this.getClass(), this.type));
        this.config = EcoEnchantsConfigs.getEnchantmentConfig(this.permissionName);

        if (Bukkit.getPluginManager().getPermission("ecoenchants.fromtable." + permissionName) == null) {
            Permission permission = new Permission(
                    "ecoenchants.fromtable." + permissionName,
                    "Allows getting " + permissionName + " from an Enchanting Table",
                    PermissionDefault.TRUE
            );
            permission.addParent(Objects.requireNonNull(Bukkit.getPluginManager().getPermission("ecoenchants.fromtable.*")), true);
            Bukkit.getPluginManager().addPermission(permission);
        }

        //WorldguardManager.registerFlag(this.getPermissionName() + "-enabled", true);

        if (type.getRequiredToExtend() != null && !type.getRequiredToExtend().isInstance(this)) {
            return;
        }

        if (!Prerequisite.areMet(prerequisites)) {
            return;
        }

        this.update();
        EcoEnchants.addNewEcoEnchant(this);
    }

    /**
     * Update the enchantment based off config values.
     * This can be overridden but may lead to unexpected behavior.
     */
    public void update() {
        config.loadFromLang();
        rarity = config.getRarity();
        conflicts = config.getEnchantments(EcoEnchants.GENERAL_LOCATION + "conflicts");
        grindstoneable = config.getBool(EcoEnchants.GENERAL_LOCATION + "grindstoneable");
        availableFromTable = config.getBool(EcoEnchants.OBTAINING_LOCATION + "table");
        availableFromVillager = config.getBool(EcoEnchants.OBTAINING_LOCATION + "villager");
        availableFromLoot = config.getBool(EcoEnchants.OBTAINING_LOCATION + "loot");
        maxLevel = config.getInt(EcoEnchants.GENERAL_LOCATION + "maximum-level", 1);
        name = StringUtils.translate(config.getString("name"));
        description = StringUtils.translate(config.getString("description"));
        disabledWorldNames.clear();
        disabledWorldNames.addAll(config.getStrings(EcoEnchants.GENERAL_LOCATION + "disabled-in-worlds"));
        disabledWorlds.clear();
        List<String> worldNames = Bukkit.getWorlds().stream().map(World::getName).map(String::toLowerCase).collect(Collectors.toList());
        List<String> disabledExistingWorldNames = disabledWorldNames.stream().filter(s -> worldNames.contains(s.toLowerCase())).collect(Collectors.toList());
        disabledWorlds.addAll(Bukkit.getWorlds().stream().filter(world -> disabledExistingWorldNames.contains(world.getName().toLowerCase())).collect(Collectors.toList()));
        targets.clear();
        targetMaterials.clear();
        targets.addAll(config.getTargets());
        targets.forEach(enchantmentTarget -> targetMaterials.addAll(enchantmentTarget.getMaterials()));
        enabled = config.getBool("enabled");
        EnchantmentUtils.registerPlaceholders(this);

        postUpdate();
        this.register();
    }

    protected void postUpdate() {
        // Unused as some enchantments may have postUpdate tasks, however most won't.
    }

    /**
     * Register the enchantment with spigot.
     * Only used internally.
     */
    @Override
    public void register() {
        try {
            Field byIdField = Enchantment.class.getDeclaredField("byKey");
            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byIdField.setAccessible(true);
            byNameField.setAccessible(true);
            Map<NamespacedKey, Enchantment> byKey = (Map<NamespacedKey, Enchantment>) byIdField.get(null);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
            byKey.remove(this.getKey());
            byName.remove(this.getName());

            Map<String, Enchantment> byNameClone = new HashMap<>(byName);
            for (Map.Entry<String, Enchantment> entry : byNameClone.entrySet()) {
                if (entry.getValue().getKey().equals(this.getKey())) {
                    byName.remove(entry.getKey());
                }
            }

            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            f.setAccessible(false);

            Enchantment.registerEnchantment(this);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
    }

    /**
     * Get description of enchantment line-wrapped.
     *
     * @return The description.
     */
    public List<String> getWrappedDescription() {
        return Arrays.asList(WordUtils.wrap(description, Configs.CONFIG.getInt("lore.describe.wrap"), "\n", false).split("\\r?\\n"));
    }

    /**
     * If enchantment conflicts with any enchantment in set.
     *
     * @param enchantments The set to test against.
     * @return If there are any conflicts.
     */
    public boolean conflictsWithAny(@NotNull final Set<? extends Enchantment> enchantments) {
        return conflicts.stream().anyMatch(enchantments::contains);
    }

    /**
     * Get enchantment cast to {@link Enchantment}.
     *
     * @return The enchantment.
     */
    public Enchantment getEnchantment() {
        return this;
    }

    /**
     * Get the display name of the enchantment.
     * <p>
     * Not deprecated, unlike superclass.
     *
     * @return The name.
     */
    @Override
    @NotNull
    public String getName() {
        return name;
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
        return false;
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
        return targetMaterials.contains(itemStack.getType()) || itemStack.getType().equals(Material.BOOK) || itemStack.getType().equals(Material.ENCHANTED_BOOK);
    }
}
