package com.willfp.ecoenchants.command.commands;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.willfp.eco.core.proxy.ProxyConstants;
import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.drops.internal.DropManager;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class CommandEcodebug extends AbstractCommand {
    public CommandEcodebug(AbstractEcoPlugin plugin) {
        super(plugin, "ecodebug", "ecoenchants.ecodebug", false);
    }

    @Override
    public void onExecute(CommandSender sender, List<String> args) {
        this.getPlugin().getLog().info("--------------- BEGIN DEBUG ----------------");
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("Held Item: " + player.getInventory().getItemInMainHand().toString());
            this.getPlugin().getLog().info("");

            this.getPlugin().getLog().info("Held Item: " + player.getInventory().getItemInMainHand().toString());
            this.getPlugin().getLog().info("");
        }

        this.getPlugin().getLog().info("Running Version: " + this.getPlugin().getDescription().getVersion());
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Loaded Extensions: " + this.getPlugin().getExtensionLoader().getLoadedExtensions().stream().map(extension -> extension.getName() + " v" + extension.getVersion()).collect(Collectors.joining()));
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("EcoEnchants.getAll(): " + EcoEnchants.values().toString());
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Enchantment.values(): " + Arrays.toString(Enchantment.values()));
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Enchantment Cache: " + EnchantmentCache.getCache().toString());
        this.getPlugin().getLog().info("");

        try {
            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byNameField.setAccessible(true);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
            this.getPlugin().getLog().info("Enchantment.byName: " + byName.toString());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        this.getPlugin().getLog().info("");


        List<Enchantment> extern = Arrays.stream(Enchantment.values()).collect(Collectors.toList());
        extern.removeAll(EcoEnchants.values().stream().map(EcoEnchant::getEnchantment).collect(Collectors.toList()));
        this.getPlugin().getLog().info("External/Vanilla Enchantments: " + extern.toString());
        this.getPlugin().getLog().info("");

        List<Enchantment> uncached = Arrays.stream(Enchantment.values()).collect(Collectors.toList());
        uncached.removeAll(EnchantmentCache.getCache().stream().map(EnchantmentCache.CacheEntry::getEnchantment).collect(Collectors.toList()));
        this.getPlugin().getLog().info("Uncached Enchantments: " + uncached.toString());
        this.getPlugin().getLog().info("");

        List<Enchantment> brokenCache = Arrays.stream(Enchantment.values()).collect(Collectors.toList());
        brokenCache.removeIf(enchantment -> !(
                EnchantmentCache.getEntry(enchantment).getName().equalsIgnoreCase("null") ||
                        EnchantmentCache.getEntry(enchantment).getRawName().equalsIgnoreCase("null") ||
                        EnchantmentCache.getEntry(enchantment).getStringDescription().equalsIgnoreCase("null")));
        this.getPlugin().getLog().info("Enchantments with broken cache: " + brokenCache.toString());
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Installed Plugins: " + Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(Plugin::getName).collect(Collectors.toList()).toString());
        this.getPlugin().getLog().info("");

        Set<EcoEnchant> withIssues = new HashSet<>();
        EcoEnchants.values().forEach(enchant -> {
            if (enchant.getRarity() == null) withIssues.add(enchant);
            if (enchant.getRawTargets().isEmpty()) withIssues.add(enchant);
        });
        this.getPlugin().getLog().info("Enchantments with evident issues: " + withIssues.toString());
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Drop Type: " + DropManager.getType());
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Packets: " + ProtocolLibrary.getProtocolManager().getPacketListeners().stream().filter(packetListener -> packetListener.getSendingWhitelist().getPriority().equals(ListenerPriority.MONITOR)).collect(Collectors.toList()).toString());
        this.getPlugin().getLog().info("");

        this.getPlugin().getLog().info("Server Information: ");
        this.getPlugin().getLog().info("Players Online: " + Bukkit.getServer().getOnlinePlayers().size());
        this.getPlugin().getLog().info("Bukkit IP: " + Bukkit.getIp());
        this.getPlugin().getLog().info("Running Version: " + Bukkit.getVersion() + ", Bukkit Version: " + Bukkit.getBukkitVersion() + ", Alt Version: " + Bukkit.getServer().getVersion() + ", NMS: " + ProxyConstants.NMS_VERSION);
        this.getPlugin().getLog().info("Motd: " + Bukkit.getServer().getMotd());
        this.getPlugin().getLog().info("--------------- END DEBUG ----------------");
    }
}
