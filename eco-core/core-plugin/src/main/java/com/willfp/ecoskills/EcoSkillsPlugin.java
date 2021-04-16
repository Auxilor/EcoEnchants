package com.willfp.ecoskills;

import com.willfp.eco.core.AbstractPacketAdapter;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.AbstractCommand;
import com.willfp.eco.core.integrations.IntegrationLoader;
import com.willfp.ecoskills.classes.SkillClasses;
import com.willfp.ecoskills.command.CommandClasses;
import com.willfp.ecoskills.command.CommandEsreload;
import lombok.Getter;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class EcoSkillsPlugin extends EcoPlugin {
    /**
     * Instance of the plugin.
     */
    @Getter
    private static EcoSkillsPlugin instance;

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public EcoSkillsPlugin() {
        super("EcoSkills", 79573, 7666, "com.willfp.ecoskills.proxy", "&3");
        instance = this;
    }

    /**
     * Code executed on plugin enable.
     */
    @Override
    public void enable() {
        this.getExtensionLoader().loadExtensions();

        if (this.getExtensionLoader().getLoadedExtensions().isEmpty()) {
            this.getLogger().info("&cNo extensions found");
        } else {
            this.getLogger().info("Extensions Loaded:");
            this.getExtensionLoader().getLoadedExtensions().forEach(extension -> this.getLogger().info("- " + extension.getName() + " v" + extension.getVersion()));
        }

        this.getLogger().info(SkillClasses.values().size() + " Classes Loaded");
    }

    /**
     * Code executed on plugin disable.
     */
    @Override
    public void disable() {
        this.getExtensionLoader().unloadExtensions();
    }

    /**
     * Nothing is called on plugin load.
     */
    @Override
    public void load() {
        // Nothing needs to be called on load
    }

    /**
     * Code executed on /ecoreload.
     */
    @Override
    public void onReload() {
        // Nothing needs to be called on reload
    }

    /**
     * Code executed after server is up.
     */
    @Override
    public void postLoad() {
        // Nothing needs to be called post-loads
    }

    /**
     * EcoEnchants-specific integrations.
     *
     * @return A list of all integrations.
     */
    @Override
    public List<IntegrationLoader> getIntegrationLoaders() {
        return Arrays.asList(

        );
    }

    /**
     * Ec0oSkills-specific commands.
     *
     * @return A list of all commands.
     */
    @Override
    public List<AbstractCommand> getCommands() {
        return Arrays.asList(
                new CommandClasses(this),
                new CommandEsreload(this)
        );
    }

    /**
     * Packet Adapters display.
     *
     * @return A list of packet adapters.
     */
    @Override
    public List<AbstractPacketAdapter> getPacketAdapters() {
        return new ArrayList<>();
    }

    /**
     * EcoSkills-specific listeners.
     *
     * @return A list of all listeners.
     */
    @Override
    public List<Listener> getListeners() {
        return Arrays.asList(

        );
    }

    @Override
    public List<Class<?>> getUpdatableClasses() {
        return Arrays.asList(

        );
    }
}
