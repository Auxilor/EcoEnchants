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

    /**
     * Called by {@link EcoEnchantsPlugin#onEnable()}
     */
    public static void load() {
        Bukkit.getLogger().info("==========================================");
        Bukkit.getLogger().info("");
        Bukkit.getLogger().info("Loading §aEcoEnchants");
        Bukkit.getLogger().info("Made by §aAuxilor§f - willfp.com");
        Bukkit.getLogger().info("");
        Bukkit.getLogger().info("==========================================");

        /*
        Check for paper
         */

        boolean isPapermc = false;
        try {
            isPapermc = Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData") != null;
        } catch (ClassNotFoundException ignored) {}

        if (!isPapermc) {
            Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
                Bukkit.getLogger().info("");
                Bukkit.getLogger().info("----------------------------");
                Bukkit.getLogger().info("");
                Bukkit.getLogger().severe("You don't seem to be running paper!");
                Bukkit.getLogger().severe("Paper is strongly recommended for all servers,");
                Bukkit.getLogger().severe("and enchantments like Drill may not function properly without it");
                Bukkit.getLogger().severe("Download Paper from §fhttps://papermc.io");
                Bukkit.getLogger().info("");
                Bukkit.getLogger().info("----------------------------");
                Bukkit.getLogger().info("");
            }, 1);
        }

        /*
        Load Configs
         */

        Bukkit.getLogger().info("Loading Configs...");
        ConfigManager.updateConfigs();
        Bukkit.getLogger().info("");

        /*
        Load ProtocolLib
         */

        Bukkit.getLogger().info("Loading ProtocolLib...");
        EcoEnchantsPlugin.getInstance().protocolManager = ProtocolLibrary.getProtocolManager();
        PacketOpenWindowMerchant.getInstance().register();
        PacketSetCreativeSlot.getInstance().register();
        PacketSetSlot.getInstance().register();
        PacketWindowItems.getInstance().register();

        /*
        Load land management support
         */

        Bukkit.getLogger().info("Scheduling Integration Loading...");

        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {

            Bukkit.getLogger().info("Loading Integrations...");

            if(Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
                AntigriefManager.registerAntigrief(new AntigriefWorldGuard());
                Bukkit.getLogger().info("WorldGuard: §aENABLED");
            } else {
                Bukkit.getLogger().info("WorldGuard: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
                AntigriefManager.registerAntigrief(new AntigriefGriefPrevention());
                Bukkit.getLogger().info("GriefPrevention: §aENABLED");
            } else {
                Bukkit.getLogger().info("GriefPrevention: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("FactionsUUID")) {
                AntigriefManager.registerAntigrief(new AntigriefFactionsUUID());
                Bukkit.getLogger().info("FactionsUUID: §aENABLED");
            } else {
                Bukkit.getLogger().info("FactionsUUID: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("Towny")) {
                AntigriefManager.registerAntigrief(new AntigriefTowny());
                Bukkit.getLogger().info("Towny: §aENABLED");
            } else {
                Bukkit.getLogger().info("Towny: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("Lands")) {
                AntigriefManager.registerAntigrief(new AntigriefLands());
                Bukkit.getLogger().info("Lands: §aENABLED");
            } else {
                Bukkit.getLogger().info("Lands: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
                EssentialsManager.registerAntigrief(new IntegrationEssentials());
                Bukkit.getLogger().info("Essentials: §aENABLED");
            } else {
                Bukkit.getLogger().info("Essentials: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("AAC")) {
                AnticheatManager.registerAnticheat(new AnticheatAAC());
                Bukkit.getLogger().info("AAC: §aENABLED");
            } else {
                Bukkit.getLogger().info("AAC: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("Matrix")) {
                AnticheatManager.registerAnticheat(new AnticheatMatrix());
                Bukkit.getLogger().info("Matrix: §aENABLED");
            } else {
                Bukkit.getLogger().info("Matrix: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("NoCheatPlus")) {
                AnticheatManager.registerAnticheat(new AnticheatAAC());
                Bukkit.getLogger().info("NCP: §aENABLED");
            } else {
                Bukkit.getLogger().info("NCP: §9DISABLED");
            }

            if(Bukkit.getPluginManager().isPluginEnabled("Spartan")) {
                AnticheatManager.registerAnticheat(new AnticheatAAC());
                Bukkit.getLogger().info("Spartan: §aENABLED");
            } else {
                Bukkit.getLogger().info("Spartan: §9DISABLED");
            }

            Bukkit.getLogger().info("");

        }, 1);

        Bukkit.getLogger().info("");

        /*
        Load NMS
         */

        Bukkit.getLogger().info("Loading NMS APIs...");

        if(Cooldown.init()) {
            Bukkit.getLogger().info("Cooldown: §aSUCCESS");
        } else {
            Bukkit.getLogger().info("Cooldown: §cFAILURE");
            Bukkit.getLogger().severe("§cAborting...");
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }

        if(TridentStack.init()) {
            Bukkit.getLogger().info("Trident API: §aSUCCESS");
        } else {
            Bukkit.getLogger().info("Trident API: §cFAILURE");
            Bukkit.getLogger().severe("§cAborting...");
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }

        if(BlockBreak.init()) {
            Bukkit.getLogger().info("Block Break: §aSUCCESS");
        } else {
            Bukkit.getLogger().info("Block Break: §cFAILURE");
            Bukkit.getLogger().severe("§cAborting...");
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }
        
        Bukkit.getLogger().info("");

        /*
        Register Events
         */

        Bukkit.getLogger().info("Registering Events...");
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
        Bukkit.getLogger().info("");

        /*
        Add Block Populators
         */

        Bukkit.getLogger().info("Scheduling Adding Block Populators...");
        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
            Bukkit.getServer().getWorlds().forEach((world -> {
                world.getPopulators().add(new LootPopulator());
            }));
        }, 1);
        Bukkit.getLogger().info("");

        /*
        Load Extensions
         */

        Bukkit.getLogger().info("Loading Extensions...");

        ExtensionManager.loadExtensions();
        if(ExtensionManager.getLoadedExtensions().isEmpty()) {
            Bukkit.getLogger().info("§cNo extensions found");
        } else {
            Bukkit.getLogger().info("Extensions Loaded:");
            ExtensionManager.getLoadedExtensions().forEach((extension, name) -> {
                Bukkit.getLogger().info("- " + name);
            });
        }
        Bukkit.getLogger().info("");

        /*
        Create enchantment config files (for first time use)
         */

        Bukkit.getLogger().info("Creating Enchantment Configs...");
        ConfigManager.updateEnchantmentConfigs();
        Bukkit.getLogger().info("");

        /*
        Load all enchantments, rarities, and targets
         */

        Bukkit.getLogger().info("Adding Enchantments to API...");
        EnchantmentRarity.update();
        EnchantmentTarget.update();
        if(EnchantmentRarity.getAll().size() == 0 || EnchantmentTarget.getAll().size() == 0) {
            Bukkit.getLogger().severe("§cError loading rarities or targets! Aborting...");
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
            return;
        } else {
            Bukkit.getLogger().info(EnchantmentRarity.getAll().size() + " Rarities Loaded:");
            EnchantmentRarity.getAll().forEach((rarity -> {
                Bukkit.getLogger().info("- " + rarity.getName() + ": Table Probability=" + rarity.getProbability() + ", Minimum Level=" + rarity.getMinimumLevel() + ", Villager Probability=" + rarity.getVillagerProbability() + ", Loot Probability=" + rarity.getLootProbability() + ", Has Custom Color=" + rarity.hasCustomColor());
            }));

            Bukkit.getLogger().info("");

            Bukkit.getLogger().info(EnchantmentTarget.getAll().size() + " Targets Loaded:");
            EnchantmentTarget.getAll().forEach((target) -> {
                Bukkit.getLogger().info("- " + target.getName() + ": Materials=" + target.getMaterials().toString());
            });
        }
        Bukkit.getLogger().info("");

        if (EcoEnchants.getAll().size() == 0) {
            Bukkit.getLogger().severe("§cError adding enchantments! Aborting...");
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
            return;
        } else {
            Bukkit.getLogger().info(EcoEnchants.getAll().size() + " Enchantments Loaded:");
            EcoEnchants.getAll().forEach((ecoEnchant -> {
                if(ecoEnchant.getType().equals(EcoEnchant.EnchantmentType.SPECIAL)) {
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("special-color")) + "- " + ecoEnchant.getName() + ": " + ecoEnchant.getKey().toString());
                } else if(ecoEnchant.getType().equals(EcoEnchant.EnchantmentType.ARTIFACT)) {
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("artifact-color")) + "- " + ecoEnchant.getName() + ": " + ecoEnchant.getKey().toString());
                } else {
                    Bukkit.getLogger().info("- " + ecoEnchant.getName() + ": " + ecoEnchant.getKey().toString());
                }
            }));
        }
        Bukkit.getLogger().info("");

        /*
        Load enchantment configs
         */

        Bukkit.getLogger().info("Loading Enchantment Configs...");
        ConfigManager.updateEnchantmentConfigs();
        Bukkit.getLogger().info("");

        /*
        Plugin Conflicts
         */

        Bukkit.getLogger().info("Checking Plugin Conflicts...");

        if(Bukkit.getPluginManager().getPlugin("EnchantmentNumbers") != null) {
            Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("EnchantmentNumbers"));
            Bukkit.getLogger().severe("EnchantmentNumbers conflicts with EcoEnchants");
            Bukkit.getLogger().severe("It has been disabled. Please uninstall it!");
        }
        Bukkit.getLogger().info("");

        /*
        Register Enchantments
         */

        Bukkit.getLogger().info("Registering Enchantments...");
        EcoEnchants.update();
        EnchantDisplay.update();
        Bukkit.getLogger().info("");

        /*
        Register Enchantment Listeners
         */

        Bukkit.getLogger().info("Registering Enchantment Listeners...");
        EcoEnchants.getAll().forEach((ecoEnchant -> {
            if(ecoEnchant.isEnabled()) {
                Bukkit.getPluginManager().registerEvents(ecoEnchant, EcoEnchantsPlugin.getInstance());
            }
        }));
        Bukkit.getLogger().info("");

        /*
        Register Enchantment Tasks
         */

        Bukkit.getLogger().info("Registering Enchantment Tasks...");
        EcoEnchants.getAll().forEach((ecoEnchant -> {
            if(ecoEnchant instanceof EcoRunnable) {
                Bukkit.getScheduler().scheduleSyncRepeatingTask(EcoEnchantsPlugin.getInstance(), (Runnable) ecoEnchant, 5, ((EcoRunnable) ecoEnchant).getTime());
            }
        }));
        Bukkit.getLogger().info("");


        /*
        Load Commands
         */

        Bukkit.getLogger().info("Loading Commands...");
        Bukkit.getPluginCommand("ecoreload").setExecutor(CommandEcoreload.getInstance());
        Bukkit.getPluginCommand("ecodebug").setExecutor(CommandEcodebug.getInstance());
        Bukkit.getPluginCommand("enchantinfo").setExecutor(CommandEnchantinfo.getInstance());
        Bukkit.getPluginCommand("ecoskip").setExecutor(CommandEcoskip.getInstance());
        Bukkit.getLogger().info("");
        
        /*
        Start bStats
         */

        Bukkit.getLogger().info("Hooking into bStats...");
        new Metrics(EcoEnchantsPlugin.getInstance(), 7666);
        Bukkit.getLogger().info("");

        /*
        Start update checker
         */


        new UpdateChecker(EcoEnchantsPlugin.getInstance(), 79573).getVersion((version) -> {
            DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(EcoEnchantsPlugin.getInstance().getDescription().getVersion());
            DefaultArtifactVersion mostRecentVersion = new DefaultArtifactVersion(version);
            Bukkit.getLogger().info("----------------------------");
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("EcoEnchants Updater");
            Bukkit.getLogger().info("");
            if (currentVersion.compareTo(mostRecentVersion) > 0 || currentVersion.equals(mostRecentVersion)) {
                Bukkit.getLogger().info("§aEcoEnchants is up to date! (Version " + EcoEnchantsPlugin.getInstance().getDescription().getVersion() + ")");
            } else {
                EcoEnchantsPlugin.outdated = true;
                EcoEnchantsPlugin.newVersion = version;

                Bukkit.getScheduler().runTaskTimer(EcoEnchantsPlugin.getInstance(), () -> {
                    Bukkit.getLogger().info("§6EcoEnchants is out of date! (Version " + EcoEnchantsPlugin.getInstance().getDescription().getVersion() + ")");
                    Bukkit.getLogger().info("§6The newest version is §f" + version);
                    Bukkit.getLogger().info("§6Download the new version here: §fhttps://www.spigotmc.org/resources/ecoenchants.79573/");
                }, 0, 36000);
            }
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("----------------------------");
        });

        /*
        Finish
         */

        Bukkit.getLogger().info("Loaded §aEcoEnchants!");
    }

    /**
     * Called by {@link EcoEnchantsPlugin#onDisable()}
     */
    public static void unload() {
        Bukkit.getLogger().info("§cDisabling EcoEnchants...");
        Bukkit.getLogger().info("Removing Block Populators...");
        Bukkit.getServer().getWorlds().forEach((world -> {
            List<BlockPopulator> populators = new ArrayList<>(world.getPopulators());
            populators.forEach((blockPopulator -> {
                if(blockPopulator instanceof LootPopulator) {
                    world.getPopulators().remove(blockPopulator);
                }
            }));
        }));
        Bukkit.getLogger().info("");
        Bukkit.getLogger().info("§cUnloading Extensions...");
        ExtensionManager.unloadExtensions();
        Bukkit.getLogger().info("§fBye! :)");
    }
}
