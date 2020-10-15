package com.willfp.ecoenchants.enchantments;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.config.configs.EnchantmentConfig;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.util.Watcher;
import com.willfp.ecoenchants.util.Logger;
import com.willfp.ecoenchants.util.interfaces.Registerable;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public abstract class EcoEnchant extends Enchantment implements Listener, Registerable, Watcher {
    private String name;
    private String description;
    private final String permissionName;
    private final EnchantmentType type;

    private final double configVersion;
    private final EnchantmentConfig config;

    private boolean grindstoneable;
    private boolean canGetFromTable;
    private boolean canGetFromVillager;
    private boolean canGetFromLoot;
    private int maxLvl;
    private Set<Enchantment> conflicts;
    private EnchantmentRarity rarity;
    private final Set<Material> target = new HashSet<>();

    private boolean enabled;
    private boolean registered = false;

    /**
     * Create new EcoEnchant matching builder and prerequisites
     *
     * @param builder The {@link EcoEnchantBuilder} for enchantment
     */
    protected EcoEnchant(EcoEnchantBuilder builder, Prerequisite[] prerequisites) {
        super(NamespacedKey.minecraft(builder.key));

        this.type = builder.type;
        this.permissionName = builder.permission;
        this.configVersion = builder.configVersion;
        this.config = builder.config;

        if(!Arrays.stream(prerequisites).allMatch(Prerequisite::isMet)) {
            Arrays.stream(prerequisites).forEach(prerequisite -> {
                if(!prerequisite.isMet()) {
                    Logger.warn("Enchantment " + builder.key + " does not match prerequisite \"" + prerequisite.getDescription() + "\". It will not be available.");
                }
            });
            return;
        }

        this.update();
        this.add();
    }

    /**
     * Create new EcoEnchant matching builder
     *
     * @param builder The {@link EcoEnchantBuilder} for enchantment
     */
    protected EcoEnchant(EcoEnchantBuilder builder) {
        this(builder, new Prerequisite[]{});
    }

    /**
     * Update the enchantment based off config values
     */
    public void update() {
        config.loadFromLang();
        rarity = config.getRarity();
        conflicts = config.getEnchantments(EcoEnchants.GENERAL_LOCATION + "conflicts");
        grindstoneable = config.getBool(EcoEnchants.GENERAL_LOCATION + "grindstoneable");
        canGetFromTable = config.getBool(EcoEnchants.OBTAINING_LOCATION + "table");
        canGetFromVillager = config.getBool(EcoEnchants.OBTAINING_LOCATION + "villager");
        canGetFromLoot = config.getBool(EcoEnchants.OBTAINING_LOCATION + "loot");
        maxLvl = config.getInt(EcoEnchants.GENERAL_LOCATION + "maximum-level", 1);
        name = ChatColor.translateAlternateColorCodes('&', config.getString("name"));
        description = ChatColor.translateAlternateColorCodes('&', config.getString("description"));
        target.clear();
        config.getTargets().forEach(enchantmentTarget -> target.addAll(enchantmentTarget.getMaterials()));
        enabled = config.getBool("enabled", true);

        this.register();
    }

    /**
     * Register the enchantment with spigot
     * Only used internally
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

            this.registered = true;
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }

    private void add() {
        EcoEnchants.addNewEcoEnchant(this);
    }

    private void remove() {
        EcoEnchants.removeEcoEnchant(this);
    }

    /**
     * Get if enchantment can be removed in grindstone
     * @return Whether the enchantment can be removed
     */
    public boolean isGrindstoneable() {
        return grindstoneable;
    }

    /**
     * Get {@link EnchantmentType} of enchantment
     * @return The {@link EnchantmentType}
     */
    public EnchantmentType getType() { return this.type; }

    /**
     * Get a set of all conflicts
     * @return Conflicts
     */
    public Set<Enchantment> getConflicts() {
        return this.conflicts;
    }

    /**
     * Get if enchantment is enabled
     * @return If enabled
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Get if enchantment is registered
     * @return If registered
     */
    public boolean isRegistered() {
        return this.registered;
    }

    /**
     * Get permission name of enchantment
     * @return The permission name
     */
    public String getPermissionName() {
        return permissionName;
    }

    /**
     * Get description of enchantment
     * @return The description
     */
    public List<String> getDescription() {
        return Arrays.asList(WordUtils.wrap(description, ConfigManager.getConfig().getInt("lore.describe.wrap"), "\n", false).split("\\r?\\n"));
    }

    /**
     * Get if enchantment can be obtained from an enchanting table
     * @return If can be obtained
     */
    public boolean canGetFromTable() {
        return canGetFromTable;
    }

    /**
     * Get if enchantment can be obtained from a villager
     * @return If can be obtained
     */
    public boolean canGetFromVillager() {
        return canGetFromVillager;
    }

    /**
     * Get if enchantment can be obtained from chest loot
     * @return If can be obtained
     */
    public boolean canGetFromLoot() {
        return canGetFromLoot;
    }

    /**
     * Get {@link EnchantmentRarity} of enchantment
     * @return The enchantment rarity
     */
    public EnchantmentRarity getRarity() {
        return rarity;
    }

    /**
     * If enchantment conflicts with any enchantment in set
     * @param enchantments The set to test against
     * @return If there are any conflicts
     */
    public boolean conflictsWithAny(Set<? extends Enchantment> enchantments) {
        return conflicts.stream().anyMatch(enchantments::contains);
    }

    /**
     * Get enchantment cast to {@link Enchantment}
     * @return The enchantment
     */
    public Enchantment getEnchantment() {
        return this;
    }

    /**
     * Get the target of enchantment
     * @return Set of enchantable items
     */
    public Set<Material> getTarget() {
        return target;
    }

    /**
     * Get latest config version
     * @return The latest version
     */
    public double getConfigVersion() {
        return configVersion;
    }

    /**
     * Get {@link EnchantmentConfig} of enchantment
     * @return The config
     */
    public EnchantmentConfig getConfig() {
        return config;
    }

    /**
     * Get display name of enchantment.
     * Not deprecated, unlike {@link Enchantment#getName()}
     *
     * @return The display name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get max level of enchantment
     * @return The max level
     */
    @Override
    public int getMaxLevel() {
        return maxLvl;
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
     * Only here for compatibility with {@link Enchantment}
     *
     * @return Returns {@link EnchantmentTarget#ALL}. Do not use.
     *
     * @deprecated {@link EnchantmentTarget} is not supported due to its lack of flexibility. Use {@link EcoEnchant#getTarget()} instead.
     */
    @Override
    @Deprecated
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    /**
     * @return false
     * @deprecated Treasure enchantments do not exist. Use {@link EcoEnchant#getType()} instead.
     */
    @Override
    @Deprecated
    public boolean isTreasure() {
        return false;
    }

    /**
     * @return Returns if enchantment is cursed.
     *
     * @deprecated Use {@link EcoEnchant#getType()} instead.
     */
    @Override
    @Deprecated
    public boolean isCursed() {
        return this.type.equals(EnchantmentType.CURSE);
    }

    /**
     * Get if enchantment conflicts with specified enchantment
     * @param enchantment The enchantment to test against
     * @return If conflicts
     */
    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return conflicts.contains(enchantment);
    }

    /**
     * If enchantment can be applied to item
     * @param itemStack The {@link ItemStack} to test against
     * @return If can be applied
     */
    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return target.contains(itemStack.getType()) || itemStack.getType().equals(Material.BOOK) || itemStack.getType().equals(Material.ENCHANTED_BOOK);
    }

    /**
     * The types of {@link EcoEnchant}
     */
    public enum EnchantmentType {
        NORMAL,
        CURSE,
        SPECIAL,
        ARTIFACT
    }
}
