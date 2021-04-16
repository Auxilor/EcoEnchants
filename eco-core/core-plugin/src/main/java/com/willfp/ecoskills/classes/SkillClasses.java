package com.willfp.ecoskills.classes;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.willfp.eco.core.config.ConfigUpdater;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
public class SkillClasses {
    /**
     * All registered skill classes.
     */
    private static final BiMap<String, SkillClass> BY_NAME = HashBiMap.create();

    /**
     * Get all registered {@link SkillClass}es.
     *
     * @return A list of all {@link SkillClass}es.
     */
    public static List<SkillClass> values() {
        return ImmutableList.copyOf(BY_NAME.values());
    }

    /**
     * Update the skill classes.
     */
    @ConfigUpdater
    public static void update() {
        for (SkillClass skillClass : values()) {
            skillClass.update();
        }
    }

    /**
     * Add new {@link SkillClass} to EcoSkills.
     * <p>
     * Only for internal use, skill classes are automatically added in the constructor.
     *
     * @param skillClass The {@link SkillClass} to add.
     */
    public static void addNewSkillClass(@NotNull final SkillClass skillClass) {
        BY_NAME.inverse().remove(skillClass);
        BY_NAME.put(skillClass.getName(), skillClass);
    }
}
