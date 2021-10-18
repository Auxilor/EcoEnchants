package com.willfp.ecoenchants;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.eco.core.integrations.IntegrationLoader;
import com.willfp.eco.util.TelekinesisUtils;
import com.willfp.ecoenchants.command.CommandEcoEnchants;
import com.willfp.ecoenchants.command.CommandEnchantinfo;
import com.willfp.ecoenchants.config.RarityYml;
import com.willfp.ecoenchants.config.TargetYml;
import com.willfp.ecoenchants.config.VanillaEnchantsYml;
import com.willfp.ecoenchants.data.SaveHandler;
import com.willfp.ecoenchants.data.storage.DataHandler;
import com.willfp.ecoenchants.data.storage.MySQLDataHandler;
import com.willfp.ecoenchants.data.storage.YamlDataHandler;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.support.merging.anvil.AnvilListeners;
import com.willfp.ecoenchants.enchantments.support.merging.grindstone.GrindstoneListeners;
import com.willfp.ecoenchants.enchantments.support.obtaining.EnchantingListeners;
import com.willfp.ecoenchants.enchantments.support.obtaining.LootPopulator;
import com.willfp.ecoenchants.enchantments.support.obtaining.VillagerListeners;
import com.willfp.ecoenchants.enchantments.util.ItemConversions;
import com.willfp.ecoenchants.enchantments.util.TimedRunnable;
import com.willfp.ecoenchants.enchantments.util.WatcherTriggers;
import com.willfp.ecoenchants.integrations.registration.RegistrationManager;
import com.willfp.ecoenchants.integrations.registration.plugins.IntegrationEssentials;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class EcoEnchantsPlugin extends EcoPlugin {
    /**
     * Instance of the plugin.
     */
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
     * The data handler.
     */
    @Getter
    private final DataHandler dataHandler;

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public EcoEnchantsPlugin() {
        super(490, 7666, "com.willfp.ecoenchants.proxy", "&a", true);
        instance = this;

        rarityYml = new RarityYml(this);
        targetYml = new TargetYml(this);
        vanillaEnchantsYml = new VanillaEnchantsYml(this);
        dataHandler = this.getConfigYml().getBool("mysql.enabled")
                ? new MySQLDataHandler(this) : new YamlDataHandler(this);
    }

    @Override
    protected void handleEnable() {
        this.getLogger().info(EcoEnchants.values().size() + " Enchantments Loaded");

        TelekinesisUtils.registerTest(player -> FastItemStack.wrap(player.getInventory().getItemInMainHand()).getLevelOnItem(EcoEnchants.TELEKINESIS, false) > 0);
    }

    @Override
    protected void handleDisable() {
        SaveHandler.Companion.save(this);
        for (World world : Bukkit.getServer().getWorlds()) {
            world.getPopulators().removeIf(blockPopulator -> blockPopulator instanceof LootPopulator);
        }
    }

    @Override
    protected void handleReload() {
        this.getDisplayModule().update();
        for (EcoEnchant enchant : EcoEnchants.values()) {
            HandlerList.unregisterAll(enchant);
            this.getScheduler().runLater(() -> {
                if (enchant.isEnabled()) {
                    this.getEventManager().registerListener(enchant);

                    if (enchant instanceof TimedRunnable) {
                        this.getScheduler().syncRepeating((TimedRunnable) enchant, 5, ((TimedRunnable) enchant).getTime());
                    }
                }
            }, 1);
        }
        this.getScheduler().runTimer(() -> {
            for (EcoEnchant enchant : EcoEnchants.values()) {
                enchant.clearCachedRequirements();
            }
        }, 300, 300);

        SaveHandler.Companion.save(this);
        this.getScheduler().runTimer(new SaveHandler.Runnable(this), 20000, 20000);
    }

    @Override
    protected void handleAfterLoad() {
        if (this.getConfigYml().getBool("loot.enabled")) {
            for (World world : Bukkit.getServer().getWorlds()) {
                world.getPopulators().removeIf(blockPopulator -> blockPopulator instanceof LootPopulator);
                world.getPopulators().add(new LootPopulator(this));
            }
        }
        RegistrationManager.registerEnchantments();
    }

    @Override
    protected List<IntegrationLoader> loadIntegrationLoaders() {
        return Arrays.asList(
                new IntegrationLoader("Essentials", () -> RegistrationManager.register(new IntegrationEssentials()))
        );
    }

    @Override
    protected List<PluginCommand> loadPluginCommands() {
        return Arrays.asList(
                new CommandEnchantinfo(this),
                new CommandEcoEnchants(this)
        );
    }

    @Override
    protected List<Listener> loadListeners() {
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
    @Nullable
    protected DisplayModule createDisplayModule() {
        return new EnchantDisplay(this);
    }

    @Override
    public EnchantDisplay getDisplayModule() {
        return (EnchantDisplay) super.getDisplayModule();
    }

    @Override
    public String getMinimumEcoVersion() {
        return "6.10.0";
    }

    /**
     * Get the instance of EcoEnchants.
     * <p>
     * Bad practice to use this.
     *
     * @return The instance.
     */
    public static EcoEnchantsPlugin getInstance() {
        return instance;
    }
}
