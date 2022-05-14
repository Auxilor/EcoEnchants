package com.willfp.ecoenchants;

import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.eco.core.integrations.IntegrationLoader;
import com.willfp.eco.util.TelekinesisUtils;
import com.willfp.ecoenchants.command.CommandEcoEnchants;
import com.willfp.ecoenchants.command.CommandEnchantinfo;
import com.willfp.ecoenchants.config.CustomEnchantsYml;
import com.willfp.ecoenchants.config.RarityYml;
import com.willfp.ecoenchants.config.TargetYml;
import com.willfp.ecoenchants.config.VanillaEnchantsYml;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.custom.CustomEcoEnchantRequirementListeners;
import com.willfp.ecoenchants.enchantments.custom.CustomEnchantEnableListeners;
import com.willfp.ecoenchants.enchantments.custom.CustomEnchantLookup;
import com.willfp.ecoenchants.enchantments.support.merging.anvil.AnvilListeners;
import com.willfp.ecoenchants.enchantments.support.merging.grindstone.GrindstoneListeners;
import com.willfp.ecoenchants.enchantments.support.obtaining.EnchantingListeners;
import com.willfp.ecoenchants.enchantments.support.obtaining.LootPopulator;
import com.willfp.ecoenchants.enchantments.support.obtaining.VillagerListeners;
import com.willfp.ecoenchants.enchantments.util.ItemConversions;
import com.willfp.ecoenchants.enchantments.util.TimedRunnable;
import com.willfp.ecoenchants.enchantments.util.WatcherTriggers;
import com.willfp.ecoenchants.integrations.mythicmobs.MythicMobsManager;
import com.willfp.ecoenchants.integrations.mythicmobs.plugins.IntegrationMythicMobs;
import com.willfp.ecoenchants.integrations.registration.RegistrationManager;
import com.willfp.ecoenchants.integrations.registration.plugins.IntegrationCMI;
import com.willfp.ecoenchants.integrations.registration.plugins.IntegrationEssentials;
import com.willfp.libreforge.LibReforgePlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class EcoEnchantsPlugin extends LibReforgePlugin {
    /**
     * Instance of the plugin.
     */
    private static EcoEnchantsPlugin instance;

    /**
     * Rarity.yml.
     */
    private final RarityYml rarityYml;

    /**
     * Target.yml.
     */
    private final TargetYml targetYml;

    /**
     * VanillaEnchants.yml.
     */
    private final VanillaEnchantsYml vanillaEnchantsYml;

    /**
     * CustomEnchants.yml.
     */
    private final CustomEnchantsYml customEnchantsYml;

    /**
     * Internal constructor called by bukkit on plugin load.
     */
    public EcoEnchantsPlugin() {
        instance = this;

        rarityYml = new RarityYml(this);
        targetYml = new TargetYml(this);
        vanillaEnchantsYml = new VanillaEnchantsYml(this);
        customEnchantsYml = new CustomEnchantsYml(this);

        this.registerJavaHolderProvider(player -> new ArrayList<>(CustomEnchantLookup.provideLevels(player)));
    }

    @Override
    public void handleEnableAdditional() {
        this.getLogger().info(EcoEnchants.values().size() + " Enchantments Loaded");

        TelekinesisUtils.registerTest(player -> FastItemStack.wrap(player.getInventory().getItemInMainHand()).getEnchantmentLevel(EcoEnchants.TELEKINESIS, false) > 0);
    }

    @Override
    public void handleDisableAdditional() {
        for (World world : Bukkit.getServer().getWorlds()) {
            world.getPopulators().removeIf(blockPopulator -> blockPopulator instanceof LootPopulator);
        }
    }

    @Override
    public void handleReloadAdditional() {
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
    }

    @Override
    protected void handleAfterLoad() {
        if (this.getConfigYml().getBool("loot.enabled")) {
            for (World world : Bukkit.getServer().getWorlds()) {
                world.getPopulators().removeIf(blockPopulator -> blockPopulator instanceof LootPopulator);
                world.getPopulators().add(new LootPopulator(this));
            }
        }
    }

    @Override
    @NotNull
    public List<IntegrationLoader> loadAdditionalIntegrations() {
        return Arrays.asList(
                new IntegrationLoader("Essentials", () -> RegistrationManager.register(new IntegrationEssentials())),
                new IntegrationLoader("CMI", () -> RegistrationManager.register(new IntegrationCMI())),
                new IntegrationLoader("MythicMobs", () -> MythicMobsManager.register(new IntegrationMythicMobs()))
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
                new ItemConversions(this),
                new CustomEnchantEnableListeners(this),
                new CustomEcoEnchantRequirementListeners(this)
        );
    }

    @Override
    @Nullable
    protected DisplayModule createDisplayModule() {
        return new EnchantDisplay(this);
    }

    @Override
    @NotNull
    public EnchantDisplay getDisplayModule() {
        return (EnchantDisplay) Objects.requireNonNull(super.getDisplayModule());
    }

    @Override
    public String getMinimumEcoVersion() {
        return "6.35.1";
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

    /**
     * Get rarity.yml.
     *
     * @return rarity.yml.
     */
    public RarityYml getRarityYml() {
        return this.rarityYml;
    }

    /**
     * Get target.yml.
     *
     * @return target.yml.
     */
    public TargetYml getTargetYml() {
        return this.targetYml;
    }

    /**
     * Get vanillaenchants.yml.
     *
     * @return vanillaenchants.yml.
     */
    public VanillaEnchantsYml getVanillaEnchantsYml() {
        return this.vanillaEnchantsYml;
    }

    /**
     * Get customenchants.yml
     *
     * @return customenchants.yml.
     */
    public CustomEnchantsYml getCustomEnchantsYml() {
        return customEnchantsYml;
    }
}
