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
import com.willfp.eco.util.drops.internal.FastCollatedDropQueue;
import com.willfp.eco.util.events.armorequip.ArmorListener;
import com.willfp.eco.util.events.armorequip.DispenserArmorListener;
import com.willfp.eco.util.events.entitydeathbyentity.EntityDeathByEntityListeners;
import com.willfp.eco.util.events.naturalexpgainevent.NaturalExpGainListeners;
import com.willfp.eco.util.extensions.loader.EcoExtensionLoader;
import com.willfp.eco.util.extensions.loader.ExtensionLoader;
import com.willfp.eco.util.integrations.placeholder.PlaceholderManager;
import com.willfp.eco.util.integrations.placeholder.plugins.PlaceholderIntegrationPAPI;
import com.willfp.eco.util.lambda.Callable;
import com.willfp.eco.util.updater.UpdateChecker;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractEcoPlugin extends JavaPlugin {
    protected static AbstractEcoPlugin instance;

    protected final String pluginName;
    protected final int resourceId;

    private final Map<String, Callable> integrations = new HashMap<>();

    private final Logger log;
    private final Scheduler scheduler;
    private final EventManager eventManager;
    private final NamespacedKeyFactory namespacedKeyFactory;
    private final MetadataValueFactory metadataValueFactory;
    private final ExtensionLoader extensionLoader;

    private final BukkitTask dropQueueCollationTask;

    protected boolean outdated = false;

    protected AbstractEcoPlugin(String pluginName, int resourceId) {
        this.pluginName = pluginName;
        this.resourceId = resourceId;

        this.log = new EcoLogger(this);
        this.scheduler = new EcoScheduler(this);
        this.eventManager = new EcoEventManager(this);
        this.namespacedKeyFactory = new NamespacedKeyFactory(this);
        this.metadataValueFactory = new MetadataValueFactory(this);
        this.extensionLoader = new EcoExtensionLoader(this);

        this.dropQueueCollationTask = new FastCollatedDropQueue.CollatedRunnable(this).getRunnableTask();
    }

    @Override
    public final void onEnable() {
        super.onLoad();

        this.getEventManager().registerEvents(new ArrowDataListener(this));
        this.getEventManager().registerEvents(new NaturalExpGainListeners());
        this.getEventManager().registerEvents(new ArmorListener());
        this.getEventManager().registerEvents(new DispenserArmorListener());
        this.getEventManager().registerEvents(new EntityDeathByEntityListeners(this));

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

        Set<String> enabledPlugins = Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(Plugin::getName).collect(Collectors.toSet());

        this.getDefaultIntegrations().forEach(((s, callable) -> {
            StringBuilder log = new StringBuilder();
            log.append(s).append(": ");
            if (enabledPlugins.contains(s)) {
                callable.call();
                log.append("&aENABLED");
            } else {
                log.append("&9DISABLED");
            }
            this.getLog().info(log.toString());
        }));

        this.enable();
    }

    @Override
    public final void onDisable() {
        super.onDisable();
        this.disable();
    }

    @Override
    public final void onLoad() {
        super.onLoad();

        instance = this;

        this.load();
    }

    public final Map<String, Callable> getDefaultIntegrations() {
        integrations.put("PlaceholderAPI", () -> PlaceholderManager.addIntegration(new PlaceholderIntegrationPAPI(this)));
        integrations.putAll(this.getIntegrations());
        return integrations;
    }

    public abstract void enable();

    public abstract void disable();

    public abstract void load();

    public abstract Map<String, Callable> getIntegrations();

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

    public ExtensionLoader getExtensionLoader() {
        return extensionLoader;
    }

    public boolean isOutdated() {
        return outdated;
    }

    public static AbstractEcoPlugin getInstance() {
        return instance;
    }
}
