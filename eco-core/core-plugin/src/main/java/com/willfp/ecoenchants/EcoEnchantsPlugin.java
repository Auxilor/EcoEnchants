package com.willfp.ecoenchants;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.integrations.IntegrationLoader;
import com.willfp.eco.util.TelekinesisUtils;
import com.willfp.ecoenchants.command.CommandEcoEnchants;
import com.willfp.ecoenchants.command.CommandEnchantinfo;
import com.willfp.ecoenchants.config.RarityYml;
import com.willfp.ecoenchants.config.TargetYml;
import com.willfp.ecoenchants.config.VanillaEnchantsYml;
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
import com.willfp.ecoenchants.integrations.essentials.EssentialsManager;
import com.willfp.ecoenchants.integrations.essentials.plugins.IntegrationEssentials;
import com.willfp.ecoenchants.proxy.proxies.FastGetEnchantsProxy;
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
        super(79573, 7666, "com.willfp.ecoenchants.proxy", "&a", true);
        instance = this;

        rarityYml = new RarityYml(this);
        targetYml = new TargetYml(this);
        vanillaEnchantsYml = new VanillaEnchantsYml(this);
    }

    @Override
    protected void handleEnable() {
        this.getLogger().info(EcoEnchants.values().size() + " Enchantments Loaded");

        TelekinesisUtils.registerTest(player -> this.getProxy(FastGetEnchantsProxy.class).getLevelOnItem(player.getInventory().getItemInMainHand(), EcoEnchants.TELEKINESIS) > 0);
    }

    @Override
    protected void handleDisable() {
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
    }

    @Override
    protected void handleAfterLoad() {
        if (this.getConfigYml().getBool("loot.enabled")) {
            for (World world : Bukkit.getServer().getWorlds()) {
                world.getPopulators().removeIf(blockPopulator -> blockPopulator instanceof LootPopulator);
                world.getPopulators().add(new LootPopulator(this));
            }
        }
        EssentialsManager.registerEnchantments();
    }

    @Override
    protected List<IntegrationLoader> loadIntegrationLoaders() {
        return Arrays.asList(
                new IntegrationLoader("Essentials", () -> EssentialsManager.register(new IntegrationEssentials()))
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
}
