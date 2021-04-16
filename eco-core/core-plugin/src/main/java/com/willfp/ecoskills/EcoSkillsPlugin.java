package com.willfp.ecoskills;

import com.willfp.eco.core.AbstractPacketAdapter;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.AbstractCommand;
import com.willfp.eco.core.integrations.IntegrationLoader;
import com.willfp.ecoskills.command.CommandClasses;
import com.willfp.ecoskills.command.CommandEsreload;
import com.willfp.ecoskills.enchantments.EcoEnchants;
import com.willfp.ecoskills.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoskills.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoskills.enchantments.meta.EnchantmentType;
import com.willfp.ecoskills.enchantments.support.merging.anvil.AnvilListeners;
import com.willfp.ecoskills.enchantments.support.merging.grindstone.GrindstoneListeners;
import com.willfp.ecoskills.enchantments.support.obtaining.EnchantingListeners;
import com.willfp.ecoskills.enchantments.support.obtaining.LootPopulator;
import com.willfp.ecoskills.enchantments.support.obtaining.VillagerListeners;
import com.willfp.ecoskills.enchantments.util.ItemConversions;
import com.willfp.ecoskills.enchantments.util.WatcherTriggers;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.generator.BlockPopulator;

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

        this.getLogger().info(EcoEnchants.values().size() + " Enchantments Loaded");
    }

    /**
     * Code executed on plugin disable.
     */
    @Override
    public void disable() {
        Bukkit.getServer().getWorlds().forEach(world -> {
            List<BlockPopulator> populators = new ArrayList<>(world.getPopulators());
            populators.forEach((blockPopulator -> {
                if (blockPopulator instanceof LootPopulator) {
                    world.getPopulators().remove(blockPopulator);
                }
            }));
        });

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
     * EcoEnchants-specific commands.
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
     * Packet Adapters for enchant display.
     *
     * @return A list of packet adapters.
     */
    @Override
    public List<AbstractPacketAdapter> getPacketAdapters() {
        return new ArrayList<>();
    }

    /**
     * EcoEnchants-specific listeners.
     *
     * @return A list of all listeners.
     */
    @Override
    public List<Listener> getListeners() {
        return Arrays.asList(
                new EnchantingListeners(this),
                new GrindstoneListeners(this),
                new AnvilListeners(this),
                new WatcherTriggers(this),
                new VillagerListeners(this),
                new ItemConversions(this)
        );
    }

    @Override
    public List<Class<?>> getUpdatableClasses() {
        return Arrays.asList(
                EnchantmentRarity.class,
                EnchantmentTarget.class,
                EcoEnchants.class,
                EnchantmentType.class,
                WatcherTriggers.class
        );
    }
}
