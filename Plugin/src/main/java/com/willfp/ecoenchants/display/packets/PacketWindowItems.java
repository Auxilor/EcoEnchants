package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.ecoenchants.display.AbstractPacketAdapter;
import com.willfp.ecoenchants.display.EnchantDisplay;

public final class PacketWindowItems extends AbstractPacketAdapter {
    public PacketWindowItems() {
        super(PacketType.Play.Server.WINDOW_ITEMS);
    }

    @Override
    public void onSend(PacketContainer packet) {
        packet.getItemListModifier().modify(0, (itemStacks) -> {
            itemStacks.forEach(EnchantDisplay::displayEnchantments);
            return itemStacks;
        });
    }
}
