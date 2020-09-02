package com.willfp.ecoenchants.listeners;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.Material;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EnchantingListeners implements Listener {
    private static final Set<Material> secondary = new HashSet<Material>() {{
        add(Material.ELYTRA);
        add(Material.SHIELD);
    }};
    public static HashMap<Player, int[]> currentlyEnchantingSecondary = new HashMap<>();

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        currentlyEnchantingSecondary.remove(event.getPlayer());
    }

    @EventHandler
    public void enchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        int cost = event.getExpLevelCost();
        Map<Enchantment, Integer> toAdd = event.getEnchantsToAdd();
        if (!ConfigManager.getConfig().getBool("enchanting-table.enabled")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    ItemStack item = event.getInventory().getItem(0);
                    event.getInventory().setItem(0, item);
                }
            }.runTaskLater(EcoEnchantsPlugin.getInstance(), 1);
            return;
        }

        if ((secondary.contains(event.getItem().getType()))) {
            try {
                ItemStack lapis = event.getInventory().getItem(1);
                lapis.setAmount(event.getInventory().getItem(1).getAmount() - (event.whichButton() + 1));
                event.getInventory().setItem(1, lapis);
            } catch (NullPointerException ignored) {} // Triggered if you're in creative
        }

        double multiplier = 0.01;
        if (Target.Applicable.BOOK.getMaterials().contains(item.getType())) {
            multiplier /= ConfigManager.getConfig().getInt("enchanting-table.book-times-less-likely");
        }

        if (ConfigManager.getConfig().getBool("enchanting-table.reduce-probability.enabled")) {
            multiplier /= ConfigManager.getConfig().getDouble("enchanting-table.reduce-probability.factor");
        }

        ArrayList<EcoEnchant> enchantments = new ArrayList<>(EcoEnchants.getAll());
        Collections.shuffle(enchantments); // Prevent list bias towards early enchantments like telekinesis

        boolean gotSpecial = false;

        for (EcoEnchant enchantment : enchantments) {

            if (!enchantment.canEnchantItem(item))
                continue;
            if (NumberUtils.randFloat(0, 1) > enchantment.getRarity().getProbability() * multiplier)
                continue;
            if (enchantment.getRarity().getMinimumLevel() > cost)
                continue;
            if(enchantment.isDisabled())
                continue;
            if (!enchantment.canGetFromTable())
                continue;
            if (!player.hasPermission("ecoenchants.fromtable." + enchantment.getPermissionName()))
                continue;

            AtomicBoolean anyConflicts = new AtomicBoolean(false);
            toAdd.forEach((enchant, integer) -> {
                if (enchantment.conflictsWithAny(toAdd.keySet())) anyConflicts.set(true);
                if (enchant.conflictsWith(enchantment)) anyConflicts.set(true);

                if(EcoEnchants.getFromEnchantment(enchant) != null) {
                    EcoEnchant ecoEnchant = EcoEnchants.getFromEnchantment(enchant);
                    if (enchantment.getType().equals(EcoEnchant.EnchantmentType.SPECIAL) && ecoEnchant.getType().equals(EcoEnchant.EnchantmentType.SPECIAL)) anyConflicts.set(true);
                    if (enchantment.getType().equals(EcoEnchant.EnchantmentType.ARTIFACT) && ecoEnchant.getType().equals(EcoEnchant.EnchantmentType.ARTIFACT)) anyConflicts.set(true);
                }
            });
            if (anyConflicts.get()) continue;

            int level;

            double maxLevelDouble = enchantment.getMaxLevel();

            if(enchantment.getType().equals(EcoEnchant.EnchantmentType.SPECIAL)) {
                double enchantlevel1 = NumberUtils.randFloat(0, 1);
                double enchantlevel2 = NumberUtils.bias(enchantlevel1, ConfigManager.getConfig().getDouble("enchanting-table.special-bias"));
                double enchantlevel3 = 1 / maxLevelDouble;
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            } else {
                int maxLevel = ConfigManager.getConfig().getInt("enchanting-table.maximum-obtainable-level");
                double enchantlevel1 = (cost / (double) enchantment.getRarity().getMinimumLevel()) / (maxLevel / (double) enchantment.getRarity().getMinimumLevel());
                double enchantlevel2 = NumberUtils.triangularDistribution(0, 1, enchantlevel1);
                double enchantlevel3 = 1 / maxLevelDouble;
                level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
            }

            level = NumberUtils.equalIfOver(level, enchantment.getMaxLevel());
            toAdd.put(enchantment, level);

            if(ConfigManager.getConfig().getBool("enchanting-table.cap-amount.enabled")) {
                if(toAdd.size() >= ConfigManager.getConfig().getInt("enchanting-table.cap-amount.limit")) {
                    break;
                }
            }

            if(enchantment.getType().equals(EcoEnchant.EnchantmentType.SPECIAL)) gotSpecial = true;

            if (ConfigManager.getConfig().getBool("enchanting-table.reduce-probability.enabled")) {
                multiplier /= ConfigManager.getConfig().getDouble("enchanting-table.reduce-probability.factor");
            }
        }
        toAdd.forEach(event.getEnchantsToAdd()::putIfAbsent);

        if((secondary.contains(event.getItem().getType()))) {
            if(!toAdd.containsKey(EcoEnchants.INDESTRUCTIBILITY)) {
                event.getEnchantsToAdd().put(Enchantment.DURABILITY, currentlyEnchantingSecondary.get(player)[event.whichButton()]);
                currentlyEnchantingSecondary.remove(player);
            }
        }

        if(gotSpecial && ConfigManager.getConfig().getBool("enchanting-table.notify-on-special")) {
            player.sendMessage(ConfigManager.getLang().getMessage("got-special"));
        }

        // Ew
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack item = event.getInventory().getItem(0);
                assert item != null;
                if(item.getItemMeta() instanceof EnchantmentStorageMeta) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                    for(Enchantment enchantment : meta.getStoredEnchants().keySet()) {
                        meta.removeStoredEnchant(enchantment);
                    }
                    event.getEnchantsToAdd().forEach(((enchantment, integer) -> {
                        meta.addStoredEnchant(enchantment, integer, false);
                    }));
                    item.setItemMeta(meta);
                }
                event.getInventory().setItem(0, item);
            }
        }.runTaskLater(EcoEnchantsPlugin.getInstance(), 1);
    }

    @EventHandler
    public void allowElytraEnchant(PrepareItemEnchantEvent event) {
        try {
            event.getOffers()[2].setCost(NumberUtils.equalIfOver(event.getOffers()[2].getCost(), ConfigManager.getConfig().getInt("enchanting-table.maximum-obtainable-level")));
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {}

        if (!secondary.contains(event.getItem().getType()))
            return;

        int bonus = event.getEnchantmentBonus();
        if (bonus > 15) {
            bonus = 15;
        }
        if (bonus == 0) {
            bonus = 1;
        }


        int level2 = (int) Math.ceil(NumberUtils.randFloat(1.1, 2.5));
        EnchantmentOffer offer2 = new EnchantmentOffer(Enchantment.DURABILITY, level2, (int) Math.floor(bonus * 1.5));

        EnchantmentOffer offer3 = new EnchantmentOffer(Enchantment.DURABILITY, NumberUtils.randInt(2, 3), bonus * 2);

        if (offer3.getEnchantmentLevel() < offer2.getEnchantmentLevel()) {
            int temp = offer2.getEnchantmentLevel();
            offer2.setEnchantmentLevel(offer3.getEnchantmentLevel());
            offer3.setEnchantmentLevel(temp);
        }

        EnchantmentOffer[] offers = {
                new EnchantmentOffer(Enchantment.DURABILITY, 1, bonus),
                offer2,
                offer3
        };

        event.getOffers()[0] = offers[0];
        if (bonus > 5) {
            event.getOffers()[1] = offers[1];
        }
        if (bonus > 10) {
            event.getOffers()[2] = offers[2];
        }

        currentlyEnchantingSecondary.remove(event.getEnchanter());
        int[] unbLevels = {
                event.getOffers()[0].getEnchantmentLevel(),
                event.getOffers()[1].getEnchantmentLevel(),
                event.getOffers()[2].getEnchantmentLevel()
        };
        currentlyEnchantingSecondary.put(event.getEnchanter(), unbLevels);
    }
}
