package com.willfp.ecoenchants;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.integrations.IntegrationLoader;
import com.willfp.eco.util.TelekinesisUtils;
import com.willfp.ecoenchants.command.CommandEcoEnchants;
import com.willfp.ecoenchants.command.CommandEnchantinfo;
import com.willfp.ecoenchants.command.CommandGiverandombook;
import com.willfp.ecoenchants.config.RarityYml;
import com.willfp.ecoenchants.config.TargetYml;
import com.willfp.ecoenchants.config.VanillaEnchantsYml;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.support.merging.anvil.AnvilListeners;
import com.willfp.ecoenchants.enchantments.support.merging.grindstone.GrindstoneListeners;
import com.willfp.ecoenchants.enchantments.support.obtaining.EnchantingListeners;
import com.willfp.ecoenchants.enchantments.support.obtaining.LootPopulator;
import com.willfp.ecoenchants.enchantments.support.obtaining.VillagerListeners;
import com.willfp.ecoenchants.enchantments.util.ItemConversions;
import com.willfp.ecoenchants.enchantments.util.TimedRunnable;
import com.willfp.ecoenchants.enchantments.util.WatcherTriggers;
import com.willfp.ecoenchants.integrations.essentials.EssentialsManager;
import com.willfp.ecoenchants.integrations.essentials.plugins.IntegrationEssentials;
import com.willfp.ecoenchants.proxy.proxies.FastGetEnchantsProxy;
import com.willfp.ecoenchants.util.ProxyUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class EcoEnchantsPlugin extends EcoPlugin {
    /**
     * Instance of the plugin.
     */
    @Getter
    private static EcoEnchantsPlugin instance;

    /**
     * Rarity.yml.
     */
    @Getter
    private final RarityYml rarityYml;

    /**
     * Target.yml.
     */
    @Getter
    private final TargetYml targetYml;

    /**
     * VanillaEnchants.yml.
     */
    @Getter
    private final VanillaEnchantsYml vanillaEnchantsYml;

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public EcoEnchantsPlugin() {
        super(79573, 7666, "com.willfp.ecoenchants.proxy", "&a");
        instance = this;

        rarityYml = new RarityYml(this);
        targetYml = new TargetYml(this);
        vanillaEnchantsYml = new VanillaEnchantsYml(this);
    }

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

        TelekinesisUtils.registerTest(player -> ProxyUtils.getProxy(FastGetEnchantsProxy.class).getLevelOnItem(player.getInventory().getItemInMainHand(), EcoEnchants.TELEKINESIS) > 0);
    }

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

    @Override
    public void onReload() {
        targetYml.update();
        rarityYml.update();
        ((EnchantDisplay) this.getDisplayModule()).update();
        EcoEnchants.values().forEach((ecoEnchant -> {
            HandlerList.unregisterAll(ecoEnchant);

            this.getScheduler().runLater(() -> {
                if (ecoEnchant.isEnabled()) {
                    this.getEventManager().registerListener(ecoEnchant);

                    if (ecoEnchant instanceof TimedRunnable) {
                        this.getScheduler().syncRepeating((TimedRunnable) ecoEnchant, 5, ((TimedRunnable) ecoEnchant).getTime());
                    }
                }
            }, 1);
        }));
    }

    @Override
    public void postLoad() {
        if (this.getConfigYml().getBool("loot.enabled")) {
            Bukkit.getServer().getWorlds().forEach(world -> {
                List<BlockPopulator> populators = new ArrayList<>(world.getPopulators());
                populators.forEach((blockPopulator -> {
                    if (blockPopulator instanceof LootPopulator) {
                        world.getPopulators().remove(blockPopulator);
                    }
                }));
                world.getPopulators().add(new LootPopulator(this));
            });
        }
        EssentialsManager.registerEnchantments();
    }

    @Override
    public List<IntegrationLoader> getIntegrationLoaders() {
        return Arrays.asList(
                new IntegrationLoader("Essentials", () -> EssentialsManager.register(new IntegrationEssentials()))
        );
    }

    @Override
    public List<PluginCommand> getPluginCommands() {
        return Arrays.asList(
                new CommandEnchantinfo(this),
                new CommandEcoEnchants(this)
        );
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
                EnchantmentCache.class,
                EnchantmentRarity.class,
                EnchantmentTarget.class,
                EcoEnchants.class,
                CommandGiverandombook.class,
                CommandEnchantinfo.class,
                EnchantmentType.class,
                WatcherTriggers.class
        );
    }

    @Override
    @Nullable
    protected DisplayModule createDisplayModule() {
        return new EnchantDisplay(this);
    }

    @Override
    protected String getMinimumEcoVersion() {
        return "5.7.0";
    }

    @Override
    public EnchantDisplay getDisplayModule() {
        return (EnchantDisplay) super.getDisplayModule();
    }
}
