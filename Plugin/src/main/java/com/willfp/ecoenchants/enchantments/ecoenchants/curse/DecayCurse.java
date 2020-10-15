package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.util.DurabilityUtils;
import com.willfp.ecoenchants.util.interfaces.EcoRunnable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.Arrays;
public final class DecayCurse extends EcoEnchant implements EcoRunnable {
    public DecayCurse() {
        super(
                new EcoEnchantBuilder("decay_curse", EnchantmentType.CURSE)
        );
    }

    @Override
    public void run() {
        EcoEnchantsPlugin.getInstance().getServer().getOnlinePlayers().forEach((player -> {
            if(Arrays.stream(player.getInventory().getContents()).parallel().noneMatch(item2 -> EnchantChecks.item(item2, EcoEnchants.DECAY_CURSE)))
                return;

            for(ItemStack item : player.getInventory().getContents()) {
                if(!EnchantChecks.item(item, EcoEnchants.DECAY_CURSE)) continue;

                if(!(item.getItemMeta() instanceof Repairable)) continue;

                if(player.getInventory().getItemInMainHand().equals(item)) continue;
                if(player.getInventory().getItemInOffHand().equals(item)) continue;
                if(player.getItemOnCursor().equals(item)) continue;


                int damage = EcoEnchants.DECAY_CURSE.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "damage");

                DurabilityUtils.damageItemNoBreak(item, damage, player);
            }
        }));
    }

    @Override
    public long getTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "repeat-ticks");
    }
}
