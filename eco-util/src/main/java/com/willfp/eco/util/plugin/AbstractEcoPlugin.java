package com.willfp.eco.util.plugin;

import com.willfp.eco.util.arrows.ArrowDataListener;
import com.willfp.eco.util.bukkit.events.EcoEventManager;
import com.willfp.eco.util.bukkit.events.EventManager;
import com.willfp.eco.util.bukkit.keys.NamespacedKeyFactory;
import com.willfp.eco.util.bukkit.logging.EcoLogger;
import com.willfp.eco.util.bukkit.logging.Logger;
import com.willfp.eco.util.bukkit.meta.MetadataValueFactory;
import com.willfp.eco.util.bukkit.scheduling.EcoScheduler;
import com.willfp.eco.util.bukkit.scheduling.Scheduler;
import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.drops.internal.DropManager;
import com.willfp.eco.util.drops.internal.FastCollatedDropQueue;
import com.willfp.eco.util.drops.telekinesis.EcoTelekinesisTests;
import com.willfp.eco.util.drops.telekinesis.TelekinesisTests;
import com.willfp.eco.util.drops.telekinesis.TelekinesisUtils;
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
import com.willfp.eco.util.packets.AbstractPacketAdapter;
import com.willfp.eco.util.updater.UpdateChecker;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("DeprecatedIsStillUsed")
public abstract class AbstractEcoPlugin extends JavaPlugin {
    protected static AbstractEcoPlugin instance;

    protected final String pluginName;
    protected final int resourceId;
    protected final int bStatsId;

    private final List<IntegrationLoader> integrations = new ArrayList<>();

    private final Logger log;
    private final Scheduler scheduler;
    private final EventManager eventManager;
    private final NamespacedKeyFactory namespacedKeyFactory;
    private final MetadataValueFactory metadataValueFactory;
    private final ExtensionLoader extensionLoader;

    protected boolean outdated = false;

    protected AbstractEcoPlugin(String pluginName, int resourceId, int bStatsId) {
        this.pluginName = pluginName;
        this.resourceId = resourceId;
        this.bStatsId = bStatsId;

        this.log = new EcoLogger(this);
        this.scheduler = new EcoScheduler(this);
        this.eventManager = new EcoEventManager(this);
        this.namespacedKeyFactory = new NamespacedKeyFactory(this);
        this.metadataValueFactory = new MetadataValueFactory(this);
        this.extensionLoader = new EcoExtensionLoader(this);

        if (!Bukkit.getServicesManager().isProvidedFor(TelekinesisTests.class)) {
            Bukkit.getServicesManager().register(TelekinesisTests.class, new EcoTelekinesisTests(this), this, ServicePriority.Normal);
        }

        TelekinesisUtils.update();
    }

    @Override
    public final void onEnable() {
        super.onLoad();

        this.getLog().info("==========================================");
        this.getLog().info("");
        this.getLog().info("Loading &a" + this.pluginName);
        this.getLog().info("Made by &aAuxilor&f - willfp.com");
        this.getLog().info("");
        this.getLog().info("==========================================");

        this.getEventManager().registerEvents(new ArrowDataListener(this));
        this.getEventManager().registerEvents(new NaturalExpGainListeners());
        this.getEventManager().registerEvents(new ArmorListener());
        this.getEventManager().registerEvents(new DispenserArmorListener());
        this.getEventManager().registerEvents(new EntityDeathByEntityListeners(this));

        new FastCollatedDropQueue.CollatedRunnable(this);

        new UpdateChecker(this, resourceId).getVersion(version -> {
            DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(this.getDescription().getVersion());
            DefaultArtifactVersion mostRecentVersion = new DefaultArtifactVersion(version);
            if (!(currentVersion.compareTo(mostRecentVersion) > 0 || currentVersion.equals(mostRecentVersion))) {
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
            if(!abstractPacketAdapter.isPostLoad()) abstractPacketAdapter.register();
        });

        this.getListeners().forEach(listener -> this.getEventManager().registerEvents(listener));

        this.getCommands().forEach(AbstractCommand::register);

        this.getScheduler().runLater(this::afterLoad, 1);

        this.enable();
    }

    @Override
    public final void onDisable() {
        super.onDisable();

        this.getEventManager().unregisterAllEvents();
        this.getScheduler().cancelAll();

        this.disable();
    }

    @Override
    public final void onLoad() {
        super.onLoad();

        instance = this;

        this.load();
    }

    public final void afterLoad() {
        this.getPacketAdapters().forEach(abstractPacketAdapter -> {
            if(abstractPacketAdapter.isPostLoad()) abstractPacketAdapter.register();
        });

        if (!Prerequisite.HasPaper.isMet()) {
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

    public final void onReload() {
        Configs.update();
        DropManager.update();
        this.getScheduler().cancelAll();
        new FastCollatedDropQueue.CollatedRunnable(this);

        this.reload();
    }

    public final List<IntegrationLoader> getDefaultIntegrations() {
        integrations.add(new IntegrationLoader("PlaceholderAPI", () -> PlaceholderManager.addIntegration(new PlaceholderIntegrationPAPI(this))));

        // AntiGrief
        integrations.add(new IntegrationLoader("WorldGuard", () -> AntigriefManager.register(new AntigriefWorldGuard())));
        integrations.add(new IntegrationLoader("GriefPrevention", () -> AntigriefManager.register(new AntigriefGriefPrevention())));
        integrations.add(new IntegrationLoader("FactionsUUID", () -> AntigriefManager.register(new AntigriefFactionsUUID())));
        integrations.add(new IntegrationLoader("Towny", () -> AntigriefManager.register(new AntigriefTowny())));
        integrations.add(new IntegrationLoader("Lands", () -> AntigriefManager.register(new AntigriefLands(this))));
        integrations.add(new IntegrationLoader("Kingdoms", () -> AntigriefManager.register(new AntigriefKingdoms())));

        // Anticheat
        integrations.add(new IntegrationLoader("AAC", () -> AnticheatManager.register(new AnticheatAAC())));
        integrations.add(new IntegrationLoader("Matrix", () -> AnticheatManager.register(new AnticheatMatrix())));
        integrations.add(new IntegrationLoader("NoCheatPlus", () -> AnticheatManager.register(new AnticheatNCP())));
        integrations.add(new IntegrationLoader("Spartan", () -> AnticheatManager.register(new AnticheatSpartan())));
        integrations.addAll(this.getIntegrations());
        return integrations;
    }

    public abstract void enable();

    public abstract void disable();

    public abstract void load();

    public abstract void reload();

    public abstract void postLoad();

    public abstract List<IntegrationLoader> getIntegrations();

    public abstract List<AbstractCommand> getCommands();

    public abstract List<AbstractPacketAdapter> getPacketAdapters();

    public abstract List<Listener> getListeners();

    public final Logger getLog() {
        return log;
    }

    public final Scheduler getScheduler() {
        return scheduler;
    }

    public final EventManager getEventManager() {
        return eventManager;
    }

    public final NamespacedKeyFactory getNamespacedKeyFactory() {
        return namespacedKeyFactory;
    }

    public final MetadataValueFactory getMetadataValueFactory() {
        return metadataValueFactory;
    }

    public final ExtensionLoader getExtensionLoader() {
        return extensionLoader;
    }

    public final boolean isOutdated() {
        return outdated;
    }

    public static AbstractEcoPlugin getInstance() {
        return instance;
    }
}
