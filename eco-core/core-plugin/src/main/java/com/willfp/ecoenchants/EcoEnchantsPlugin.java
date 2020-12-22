package com.willfp.ecoenchants;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.integrations.IntegrationLoader;
import com.willfp.eco.util.interfaces.EcoRunnable;
import com.willfp.eco.util.packets.AbstractPacketAdapter;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.command.commands.CommandEcodebug;
import com.willfp.ecoenchants.command.commands.CommandEcoreload;
import com.willfp.ecoenchants.command.commands.CommandEnchantinfo;
import com.willfp.ecoenchants.command.tabcompleters.TabCompleterEnchantinfo;
import com.willfp.ecoenchants.config.EcoEnchantsConfigs;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.display.packets.PacketChat;
import com.willfp.ecoenchants.display.packets.PacketOpenWindowMerchant;
import com.willfp.ecoenchants.display.packets.PacketSetCreativeSlot;
import com.willfp.ecoenchants.display.packets.PacketSetSlot;
import com.willfp.ecoenchants.display.packets.PacketWindowItems;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.support.merging.anvil.AnvilListeners;
import com.willfp.ecoenchants.enchantments.support.merging.grindstone.GrindstoneListeners;
import com.willfp.ecoenchants.enchantments.support.obtaining.EnchantingListeners;
import com.willfp.ecoenchants.enchantments.support.obtaining.LootPopulator;
import com.willfp.ecoenchants.enchantments.support.obtaining.VillagerListeners;
import com.willfp.ecoenchants.enchantments.util.WatcherTriggers;
import com.willfp.ecoenchants.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.integrations.anticheat.plugins.AnticheatAAC;
import com.willfp.ecoenchants.integrations.anticheat.plugins.AnticheatMatrix;
import com.willfp.ecoenchants.integrations.anticheat.plugins.AnticheatNCP;
import com.willfp.ecoenchants.integrations.anticheat.plugins.AnticheatSpartan;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefFactionsUUID;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefGriefPrevention;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefKingdoms;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefLands;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefTowny;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefWorldGuard;
import com.willfp.ecoenchants.integrations.essentials.EssentialsManager;
import com.willfp.ecoenchants.integrations.essentials.plugins.IntegrationEssentials;
import com.willfp.ecoenchants.integrations.mcmmo.McmmoManager;
import com.willfp.ecoenchants.integrations.mcmmo.plugins.McmmoIntegrationImpl;
import com.willfp.ecoenchants.integrations.worldguard.WorldguardManager;
import com.willfp.ecoenchants.integrations.worldguard.plugins.WorldguardIntegrationImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.generator.BlockPopulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EcoEnchantsPlugin extends AbstractEcoPlugin {
    public EcoEnchantsPlugin() {
        super("EcoEnchants", 79573, 7666);
    }

    @Override
    public void enable() {
        this.getExtensionLoader().loadExtensions();

        if(this.getExtensionLoader().getLoadedExtensions().isEmpty()) {
            this.getLog().info("&cNo extensions found");
        } else {
            this.getLog().info("Extensions Loaded:");
            EcoEnchantsPlugin.getInstance().getExtensionLoader().getLoadedExtensions().forEach((extension) -> {
                this.getLog().info("- " + extension.getName() + " v" + extension.getVersion());
            });
        }

        this.getLog().info("");

        EcoEnchants.values().forEach(enchant -> {
            if(enchant.isEnabled()) {
                this.getEventManager().registerEvents(enchant);
                if(enchant instanceof EcoRunnable) {
                    this.getScheduler().syncRepeating((EcoRunnable) enchant, 5, ((EcoRunnable) enchant).getTime());
                }
            }
        });

        this.getLog().info(EcoEnchants.values().size() + " Enchantments Loaded:");
        this.getLog().info(EcoEnchants.values().stream().map(ecoEnchant -> ecoEnchant.getType().getColor() + ecoEnchant.getName()).collect(Collectors.joining(", ")));
    }

    @Override
    public void disable() {
        Bukkit.getServer().getWorlds().forEach((world -> {
            List<BlockPopulator> populators = new ArrayList<>(world.getPopulators());
            populators.forEach((blockPopulator -> {
                if (blockPopulator instanceof LootPopulator) {
                    world.getPopulators().remove(blockPopulator);
                }
            }));
        }));


        this.getExtensionLoader().unloadExtensions();
    }

    @Override
    public void load() {

    }

    @Override
    public void reload() {
        EcoEnchantsConfigs.updateConfigs();
        EnchantmentCache.update();
        EnchantmentRarity.update();
        EnchantmentTarget.update();
        EcoEnchants.update();
        EnchantDisplay.update();
        TabCompleterEnchantinfo.reload();
        EnchantmentType.update();

        EcoEnchants.values().forEach((ecoEnchant -> {
            HandlerList.unregisterAll(ecoEnchant);

            this.getScheduler().runLater(() -> {
                if(ecoEnchant.isEnabled()) this.getEventManager().registerEvents(ecoEnchant);
            }, 1);
        }));
    }

    @Override
    public void postLoad() {
        Bukkit.getServer().getWorlds().forEach(world -> {
            world.getPopulators().add(new LootPopulator());
        });
        EssentialsManager.registerEnchantments();
    }

    @Override
    public List<IntegrationLoader> getIntegrations() {
        return Arrays.asList(
                // AntiGrief
                new IntegrationLoader("WorldGuard", () -> {
                    AntigriefManager.register(new AntigriefWorldGuard());
                    WorldguardManager.register(new WorldguardIntegrationImpl());
                }),
                new IntegrationLoader("GriefPrevention", () -> AntigriefManager.register(new AntigriefGriefPrevention())),
                new IntegrationLoader("FactionsUUID", () -> AntigriefManager.register(new AntigriefFactionsUUID())),
                new IntegrationLoader("Towny", () -> AntigriefManager.register(new AntigriefTowny())),
                new IntegrationLoader("Lands", () -> AntigriefManager.register(new AntigriefLands(this))),
                new IntegrationLoader("Kingdoms", () -> AntigriefManager.register(new AntigriefKingdoms())),

                // AntiCheat
                new IntegrationLoader("AAC", () -> AnticheatManager.register(new AnticheatAAC())),
                new IntegrationLoader("Matrix", () -> AnticheatManager.register(new AnticheatMatrix())),
                new IntegrationLoader("NoCheatPlus", () -> AnticheatManager.register(new AnticheatNCP())),
                new IntegrationLoader("Spartan", () -> AnticheatManager.register(new AnticheatSpartan())),

                // MISC
                new IntegrationLoader("Essentials", () -> EssentialsManager.register(new IntegrationEssentials())),
                new IntegrationLoader("mcMMO", () -> McmmoManager.registerIntegration(new McmmoIntegrationImpl()))
        );
    }

    @Override
    public List<AbstractCommand> getCommands() {
        return Arrays.asList(
                new CommandEcodebug(this),
                new CommandEcoreload(this),
                new CommandEnchantinfo(this)
        );
    }

    @Override
    public List<AbstractPacketAdapter> getPacketAdapters() {
        return Arrays.asList(
                new PacketChat(this),
                new PacketOpenWindowMerchant(this),
                new PacketSetCreativeSlot(this),
                new PacketSetSlot(this),
                new PacketWindowItems(this)
        );
    }

    @Override
    public List<Listener> getListeners() {
        return Arrays.asList(
                new EnchantingListeners(this),
                new GrindstoneListeners(this),
                new AnvilListeners(this),
                new WatcherTriggers(this),
                new VillagerListeners()
        );
    }
}
