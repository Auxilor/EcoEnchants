package com.willfp.ecoenchants.enchantments;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.config.configs.EnchantmentConfig;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.util.Watcher;
import com.willfp.ecoenchants.integrations.placeholder.PlaceholderEntry;
import com.willfp.ecoenchants.integrations.placeholder.PlaceholderManager;
import com.willfp.ecoenchants.util.NumberUtils;
import com.willfp.ecoenchants.util.StringUtils;
import com.willfp.ecoenchants.util.interfaces.ObjectCallable;
import com.willfp.ecoenchants.util.interfaces.Registerable;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "deprecation"})
public abstract class EcoEnchant extends Enchantment implements Listener, Registerable, Watcher {
    private String name;
    private String description;
    private final String permissionName;
    private final EnchantmentType type;

    private final EnchantmentConfig config;

    private boolean grindstoneable;
    private boolean canGetFromTable;
    private boolean canGetFromVillager;
    private boolean canGetFromLoot;
    private int maxLvl;
    private Set<Enchantment> conflicts;
    private EnchantmentRarity rarity;
    private final Set<com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget> target = new HashSet<>();
    private final Set<Material> targetMaterials = new HashSet<>();

    private boolean enabled;

    protected EcoEnchant(String key, EcoEnchant.EnchantmentType type, Prerequisite... prerequisites) {
        this(key, type, EcoEnchantsPlugin.class, prerequisites);
    }

    protected EcoEnchant(String key, EcoEnchant.EnchantmentType type, Class<?> plugin, Prerequisite... prerequisites) {
        super(NamespacedKey.minecraft(key));

        if(Pattern.matches("[a-z_]", key)) throw new InvalidEnchantmentException("Key must only contain lowercase letters and underscores");

        this.type = type;
        this.permissionName = key.replace("_", "");
        ConfigManager.addEnchantmentConfig(new EnchantmentConfig(this.permissionName, plugin, this.type));
        this.config = ConfigManager.getEnchantmentConfig(this.permissionName);

        if(Bukkit.getPluginManager().getPermission("ecoenchants.fromtable." + permissionName) == null) {
            Permission permission = new Permission(
                    "ecoenchants.fromtable." + permissionName,
                    "Allows getting " + permissionName + " from an Enchanting Table",
                    PermissionDefault.TRUE
            );
            permission.addParent(Bukkit.getPluginManager().getPermission("ecoenchants.fromtable.*"), true);
            Bukkit.getPluginManager().addPermission(permission);
        }

        if(!Prerequisite.areMet(prerequisites))
            return;

        this.update();
        this.add();
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
        name = StringUtils.translate(config.getString("name"));
        description = StringUtils.translate(config.getString("description"));
        target.clear();
        targetMaterials.clear();
        target.addAll(config.getTargets());
        target.forEach(enchantmentTarget -> targetMaterials.addAll(enchantmentTarget.getMaterials()));
        enabled = config.getBool("enabled", true);
        this.updatePlaceholders();

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
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }

    private void add() {
        EcoEnchants.addNewEcoEnchant(this);
    }

    private void remove() {
        EcoEnchants.removeEcoEnchant(this);
    }

