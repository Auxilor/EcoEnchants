package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.eco.util.packets.AbstractPacketAdapter;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.display.EnchantDisplay;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

public class PacketSetSlot extends AbstractPacketAdapter {
    public PacketSetSlot(AbstractEcoPlugin plugin) {
        super(plugin, PacketType.Play.Server.SET_SLOT, false);
    }

    @Override
    public void onSend(@NotNull PacketContainer packet) {
        packet.getItemModifier().modify(0, (item) -> {
            boolean hideEnchants = false;

            if (item == null)
                return item;

            if (item.getItemMeta() != null) {
                hideEnchants = item.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ENCHANTS);
            }

            item = EnchantDisplay.displayEnchantments(item, hideEnchants);
            return item;
        });
    }
}
