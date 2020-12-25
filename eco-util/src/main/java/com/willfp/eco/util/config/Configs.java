package com.willfp.eco.util.config;

import com.willfp.eco.util.config.annotations.ConfigUpdater;
import com.willfp.eco.util.config.configs.Config;
import com.willfp.eco.util.config.configs.Lang;
import com.willfp.eco.util.interfaces.Updatable;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Configs implements Updatable {
    /**
     * The {@link BaseConfig} implementation for lang.yml.
     */
    public static final Lang LANG = new Lang();

    /**
     * The {@link BaseConfig} implementation for config.yml.
     */
    public static final Config CONFIG = new Config();

    /**
     * Update lang.yml and config.yml.
     *
     * @see BaseConfig
     */
    @ConfigUpdater
    public void update() {
        LANG.update();
        CONFIG.update();
    }
}
