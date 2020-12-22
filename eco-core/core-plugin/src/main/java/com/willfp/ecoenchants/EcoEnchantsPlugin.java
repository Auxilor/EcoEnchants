package com.willfp.ecoenchants;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.integrations.IntegrationLoader;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.command.commands.CommandEcodebug;
import com.willfp.ecoenchants.command.commands.CommandEcoreload;
import com.willfp.ecoenchants.command.commands.CommandEnchantinfo;
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

import java.util.Arrays;
import java.util.List;

public class EcoEnchantsPlugin extends AbstractEcoPlugin {
    public EcoEnchantsPlugin() {
        super("EcoEnchants", 79573, 7666);
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void load() {

    }

    @Override
    public void reload() {

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
                new IntegrationLoader("Lands", () -> AntigriefManager.register(new AntigriefLands())),
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
}
