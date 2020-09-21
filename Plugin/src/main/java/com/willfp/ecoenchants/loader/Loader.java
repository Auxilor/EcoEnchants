package com.willfp.ecoenchants.loader;

import com.comphenix.protocol.ProtocolLibrary;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.anvil.AnvilListeners;
import com.willfp.ecoenchants.bstats.Metrics;
import com.willfp.ecoenchants.command.commands.CommandEcodebug;
import com.willfp.ecoenchants.command.commands.CommandEcoreload;
import com.willfp.ecoenchants.command.commands.CommandEcoskip;
import com.willfp.ecoenchants.command.commands.CommandEnchantinfo;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.display.packets.PacketOpenWindowMerchant;
import com.willfp.ecoenchants.display.packets.PacketSetCreativeSlot;
import com.willfp.ecoenchants.display.packets.PacketSetSlot;
import com.willfp.ecoenchants.display.packets.PacketWindowItems;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.EcoRunnable;
import com.willfp.ecoenchants.enchantments.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.EnchantmentTarget;
import com.willfp.ecoenchants.events.armorequip.ArmorListener;
import com.willfp.ecoenchants.events.armorequip.DispenserArmorListener;
import com.willfp.ecoenchants.events.entitydeathbyentity.EntityDeathByEntityListeners;
import com.willfp.ecoenchants.events.naturalexpgainevent.NaturalExpGainListeners;
import com.willfp.ecoenchants.extensions.ExtensionManager;
import com.willfp.ecoenchants.grindstone.GrindstoneListeners;
import com.willfp.ecoenchants.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.integrations.anticheat.plugins.AnticheatAAC;
import com.willfp.ecoenchants.integrations.anticheat.plugins.AnticheatMatrix;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefFactionsUUID;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefGriefPrevention;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefLands;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefTowny;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefWorldGuard;
import com.willfp.ecoenchants.integrations.essentials.EssentialsManager;
import com.willfp.ecoenchants.integrations.essentials.plugins.IntegrationEssentials;
import com.willfp.ecoenchants.listeners.ArrowListeners;
import com.willfp.ecoenchants.listeners.EnchantingListeners;
import com.willfp.ecoenchants.listeners.PlayerJoinListener;
import com.willfp.ecoenchants.listeners.VillagerListeners;
import com.willfp.ecoenchants.naturalloot.LootPopulator;
import com.willfp.ecoenchants.nms.BlockBreak;
import com.willfp.ecoenchants.nms.Cooldown;
import com.willfp.ecoenchants.nms.TridentStack;
import com.willfp.ecoenchants.util.UpdateChecker;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.generator.BlockPopulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing methods for the loading and unloading of EcoEnchants
 */
public class Loader {

    private static final EcoEnchantsPlugin PLUGIN = EcoEnchantsPlugin.getInstance();

