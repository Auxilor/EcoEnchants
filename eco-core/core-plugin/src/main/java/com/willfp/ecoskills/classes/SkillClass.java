package com.willfp.ecoskills.classes;

import com.willfp.eco.util.StringUtils;
import com.willfp.ecoskills.config.ClassConfig;
import com.willfp.ecoskills.enchantments.EcoEnchants;
import com.willfp.ecoskills.enchantments.util.EnchantmentUtils;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SkillClass {
    /**
     * The name of the class.
     */
    @Getter
    private final String name;

    /**
     * The config for the skill class.
     */
    @Getter
    private final ClassConfig config;

    protected SkillClass(@NotNull final String name) {
        this.name = name;
        this.config = new ClassConfig(name, this.getClass());

        this.update();

        SkillClasses.addNewSkillClass(this);
    }

    public void update() {
        config.update();
        config.loadFromLang();
        rarity = config.getRarity();
        Validate.notNull(rarity, "Rarity specified in " + this.permissionName + " is invalid!");
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
}
