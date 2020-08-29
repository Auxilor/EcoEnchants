package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.EcoRunnable;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.ItemDurability;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.Arrays;
public class Repairing extends EcoEnchant implements EcoRunnable {
    public Repairing() {
        super(
                new EcoEnchantBuilder("repairing", EnchantmentType.SPECIAL, Target.Applicable.ALL, 4.0)
        );
    }

    @Override
    public void run() {
        EcoEnchantsPlugin.getInstance().getServer().getOnlinePlayers().parallelStream().forEach((player -> {
            if(Arrays.stream(player.getInventory().getContents()).parallel().noneMatch(item2 -> EnchantChecks.item(item2, EcoEnchants.REPAIRING)))
                return;

            for(ItemStack item : player.getInventory().getContents()) {
                if(!EnchantChecks.item(item, EcoEnchants.REPAIRING)) continue;

                if(!(item.getItemMeta() instanceof Repairable)) continue;

                if(player.getInventory().getItemInMainHand().equals(item)) continue;
                if(player.getInventory().getItemInOffHand().equals(item)) continue;
                if(player.getItemOnCursor().equals(item)) continue;


                int level = EnchantChecks.getItemLevel(item, EcoEnchants.REPAIRING);
                int multiplier = EcoEnchants.REPAIRING.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "multiplier");
                int repairAmount = level * multiplier;

                ItemDurability.repairItem(item, repairAmount);
            }
        }));
    }

    @Override
    public long getTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "repeat-ticks");
    }
}
