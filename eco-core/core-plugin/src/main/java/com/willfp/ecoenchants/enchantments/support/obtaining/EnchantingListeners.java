package com.willfp.ecoenchants.enchantments.support.obtaining;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class EnchantingListeners extends PluginDependent<EcoPlugin> implements Listener {
    /**
     * All players currently enchanting a secondary item.
     */
    private static final Map<Player, int[]> CURRENTLY_ENCHANTING_SECONDARY = new HashMap<>();

    /**
     * All enchantments that by default cannot be enchanted in a table but are in EcoEnchants.
     */
    private static final Set<Material> SECONDARY_ENCHANTABLE = new HashSet<>();

    /**
     * Instantiate enchanting listeners and link them to a specific plugin.
     *
     * @param plugin The plugin to link to.
     */
    public EnchantingListeners(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Update from config.
     *
     * @param plugin Instance of EcoEnchants.
     */
    @ConfigUpdater
    public static void update(@NotNull final EcoEnchantsPlugin plugin) {
        SECONDARY_ENCHANTABLE.clear();
        for (String string : plugin.getTargetYml().getStrings("extra-enchantable-items", false)) {
            SECONDARY_ENCHANTABLE.add(Material.matchMaterial(string.toUpperCase()));
        }
    }

    /**
     * Called on player leave.
     *
     * @param event The event to listen to.
     */
    @EventHandler
    public void onPlayerLeave(@NotNull final PlayerQuitEvent event) {
        CURRENTLY_ENCHANTING_SECONDARY.remove(event.getPlayer());
    }

    /**
     * Called on player enchant item.
     *
     * @param event The event to listen to.
     */
    @EventHandler
    public void enchantItem(@NotNull final EnchantItemEvent event) {
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        int cost = event.getExpLevelCost();

        Map<Enchantment, Integer> toAdd = event.getEnchantsToAdd();
        if (!this.getPlugin().getConfigYml().getBool("enchanting-table.enabled")) {
            this.getPlugin().getScheduler().runLater(() -> {
                ItemStack item0 = event.getInventory().getItem(0);
                event.getInventory().setItem(0, item0);
            }, 1);
            return;
        }

        if (SECONDARY_ENCHANTABLE.contains(event.getItem().getType())) {
            ItemStack lapis = event.getInventory().getItem(1);
            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                if (lapis == null) {
                    event.setCancelled(true);
                    return;
                }

                if (lapis.getAmount() < event.whichButton() + 1) {
                    event.setCancelled(true);
                    return;
                }

                lapis.setAmount(lapis.getAmount() - (event.whichButton() + 1));

                event.getInventory().setItem(1, lapis);
            }
        }

        double multiplier = 0.01;
        if (item.getType().equals(Material.BOOK) || item.getType().equals(Material.ENCHANTED_BOOK)) {
            multiplier /= this.getPlugin().getConfigYml().getInt("enchanting-table.book-times-less-likely");
        }

        if (this.getPlugin().getConfigYml().getBool("enchanting-table.reduce-probability.enabled")) {
            multiplier /= this.getPlugin().getConfigYml().getDouble("enchanting-table.reduce-probability.factor");
        }

        ArrayList<EcoEnchant> enchantments = new ArrayList<>(EcoEnchants.values());
        Collections.shuffle(enchantments); // Prevent list bias towards early enchantments like telekinesis

        boolean gotSpecial = false;

        for (EcoEnchant enchantment : enchantments) {
            if (!enchantment.canEnchantItem(item)) {
                continue;
            }
            if (NumberUtils.randFloat(0, 1) > enchantment.getEnchantmentRarity().getTableProbability() * multiplier) {
                continue;
            }
            if (enchantment.getEnchantmentRarity().getMinimumLevel() > cost) {
                continue;
            }
            if (!enchantment.isEnabled()) {
                continue;
            }
            if (!enchantment.isAvailableFromTable()) {
                continue;
            }
            if (!player.hasPermission("ecoenchants.fromtable." + enchantment.getPermissionName())) {
                continue;
            }

            AtomicBoolean anyConflicts = new AtomicBoolean(false);

            toAdd.forEach((enchant, integer) -> {
                if (enchantment.conflictsWithAny(toAdd.keySet())) {
                    anyConflicts.set(true);
                }
                if (enchant.conflictsWith(enchantment)) {
                    anyConflicts.set(true);
                }

                if (enchant instanceof EcoEnchant ecoEnchant) {
                    if (enchantment.getType().equals(ecoEnchant.getType()) && ecoEnchant.getType().isSingular()) {
                        anyConflicts.set(true);
                    }
                }
            });
            if (anyConflicts.get()) {
                continue;
            }

            int level;

            double maxLevelDouble = enchantment.getMaxLevel();

            if (enchantment.getType().equals(EnchantmentType.SPECIAL)) {
                double enchantlevel1 = NumberUtils.randFloat(0, 1);
                double enchantlevel2 = NumberUtils.bias(enchantlevel1, this.getPlugin().getConfigYml().getDouble("enchanting-table.special-bias"));
                double enchantlevel3 = 1 / maxLevelDouble;
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            } else {
                int maxLevel = this.getPlugin().getConfigYml().getInt("enchanting-table.maximum-obtainable-level");
                double enchantlevel1 = (cost / (double) enchantment.getEnchantmentRarity().getMinimumLevel()) / (maxLevel / (double) enchantment.getEnchantmentRarity().getMinimumLevel());
                double enchantlevel2 = NumberUtils.triangularDistribution(0, 1, enchantlevel1);
                double enchantlevel3 = 1 / maxLevelDouble;
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            }

            level = NumberUtils.equalIfOver(level, enchantment.getMaxLevel());
            toAdd.put(enchantment, level);

            if (this.getPlugin().getConfigYml().getBool("enchanting-table.cap-amount.enabled") && toAdd.size() >= this.getPlugin().getConfigYml().getInt("enchanting-table.cap-amount.limit")) {
                break;
            }

            if (enchantment.getType().equals(EnchantmentType.SPECIAL)) {
                gotSpecial = true;
            }

            if (this.getPlugin().getConfigYml().getBool("enchanting-table.reduce-probability.enabled")) {
                multiplier /= this.getPlugin().getConfigYml().getDouble("enchanting-table.reduce-probability.factor");
            }
        }
        toAdd.forEach(event.getEnchantsToAdd()::putIfAbsent);

        if (SECONDARY_ENCHANTABLE.contains(event.getItem().getType()) && !toAdd.containsKey(EcoEnchants.INDESTRUCTIBILITY)) {
            event.getEnchantsToAdd().put(Enchantment.DURABILITY, CURRENTLY_ENCHANTING_SECONDARY.get(player)[event.whichButton()]);
            CURRENTLY_ENCHANTING_SECONDARY.remove(player);
        }

        if (gotSpecial && this.getPlugin().getConfigYml().getBool("enchanting-table.notify-on-special.enabled")) {
            String soundName = this.getPlugin().getConfigYml().getString("enchanting-table.notify-on-special.sound").toUpperCase();
            Sound sound = Sound.valueOf(soundName);
            float pitch = (float) this.getPlugin().getConfigYml().getDouble("enchanting-table.notify-on-special.pitch");
            player.playSound(
                    player.getLocation(),
                    sound,
                    1.0f,
                    pitch
            );

            if (this.getPlugin().getConfigYml().getBool("enchanting-table.notify-on-special.show-particles")) {
                Particle.DustOptions extra = new Particle.DustOptions(
                        Color.fromRGB(Integer.parseInt(
                                this.getPlugin().getLangYml().getString("special-particle-color").substring(1),
                                16
                        )),
                        1.0f
                );

                Location location = player.getLocation().clone();

                location.add(0, 1, 0);

                int limit = this.getPlugin().getConfigYml().getInt("enchanting-table.notify-on-special.particle-amount");

                for (int i = 0; i < limit; i++) {
                    Location spawnLoc = location.clone();
                    spawnLoc.add(
                            NumberUtils.randFloat(-2, 2),
                            NumberUtils.randFloat(-0.3, 1.6),
                            NumberUtils.randFloat(-2, 2)
                    );

                    spawnLoc.getWorld().spawnParticle(Particle.REDSTONE, spawnLoc, 1, 0, 0, 0, 0, extra, true);
                }
            }

            player.sendMessage(this.getPlugin().getLangYml().getMessage("got-special"));
        }

        // Ew
        this.getPlugin().getScheduler().runLater(() -> {
            ItemStack item0 = event.getInventory().getItem(0);
            if (item0 == null) {
                return;
            }
            if (item0.getItemMeta() instanceof EnchantmentStorageMeta meta) {
                for (Enchantment enchantment : meta.getStoredEnchants().keySet()) {
                    meta.removeStoredEnchant(enchantment);
                }
                event.getEnchantsToAdd().forEach(((enchantment, integer) -> {
                    meta.addStoredEnchant(enchantment, integer, false);
                }));
                item0.setItemMeta(meta);
            }
            event.getInventory().setItem(0, item0);
        }, 1);
    }

    /**
     * Called on prepare enchant.
     * For secondary enchantments, generates unbreaking tooltips.
     *
     * @param event The event to listen to.
     */
    @EventHandler
    public void secondaryEnchant(@NotNull final PrepareItemEnchantEvent event) {
        if (!this.getPlugin().getConfigYml().getBool("enchanting-table.enabled")) {
            return;
        }

        int maxLevel = this.getPlugin().getConfigYml().getInt("enchanting-table.maximum-obtainable-level");

        try {
            event.getOffers()[2].setCost(NumberUtils.equalIfOver(event.getOffers()[2].getCost(), maxLevel));
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {
        }

        if (!SECONDARY_ENCHANTABLE.contains(event.getItem().getType())) {
            return;
        }

        if (!EnchantmentTarget.ALL.getMaterials().contains(event.getItem().getType())) {
            return;
        }

        int bonus = event.getEnchantmentBonus();
        if (bonus > 15) {
            bonus = 15;
        }
        if (bonus == 0) {
            bonus = 1;
        }

        double baseLevel = NumberUtils.randInt(1, 8) + Math.floor((double) bonus / 2) + NumberUtils.randInt(0, bonus);

        int bottomEnchantLevel = (int) Math.ceil(Math.max(baseLevel / 3, 1));
        int midEnchantLevel = (int) ((baseLevel * 2) / 3) + 1;
        int topEnchantLevel = (int) Math.max(baseLevel, bonus * 2);

        bottomEnchantLevel *= (int) Math.ceil((double) maxLevel / 30);
        midEnchantLevel *= (int) Math.ceil((double) maxLevel / 30);
        topEnchantLevel *= (int) Math.ceil((double) maxLevel / 30);

        bottomEnchantLevel = NumberUtils.equalIfOver(bottomEnchantLevel, maxLevel);

        int midUnbreakingLevel = NumberUtils.randInt(1, 3);
        if (midUnbreakingLevel < 2) {
            midUnbreakingLevel = 2;
        }
        if (midEnchantLevel < 15) {
            midUnbreakingLevel = 1;
        }

        int topUnbreakingLevel = 3;
        if (topEnchantLevel < 20) {
            topUnbreakingLevel = 2;
        }
        if (topEnchantLevel < 10) {
            topUnbreakingLevel = 1;
        }

        EnchantmentOffer[] offers = {
                new EnchantmentOffer(Enchantment.DURABILITY, 1, bottomEnchantLevel),
                new EnchantmentOffer(Enchantment.DURABILITY, midUnbreakingLevel, midEnchantLevel),
                new EnchantmentOffer(Enchantment.DURABILITY, topUnbreakingLevel, topEnchantLevel),
        };

        for (int i = 0; i < offers.length; i++) {
            event.getOffers()[i] = offers[i];
        }

        CURRENTLY_ENCHANTING_SECONDARY.remove(event.getEnchanter());

        int[] unbLevels = {
                event.getOffers()[0].getEnchantmentLevel(),
                event.getOffers()[1].getEnchantmentLevel(),
                event.getOffers()[2].getEnchantmentLevel()
        };

        CURRENTLY_ENCHANTING_SECONDARY.put(event.getEnchanter(), unbLevels);
    }
}
