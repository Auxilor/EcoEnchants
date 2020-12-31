package com.willfp.eco.util.plugin;

import com.willfp.eco.util.arrows.ArrowDataListener;
import com.willfp.eco.util.bukkit.events.EcoEventManager;
import com.willfp.eco.util.bukkit.events.EventManager;
import com.willfp.eco.util.bukkit.keys.NamespacedKeyFactory;
import com.willfp.eco.util.bukkit.logging.EcoLogger;
import com.willfp.eco.util.bukkit.logging.Logger;
import com.willfp.eco.util.bukkit.meta.MetadataValueFactory;
import com.willfp.eco.util.bukkit.scheduling.EcoScheduler;
import com.willfp.eco.util.bukkit.scheduling.RunnableFactory;
import com.willfp.eco.util.bukkit.scheduling.Scheduler;
import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.config.updating.ConfigHandler;
import com.willfp.eco.util.drops.internal.DropManager;
import com.willfp.eco.util.drops.internal.FastCollatedDropQueue;
import com.willfp.eco.util.events.armorequip.ArmorListener;
import com.willfp.eco.util.events.armorequip.DispenserArmorListener;
import com.willfp.eco.util.events.entitydeathbyentity.EntityDeathByEntityListeners;
import com.willfp.eco.util.events.naturalexpgainevent.NaturalExpGainListeners;
import com.willfp.eco.util.extensions.loader.EcoExtensionLoader;
import com.willfp.eco.util.extensions.loader.ExtensionLoader;
import com.willfp.eco.util.integrations.IntegrationLoader;
import com.willfp.eco.util.integrations.anticheat.AnticheatManager;
import com.willfp.eco.util.integrations.anticheat.plugins.AnticheatAAC;
import com.willfp.eco.util.integrations.anticheat.plugins.AnticheatMatrix;
import com.willfp.eco.util.integrations.anticheat.plugins.AnticheatNCP;
import com.willfp.eco.util.integrations.anticheat.plugins.AnticheatSpartan;
import com.willfp.eco.util.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.integrations.antigrief.plugins.AntigriefFactionsUUID;
import com.willfp.eco.util.integrations.antigrief.plugins.AntigriefGriefPrevention;
import com.willfp.eco.util.integrations.antigrief.plugins.AntigriefKingdoms;
import com.willfp.eco.util.integrations.antigrief.plugins.AntigriefLands;
import com.willfp.eco.util.integrations.antigrief.plugins.AntigriefTowny;
import com.willfp.eco.util.integrations.antigrief.plugins.AntigriefWorldGuard;
import com.willfp.eco.util.integrations.placeholder.PlaceholderManager;
import com.willfp.eco.util.integrations.placeholder.plugins.PlaceholderIntegrationPAPI;
import com.willfp.eco.util.optional.Prerequisite;
import com.willfp.eco.util.protocollib.AbstractPacketAdapter;
import com.willfp.eco.util.updater.UpdateChecker;
import lombok.Getter;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractEcoPlugin extends JavaPlugin {
    /**
     * The instance of the plugin.
     */
    @Getter
    private static AbstractEcoPlugin instance;

    /**
     * The name of the plugin.
     */
    @Getter
    private final String pluginName;

    /**
     * The spigot resource ID of the plugin.
     */
    @Getter
    private final int resourceId;

    /**
     * The bStats resource ID of the plugin.
     */
    @Getter
    private final int bStatsId;

    /**
     * The package where proxy implementations are.
     */
    @Getter
    private final String proxyPackage;

    /**
     * Set of external plugin integrations.
     */
    private final List<IntegrationLoader> integrationLoaders = new ArrayList<>();

    /**
     * Set of classes to be processed on config update.
     */
    private final List<Class<?>> updatableClasses = new ArrayList<>();

    /**
     * The internal plugin logger.
     */
    @Getter
    private final Logger log;

    /**
     * The internal plugin scheduler.
     */
    @Getter
    private final Scheduler scheduler;

    /**
     * The internal plugin Event Manager.
     */
    @Getter
    private final EventManager eventManager;

    /**
     * The internal factory to produce {@link org.bukkit.NamespacedKey}s.
     */
    @Getter
    private final NamespacedKeyFactory namespacedKeyFactory;

    /**
     * The internal factory to produce {@link org.bukkit.metadata.FixedMetadataValue}s.
     */
    @Getter
    private final MetadataValueFactory metadataValueFactory;

    /**
     * The internal factory to produce {@link com.willfp.eco.util.bukkit.scheduling.EcoBukkitRunnable}s.
     */
    @Getter
    private final RunnableFactory runnableFactory;

    /**
     * The loader for all plugin extensions.
     *
     * @see com.willfp.eco.util.extensions.Extension
     */
    @Getter
    private final ExtensionLoader extensionLoader;

    /**
     * The handler class for updatable classes.
     */
    @Getter
    private final ConfigHandler configHandler;

    /**
     * If the server is running an outdated version of the plugin.
     */
    @Getter
    private boolean outdated = false;

    /**
     * Create a new plugin.
     *
     * @param pluginName   The name of the plugin.
     * @param resourceId   The spigot resource ID for the plugin.
     * @param bStatsId     The bStats resource ID for the plugin.
     * @param proxyPackage The package where proxy implementations are stored.
     */
    protected AbstractEcoPlugin(@NotNull final String pluginName,
                                final int resourceId,
                                final int bStatsId,
                                @NotNull final String proxyPackage) {
        this.pluginName = pluginName;
        this.resourceId = resourceId;
        this.bStatsId = bStatsId;
        this.proxyPackage = proxyPackage;

        this.log = new EcoLogger(this);
        this.scheduler = new EcoScheduler(this);
        this.eventManager = new EcoEventManager(this);
        this.namespacedKeyFactory = new NamespacedKeyFactory(this);
        this.metadataValueFactory = new MetadataValueFactory(this);
        this.runnableFactory = new RunnableFactory(this);
        this.extensionLoader = new EcoExtensionLoader(this);
        this.configHandler = new ConfigHandler(this);
    }

    /**
     * Default code to be executed on plugin enable.
     */
    @Override
    public final void onEnable() {
        super.onLoad();

        this.getLog().info("==========================================");
        this.getLog().info("");
        this.getLog().info("Loading &a" + this.pluginName);
        this.getLog().info("Made by &aAuxilor&f - willfp.com");
        this.getLog().info("");
        this.getLog().info("==========================================");

        this.getEventManager().registerListener(new ArrowDataListener(this));
        this.getEventManager().registerListener(new NaturalExpGainListeners());
        this.getEventManager().registerListener(new ArmorListener());
        this.getEventManager().registerListener(new DispenserArmorListener());
        this.getEventManager().registerListener(new EntityDeathByEntityListeners(this));

        new FastCollatedDropQueue.CollatedRunnable(this);

        new UpdateChecker(this).getVersion(version -> {
            DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(this.getDescription().getVersion());
            DefaultArtifactVersion mostRecentVersion = new DefaultArtifactVersion(version);
            if (!(currentVersion.compareTo(mostRecentVersion) > 0 || currentVersion.equals(mostRecentVersion))) {
                this.outdated = true;
                this.getScheduler().runTimer(() -> {
                    this.getLog().info("&c " + this.pluginName + " is out of date! (Version " + this.getDescription().getVersion() + ")");
                    this.getLog().info("&cThe newest version is &f" + version);
                    this.getLog().info("&cDownload the new version!");
                }, 0, 864000);
            }
        });

        new Metrics(this, this.bStatsId);

        Set<String> enabledPlugins = Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(Plugin::getName).collect(Collectors.toSet());

        this.getDefaultIntegrations().forEach((integrationLoader -> {
            StringBuilder infoBuilder = new StringBuilder();
            infoBuilder.append(integrationLoader.getPluginName()).append(": ");
            if (enabledPlugins.contains(integrationLoader.getPluginName())) {
                integrationLoader.load();
                infoBuilder.append("&aENABLED");
            } else {
                infoBuilder.append("&9DISABLED");
            }
            this.getLog().info(infoBuilder.toString());
        }));
        this.getLog().info("");

        Prerequisite.update();

        this.getPacketAdapters().forEach(abstractPacketAdapter -> {
            if (!abstractPacketAdapter.isPostLoad()) {
                abstractPacketAdapter.register();
            }
        });


        updatableClasses.add(Configs.class);
        updatableClasses.add(DropManager.class);
        updatableClasses.addAll(this.getUpdatableClasses());

        this.getListeners().forEach(listener -> this.getEventManager().registerListener(listener));

        this.getCommands().forEach(AbstractCommand::register);

        this.getScheduler().runLater(this::afterLoad, 1);

        this.updatableClasses.forEach(clazz -> this.getConfigHandler().registerUpdatableClass(clazz));

        this.enable();
    }

    /**
     * Default code to be executed on plugin disable.
     */
    @Override
    public final void onDisable() {
        super.onDisable();

        this.getEventManager().unregisterAllListeners();
        this.getScheduler().cancelAll();

        this.disable();
    }

    /**
     * Default code to be executed on plugin load.
     */
    @Override
    public final void onLoad() {
        super.onLoad();

        instance = this;

        this.load();
    }

    /**
     * Default code to be executed after the server is up.
     */
    public final void afterLoad() {
        this.getPacketAdapters().forEach(abstractPacketAdapter -> {
            if (abstractPacketAdapter.isPostLoad()) {
                abstractPacketAdapter.register();
            }
        });

        if (!Prerequisite.HAS_PAPER.isMet()) {
            this.getLog().error("");
            this.getLog().error("----------------------------");
            this.getLog().error("");
            this.getLog().error("You don't seem to be running paper!");
            this.getLog().error("Paper is strongly recommended for all servers,");
            this.getLog().error("and some things may not function properly without it");
            this.getLog().error("Download Paper from &fhttps://papermc.io");
            this.getLog().error("");
            this.getLog().error("----------------------------");
            this.getLog().error("");
        }

        this.postLoad();

        this.reload();

        this.getLog().info("Loaded &a" + this.pluginName);
    }

    /**
     * Default code to be executed on plugin reload.
     */
    public final void reload() {
        this.getConfigHandler().callUpdate();
        this.getScheduler().cancelAll();
        new FastCollatedDropQueue.CollatedRunnable(this);

        this.onReload();
    }

    /**
     * Default integrations that exist within {@link AbstractEcoPlugin}.
     *
     * @return The default integrations.
     */
    public final List<IntegrationLoader> getDefaultIntegrations() {
        integrationLoaders.add(new IntegrationLoader("PlaceholderAPI", () -> PlaceholderManager.addIntegration(new PlaceholderIntegrationPAPI(this))));

        // AntiGrief
        integrationLoaders.add(new IntegrationLoader("WorldGuard", () -> AntigriefManager.register(new AntigriefWorldGuard())));
        integrationLoaders.add(new IntegrationLoader("GriefPrevention", () -> AntigriefManager.register(new AntigriefGriefPrevention())));
        integrationLoaders.add(new IntegrationLoader("FactionsUUID", () -> AntigriefManager.register(new AntigriefFactionsUUID())));
        integrationLoaders.add(new IntegrationLoader("Towny", () -> AntigriefManager.register(new AntigriefTowny())));
        integrationLoaders.add(new IntegrationLoader("Lands", () -> AntigriefManager.register(new AntigriefLands(this))));
        integrationLoaders.add(new IntegrationLoader("Kingdoms", () -> AntigriefManager.register(new AntigriefKingdoms())));

        // Anticheat
        integrationLoaders.add(new IntegrationLoader("AAC5", () -> AnticheatManager.register(new AnticheatAAC())));
        integrationLoaders.add(new IntegrationLoader("Matrix", () -> AnticheatManager.register(new AnticheatMatrix())));
        integrationLoaders.add(new IntegrationLoader("NoCheatPlus", () -> AnticheatManager.register(new AnticheatNCP())));
        integrationLoaders.add(new IntegrationLoader("Spartan", () -> AnticheatManager.register(new AnticheatSpartan())));
        integrationLoaders.addAll(this.getIntegrationLoaders());
        return integrationLoaders;
    }

    /**
     * The plugin-specific code to be executed on enable.
     */
    public abstract void enable();

    /**
     * The plugin-specific code to be executed on disable.
     */
    public abstract void disable();

    /**
     * The plugin-specific code to be executed on load.
     */
    public abstract void load();

    /**
     * The plugin-specific code to be executed on reload.
     */
    public abstract void onReload();

    /**
     * The plugin-specific code to be executed after the server is up.
     */
    public abstract void postLoad();

    /**
     * The plugin-specific integrations to be tested and loaded.
     *
     * @return A list of integrations.
     */
    public abstract List<IntegrationLoader> getIntegrationLoaders();

    /**
     * The command to be registered.
     *
     * @return A list of commands.
     */
    public abstract List<AbstractCommand> getCommands();

    /**
     * ProtocolLib packet adapters to be registered.
     * <p>
     * If the plugin does not require ProtocolLib this can be left empty.
     *
     * @return A list of packet adapters.
     */
    public abstract List<AbstractPacketAdapter> getPacketAdapters();

    /**
     * All listeners to be registered.
     *
     * @return A list of all listeners.
     */
    public abstract List<Listener> getListeners();

    /**
     * All updatable classes.
     *
     * @return A list of all updatable classes.
     */
    public abstract List<Class<?>> getUpdatableClasses();
}