    /**
     * Called by {@link EcoEnchantsPlugin#onEnable()}
     */
    public static void load() {
        PLUGIN.getLogger().info("==========================================");
        PLUGIN.getLogger().info("");
        PLUGIN.getLogger().info("Loading §aEcoEnchants");
        PLUGIN.getLogger().info("Made by §aAuxilor§f - willfp.com");
        PLUGIN.getLogger().info("");
        PLUGIN.getLogger().info("==========================================");

        /*
        Check for paper
         */

        boolean isPapermc = false;
        try {
            isPapermc = Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData") != null;
        } catch (ClassNotFoundException ignored) {}

        if (!isPapermc) {
            Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
                PLUGIN.getLogger().info("");
                PLUGIN.getLogger().info("----------------------------");
                PLUGIN.getLogger().info("");
                PLUGIN.getLogger().severe("You don't seem to be running paper!");
                PLUGIN.getLogger().severe("Paper is strongly recommended for all servers,");
                PLUGIN.getLogger().severe("and enchantments like Drill may not function properly without it");
                PLUGIN.getLogger().severe("Download Paper from §fhttps://papermc.io");
                PLUGIN.getLogger().info("");
                PLUGIN.getLogger().info("----------------------------");
                PLUGIN.getLogger().info("");
            }, 1);
        }

        /*
        Load Configs
         */

        PLUGIN.getLogger().info("Loading Configs...");
        ConfigManager.updateConfigs();
        PLUGIN.getLogger().info("");

        /*
        Load ProtocolLib
         */

        PLUGIN.getLogger().info("Loading ProtocolLib...");
        EcoEnchantsPlugin.getInstance().protocolManager = ProtocolLibrary.getProtocolManager();
        PacketOpenWindowMerchant.getInstance().register();
        PacketSetCreativeSlot.getInstance().register();
        PacketSetSlot.getInstance().register();
        PacketWindowItems.getInstance().register();

        /*
        Load land management support
         */

        PLUGIN.getLogger().info("Scheduling Integration Loading...");

        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {

            PLUGIN.getLogger().info("Loading Integrations...");

            if(Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
                AntigriefManager.registerAntigrief(new AntigriefWorldGuard());
                PLUGIN.getLogger().info("WorldGuard: §aENABLED");
            } else {
                PLUGIN.getLogger().info("WorldGuard: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
                AntigriefManager.registerAntigrief(new AntigriefGriefPrevention());
                PLUGIN.getLogger().info("GriefPrevention: §aENABLED");
            } else {
                PLUGIN.getLogger().info("GriefPrevention: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("FactionsUUID")) {
                AntigriefManager.registerAntigrief(new AntigriefFactionsUUID());
                PLUGIN.getLogger().info("FactionsUUID: §aENABLED");
            } else {
                PLUGIN.getLogger().info("FactionsUUID: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("Towny")) {
                AntigriefManager.registerAntigrief(new AntigriefTowny());
                PLUGIN.getLogger().info("Towny: §aENABLED");
            } else {
                PLUGIN.getLogger().info("Towny: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("Lands")) {
                AntigriefManager.registerAntigrief(new AntigriefLands());
                PLUGIN.getLogger().info("Lands: §aENABLED");
            } else {
                PLUGIN.getLogger().info("Lands: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
                EssentialsManager.registerAntigrief(new IntegrationEssentials());
                PLUGIN.getLogger().info("Essentials: §aENABLED");
            } else {
                PLUGIN.getLogger().info("Essentials: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("AAC")) {
                AnticheatManager.registerAnticheat(new AnticheatAAC());
                PLUGIN.getLogger().info("AAC: §aENABLED");
            } else {
                PLUGIN.getLogger().info("AAC: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("Matrix")) {
                AnticheatManager.registerAnticheat(new AnticheatMatrix());
                PLUGIN.getLogger().info("Matrix: §aENABLED");
            } else {
                PLUGIN.getLogger().info("Matrix: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("NoCheatPlus")) {
                AnticheatManager.registerAnticheat(new AnticheatAAC());
                PLUGIN.getLogger().info("NCP: §aENABLED");
            } else {
                PLUGIN.getLogger().info("NCP: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("Spartan")) {
                AnticheatManager.registerAnticheat(new AnticheatAAC());
                PLUGIN.getLogger().info("Spartan: §aENABLED");
            } else {
                PLUGIN.getLogger().info("Spartan: §9DISABLED");
            }

            PLUGIN.getLogger().info("");

        }, 1);

        PLUGIN.getLogger().info("");

        /*
        Load NMS
         */

        PLUGIN.getLogger().info("Loading NMS APIs...");

        if(Cooldown.init()) {
            PLUGIN.getLogger().info("Cooldown: §aSUCCESS");
        } else {
            PLUGIN.getLogger().info("Cooldown: §cFAILURE");
            PLUGIN.getLogger().severe("§cAborting...");
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }

        if(TridentStack.init()) {
            PLUGIN.getLogger().info("Trident API: §aSUCCESS");
        } else {
            PLUGIN.getLogger().info("Trident API: §cFAILURE");
            PLUGIN.getLogger().severe("§cAborting...");
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }

        if(BlockBreak.init()) {
            PLUGIN.getLogger().info("Block Break: §aSUCCESS");
        } else {
            PLUGIN.getLogger().info("Block Break: §cFAILURE");
            PLUGIN.getLogger().severe("§cAborting...");
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }
        
        PLUGIN.getLogger().info("");

        /*
        Register Events
         */

        PLUGIN.getLogger().info("Registering Events...");
        Bukkit.getPluginManager().registerEvents(new ArmorListener(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new DispenserArmorListener(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new EnchantingListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new GrindstoneListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new AnvilListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityDeathByEntityListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new NaturalExpGainListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new VillagerListeners(), EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new ArrowListeners(), EcoEnchantsPlugin.getInstance());
        PLUGIN.getLogger().info("");

        /*
        Add Block Populators
         */

        PLUGIN.getLogger().info("Scheduling Adding Block Populators...");
        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
            Bukkit.getServer().getWorlds().forEach((world -> {
                world.getPopulators().add(new LootPopulator());
            }));
        }, 1);
        PLUGIN.getLogger().info("");

        /*
        Load Extensions
         */

        PLUGIN.getLogger().info("Loading Extensions...");

        ExtensionManager.loadExtensions();
        if(ExtensionManager.getLoadedExtensions().isEmpty()) {
            PLUGIN.getLogger().info("§cNo extensions found");
        } else {
            PLUGIN.getLogger().info("Extensions Loaded:");
            ExtensionManager.getLoadedExtensions().forEach((extension, name) -> {
                PLUGIN.getLogger().info("- " + name);
            });
        }
        PLUGIN.getLogger().info("");

        /*
        Create enchantment config files (for first time use)
         */

        PLUGIN.getLogger().info("Creating Enchantment Configs...");
        ConfigManager.updateEnchantmentConfigs();
        PLUGIN.getLogger().info("");

        /*
        Load all enchantments, rarities, and targets
         */

        PLUGIN.getLogger().info("Adding Enchantments to API...");
        EnchantmentRarity.update();
        EnchantmentTarget.update();
        if(EnchantmentRarity.getAll().size() == 0 || EnchantmentTarget.getAll().size() == 0) {
            PLUGIN.getLogger().severe("§cError loading rarities or targets! Aborting...");
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
            return;
        } else {
            PLUGIN.getLogger().info(EnchantmentRarity.getAll().size() + " Rarities Loaded:");
            EnchantmentRarity.getAll().forEach((rarity -> {
                PLUGIN.getLogger().info("- " + rarity.getName() + ": Table Probability=" + rarity.getProbability() + ", Minimum Level=" + rarity.getMinimumLevel() + ", Villager Probability=" + rarity.getVillagerProbability() + ", Loot Probability=" + rarity.getLootProbability() + ", Has Custom Color=" + rarity.hasCustomColor());
            }));

            PLUGIN.getLogger().info("");

            PLUGIN.getLogger().info(EnchantmentTarget.getAll().size() + " Targets Loaded:");
            EnchantmentTarget.getAll().forEach((target) -> {
                PLUGIN.getLogger().info("- " + target.getName() + ": Materials=" + target.getMaterials().toString());
            });
        }
        PLUGIN.getLogger().info("");

        if (EcoEnchants.getAll().size() == 0) {
            PLUGIN.getLogger().severe("§cError adding enchantments! Aborting...");
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
            return;
        } else {
            PLUGIN.getLogger().info(EcoEnchants.getAll().size() + " Enchantments Loaded:");
            EcoEnchants.getAll().forEach((ecoEnchant -> {
                if(ecoEnchant.getType().equals(EcoEnchant.EnchantmentType.SPECIAL)) {
                    PLUGIN.getLogger().info(ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("special-color")) + "- " + ecoEnchant.getName() + ": " + ecoEnchant.getKey().toString());
                } else if(ecoEnchant.getType().equals(EcoEnchant.EnchantmentType.ARTIFACT)) {
                    PLUGIN.getLogger().info(ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("artifact-color")) + "- " + ecoEnchant.getName() + ": " + ecoEnchant.getKey().toString());
                } else {
                    PLUGIN.getLogger().info("- " + ecoEnchant.getName() + ": " + ecoEnchant.getKey().toString());
                }
            }));
        }
        PLUGIN.getLogger().info("");

        /*
        Load enchantment configs
         */

        PLUGIN.getLogger().info("Loading Enchantment Configs...");
        ConfigManager.updateEnchantmentConfigs();
        PLUGIN.getLogger().info("");

        /*
        Plugin Conflicts
         */

        PLUGIN.getLogger().info("Checking Plugin Conflicts...");

        if(Bukkit.getPluginManager().getPlugin("EnchantmentNumbers") != null) {
            Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("EnchantmentNumbers"));
            PLUGIN.getLogger().severe("EnchantmentNumbers conflicts with EcoEnchants");
            PLUGIN.getLogger().severe("It has been disabled. Please uninstall it!");
        }
        PLUGIN.getLogger().info("");

        /*
        Register Enchantments
         */

        PLUGIN.getLogger().info("Registering Enchantments...");
        EcoEnchants.update();
        EnchantDisplay.update();
        PLUGIN.getLogger().info("");

        /*
        Register Enchantment Listeners
         */

        PLUGIN.getLogger().info("Registering Enchantment Listeners...");
        EcoEnchants.getAll().forEach((ecoEnchant -> {
            if(ecoEnchant.isEnabled()) {
                Bukkit.getPluginManager().registerEvents(ecoEnchant, EcoEnchantsPlugin.getInstance());
            }
        }));
        PLUGIN.getLogger().info("");

        /*
        Register Enchantment Tasks
         */

        PLUGIN.getLogger().info("Registering Enchantment Tasks...");
        EcoEnchants.getAll().forEach((ecoEnchant -> {
            if(ecoEnchant instanceof EcoRunnable) {
                Bukkit.getScheduler().scheduleSyncRepeatingTask(EcoEnchantsPlugin.getInstance(), (Runnable) ecoEnchant, 5, ((EcoRunnable) ecoEnchant).getTime());
            }
        }));
        PLUGIN.getLogger().info("");


        /*
        Load Commands
         */

        PLUGIN.getLogger().info("Loading Commands...");
        Bukkit.getPluginCommand("ecoreload").setExecutor(CommandEcoreload.getInstance());
        Bukkit.getPluginCommand("ecodebug").setExecutor(CommandEcodebug.getInstance());
        Bukkit.getPluginCommand("enchantinfo").setExecutor(CommandEnchantinfo.getInstance());
        Bukkit.getPluginCommand("ecoskip").setExecutor(CommandEcoskip.getInstance());
        PLUGIN.getLogger().info("");
        
        /*
        Start bStats
         */

        PLUGIN.getLogger().info("Hooking into bStats...");
        new Metrics(EcoEnchantsPlugin.getInstance(), 7666);
        PLUGIN.getLogger().info("");

        /*
        Start update checker
         */


        new UpdateChecker(EcoEnchantsPlugin.getInstance(), 79573).getVersion((version) -> {
            DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(EcoEnchantsPlugin.getInstance().getDescription().getVersion());
            DefaultArtifactVersion mostRecentVersion = new DefaultArtifactVersion(version);
            PLUGIN.getLogger().info("----------------------------");
            PLUGIN.getLogger().info("");
            PLUGIN.getLogger().info("EcoEnchants Updater");
            PLUGIN.getLogger().info("");
            if (currentVersion.compareTo(mostRecentVersion) > 0 || currentVersion.equals(mostRecentVersion)) {
                PLUGIN.getLogger().info("§aEcoEnchants is up to date! (Version " + EcoEnchantsPlugin.getInstance().getDescription().getVersion() + ")");
            } else {
                EcoEnchantsPlugin.outdated = true;
                EcoEnchantsPlugin.newVersion = version;

                Bukkit.getScheduler().runTaskTimer(EcoEnchantsPlugin.getInstance(), () -> {
                    PLUGIN.getLogger().info("§6EcoEnchants is out of date! (Version " + EcoEnchantsPlugin.getInstance().getDescription().getVersion() + ")");
                    PLUGIN.getLogger().info("§6The newest version is §f" + version);
                    PLUGIN.getLogger().info("§6Download the new version here: §fhttps://www.spigotmc.org/resources/ecoenchants.79573/");
                }, 0, 36000);
            }
            PLUGIN.getLogger().info("");
            PLUGIN.getLogger().info("----------------------------");
        });

        /*
        Finish
         */

        PLUGIN.getLogger().info("Loaded §aEcoEnchants!");
    }

    /**
     * Called by {@link EcoEnchantsPlugin#onDisable()}
     */
    public static void unload() {
        PLUGIN.getLogger().info("§cDisabling EcoEnchants...");
        PLUGIN.getLogger().info("Removing Block Populators...");
        Bukkit.getServer().getWorlds().forEach((world -> {
            List<BlockPopulator> populators = new ArrayList<>(world.getPopulators());
            populators.forEach((blockPopulator -> {
                if(blockPopulator instanceof LootPopulator) {
                    world.getPopulators().remove(blockPopulator);
                }
            }));
        }));
        PLUGIN.getLogger().info("");
        PLUGIN.getLogger().info("§cUnloading Extensions...");
        ExtensionManager.unloadExtensions();
        PLUGIN.getLogger().info("§fBye! :)");
    }
}
