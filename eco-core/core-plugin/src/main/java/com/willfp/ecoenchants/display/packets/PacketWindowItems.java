package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.display.AbstractPacketAdapter;
import com.willfp.ecoenchants.display.EnchantDisplay;
import org.bukkit.inventory.ItemFlag;

public class PacketWindowItems extends AbstractPacketAdapter {
    public PacketWindowItems(AbstractEcoPlugin plugin) {
        super(plugin, PacketType.Play.Server.WINDOW_ITEMS);
    }

    @Override
    public void onSend(PacketContainer packet) {
        packet.getItemListModifier().modify(0, (itemStacks) -> {
            if (itemStacks == null) return null;
            itemStacks.forEach(item -> {
                if (item == null)
                    return;

                boolean hideEnchants = false;

                if (item.getItemMeta() != null) {
                    hideEnchants = item.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ENCHANTS);
                }

                EnchantDisplay.displayEnchantments(item, hideEnchants);
            });
            return itemStacks;
        });
    }
}
