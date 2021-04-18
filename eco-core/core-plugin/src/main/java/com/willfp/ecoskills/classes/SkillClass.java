package com.willfp.ecoskills.classes;

import com.willfp.ecoskills.config.ClassConfig;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

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
    }
}
