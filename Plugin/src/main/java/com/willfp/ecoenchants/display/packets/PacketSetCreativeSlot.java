package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.ecoenchants.display.AbstractPacketAdapter;
import com.willfp.ecoenchants.display.EnchantDisplay;

public final class PacketSetCreativeSlot extends AbstractPacketAdapter {
    public PacketSetCreativeSlot() {
        super(PacketType.Play.Client.SET_CREATIVE_SLOT);
    }

    @Override
    public void onReceive(PacketContainer packet) {
        packet.getItemModifier().modify(0, (item) -> {
            item = EnchantDisplay.revertDisplay(item);
            return item;
        });
    }
}
