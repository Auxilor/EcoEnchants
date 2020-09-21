package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.ecoenchants.display.AbstractPacketAdapter;
import com.willfp.ecoenchants.display.EnchantDisplay;

public final class PacketSetSlot extends AbstractPacketAdapter {
    public PacketSetSlot() {
        super(PacketType.Play.Server.SET_SLOT);
    }

    @Override
    public void onSend(PacketContainer packet) {
        packet.getItemModifier().modify(0, (item) -> {
            item = EnchantDisplay.displayEnchantments(item);
            return item;
        });
    }
}