    private void updatePlaceholders() {
        PlaceholderManager.registerPlaceholder(
                new PlaceholderEntry(this.getPermissionName() + "_" + "enabled", (player) -> {
                    return String.valueOf(this.isEnabled());
                })
        );

        this.getConfig().config.getKeys(true).forEach(string -> {
            String key = string.replaceAll("\\.", "_").replaceAll("-", "_");
            Object object = this.getConfig().config.get(string);

            if (object instanceof Integer) {
                PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(this.getPermissionName() + "_" + key, (player) -> {
                        return ((Integer) object).toString();
                    })
                );
            } else if(object instanceof String) {
                PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(this.getPermissionName() + "_" + key, (player) -> {
                        return (String) object;
                    })
                );
            } else if(object instanceof Double) {
                PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(this.getPermissionName() + "_" + key, (player) -> {
                        return NumberUtils.format((Double) object);
                    })
                );
            } else if(object instanceof Collection<?>) {
                PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(this.getPermissionName() + "_" + key, (player) -> {
                        Collection<?> c = (Collection<?>) object;
                        return c.stream().map(String::valueOf).collect(Collectors.joining(", "));
                    })
                );
            }
        });

        if(this.getConfig().config.get(EcoEnchants.CONFIG_LOCATION + "chance-per-level") != null) {
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(this.getPermissionName() + "_" + "chance_per_level", (player) -> {
                        return NumberUtils.format(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level"));
                    })
            );
        }

        if(this.getConfig().config.get(EcoEnchants.CONFIG_LOCATION + "multiplier") != null) {
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(this.getPermissionName() + "_" + "multiplier", (player) -> {
                        return NumberUtils.format(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier"));
                    })
            );
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(this.getPermissionName() + "_" + "multiplier_percentage", (player) -> {
                        return NumberUtils.format(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier") * 100);
                    })
            );
        }

        if(this instanceof Spell) {
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(this.getPermissionName() + "_" + "cooldown", (player) -> {
                        return NumberUtils.format(Spell.getCooldown((Spell) this, player));
                    }, true)
            );
            PlaceholderManager.registerPlaceholder(
                    new PlaceholderEntry(this.getPermissionName() + "_" + "cooldown_total", (player) -> {
                        return NumberUtils.format(((Spell) this).getCooldownTime());
                    })
            );
        }
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
        return targetMaterials;
    }

    /**
     * Get raw target of enchantment
     * @return {@link com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget}
     */
    public Set<com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget> getRawTargets() {
        return target;
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
        return targetMaterials.contains(itemStack.getType()) || itemStack.getType().equals(Material.BOOK) || itemStack.getType().equals(Material.ENCHANTED_BOOK);
    }

    public static class EnchantmentType {
        private static final Set<EnchantmentType> values = new HashSet<>();

        public static final EnchantmentType NORMAL = new EnchantmentType("normal", false, () -> ConfigManager.getLang().getString("not-curse-color"));
        public static final EnchantmentType CURSE = new EnchantmentType("curse", false, () -> ConfigManager.getLang().getString("curse-color"));
        public static final EnchantmentType SPECIAL = new EnchantmentType("special", () -> !ConfigManager.getConfig().getBool("types.special.allow-multiple"), () -> ConfigManager.getLang().getString("special-color"));
        public static final EnchantmentType ARTIFACT = new EnchantmentType("artifact", () -> !ConfigManager.getConfig().getBool("types.artifact.allow-multiple"), () -> ConfigManager.getLang().getString("artifact-color"));
        public static final EnchantmentType SPELL = new EnchantmentType("spell", true, () -> ConfigManager.getLang().getString("spell-color"));

        private boolean singular;
        private String color;
        private final String name;
        private final ObjectCallable<String> colorCallable;
        private final ObjectCallable<Boolean> singularCallable;

        public EnchantmentType(String name, boolean singular, String color) {
            this(name, () -> singular, () -> color);
        }

        public EnchantmentType(String name, boolean singular, ObjectCallable<String> colorCallable) {
            this(name, () -> singular, colorCallable);
        }

        public EnchantmentType(String name, ObjectCallable<Boolean> singularCallable, ObjectCallable<String> colorCallable) {
            this.name = name;
            this.singularCallable = singularCallable;
            this.colorCallable = colorCallable;
            color = colorCallable.call();
            singular = singularCallable.call();
            values.add(this);
        }

        private void refresh() {
            this.color = colorCallable.call();
            this.singular = singularCallable.call();
        }

        public String getColor() {
            return color;
        }

        public boolean isSingular() {
            return singular;
        }

        public String getName() {
            return name;
        }

        public static void update() {
            values.forEach(EnchantmentType::refresh);
        }

        public static Set<EnchantmentType> getValues() {
            return values;
        }
    }
}
