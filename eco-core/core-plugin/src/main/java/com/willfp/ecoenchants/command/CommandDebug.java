package com.willfp.ecoenchants.command;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.eco.core.proxy.ProxyConstants;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class CommandDebug extends Subcommand {
    /**
     * Instantiate a new /ecoenchants debug command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandDebug(@NotNull final EcoEnchantsPlugin plugin) {
        super(plugin, "debug", "ecoenchants.command.debug", false);
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
            if (!args.isEmpty() && args.get(0).equalsIgnoreCase("full")) {
                Bukkit.getLogger().info("--------------- BEGIN DEBUG ----------------");
                if (sender instanceof Player player) {
                    player.sendMessage("Held Item: " + player.getInventory().getItemInMainHand());
                    Bukkit.getLogger().info("");

                    Bukkit.getLogger().info("Held Item: " + player.getInventory().getItemInMainHand());
                    Bukkit.getLogger().info("");
                }

                Bukkit.getLogger().info("Running Version: " + this.getPlugin().getDescription().getVersion());
                Bukkit.getLogger().info("");

                Bukkit.getLogger().info("Loaded Extensions: " + this.getPlugin().getExtensionLoader().getLoadedExtensions().stream()
                        .map(extension -> extension.getName() + " v" + extension.getVersion())
                        .collect(Collectors.joining()));
                Bukkit.getLogger().info("");

                Bukkit.getLogger().info("EcoEnchants.getAll(): " + EcoEnchants.values().toString());
                Bukkit.getLogger().info("");

                Bukkit.getLogger().info("Enchantment.values(): " + Arrays.toString(Enchantment.values()));
                Bukkit.getLogger().info("");

                Bukkit.getLogger().info("Enchantment Cache: " + EnchantmentCache.getCache().toString());
                Bukkit.getLogger().info("");

                try {
                    Field byNameField = Enchantment.class.getDeclaredField("byName");
                    byNameField.setAccessible(true);
                    Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
                    Bukkit.getLogger().info("Enchantment.byName: " + byName.toString());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                Bukkit.getLogger().info("");


                List<Enchantment> extern = Arrays.stream(Enchantment.values()).collect(Collectors.toList());
                extern.removeAll(new ArrayList<>(EcoEnchants.values()));
                extern.removeIf(enchantment -> enchantment.getClass().toString().toLowerCase().contains("craftenchantment"));
                String external = extern.stream().map(enchantment -> "{" + enchantment.toString() + ", Provider: " + enchantment.getClass().toString() + "}").collect(Collectors.joining(", "));
                Bukkit.getLogger().info("External Enchantments: " + external);
                Bukkit.getLogger().info("");

                List<Enchantment> uncached = Arrays.stream(Enchantment.values()).collect(Collectors.toList());
                uncached.removeAll(EnchantmentCache.getCache().values().stream().map(EnchantmentCache.CacheEntry::getEnchantment).collect(Collectors.toList()));
                Bukkit.getLogger().info("Uncached Enchantments: " + uncached);
                Bukkit.getLogger().info("");

                List<Enchantment> brokenCache = Arrays.stream(Enchantment.values()).collect(Collectors.toList());
                brokenCache.removeIf(enchantment -> !(
                        EnchantmentCache.getEntry(enchantment).getName().equalsIgnoreCase("null")
                                || EnchantmentCache.getEntry(enchantment).getRawName().equalsIgnoreCase("null")
                                || EnchantmentCache.getEntry(enchantment).getStringDescription().equalsIgnoreCase("null")));
                Bukkit.getLogger().info("Enchantments with broken cache: " + brokenCache);
                Bukkit.getLogger().info("");

                Bukkit.getLogger().info("Installed Plugins: " + Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(Plugin::getName).collect(Collectors.toList()));
                Bukkit.getLogger().info("");

                Set<EcoEnchant> withIssues = new HashSet<>();
                EcoEnchants.values().forEach(enchant -> {
                    if (enchant.getRarity() == null) {
                        withIssues.add(enchant);
                    }
                    if (enchant.getTargets().isEmpty()) {
                        withIssues.add(enchant);
                    }
                });
                Bukkit.getLogger().info("Enchantments with evident issues: " + withIssues);
                Bukkit.getLogger().info("");

                Bukkit.getLogger().info("Packets: " + ProtocolLibrary.getProtocolManager().getPacketListeners().stream()
                        .filter(packetListener -> packetListener.getSendingWhitelist().getPriority().equals(ListenerPriority.MONITOR))
                        .collect(Collectors.toList()));
                Bukkit.getLogger().info("");

                Bukkit.getLogger().info("Server Information: ");
                Bukkit.getLogger().info("Players Online: " + Bukkit.getServer().getOnlinePlayers().size());
                Bukkit.getLogger().info("Bukkit IP: " + Bukkit.getIp());
                Bukkit.getLogger().info("Running Version: " + Bukkit.getVersion()
                        + ", Bukkit Version: " + Bukkit.getBukkitVersion()
                        + ", Alt Version: " + Bukkit.getServer().getVersion()
                        + ", NMS: " + ProxyConstants.NMS_VERSION);
                Bukkit.getLogger().info("Motd: " + Bukkit.getServer().getMotd());
                Bukkit.getLogger().info("--------------- END DEBUG ----------------");
            } else {
                if (sender instanceof Player player) {
                    player.sendMessage("Held Item: " + player.getInventory().getItemInMainHand());
                    player.sendMessage("Lore: ");
                    Bukkit.getLogger().info("");

                    Bukkit.getLogger().info("Held Item: " + player.getInventory().getItemInMainHand());
                    Bukkit.getLogger().info("Lore: ");
                    ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                    if (meta != null) {
                        for (String s : new ArrayList<>(meta.hasLore() ? meta.getLore() : new ArrayList<>())) {
                            Bukkit.getLogger().info(s);
                            player.sendMessage(s);
                        }
                    }
                    Bukkit.getLogger().info("");
                }
            }
        };
    }
}
