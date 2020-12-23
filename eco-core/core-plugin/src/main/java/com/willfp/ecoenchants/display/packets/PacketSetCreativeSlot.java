package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.eco.util.packets.AbstractPacketAdapter;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.display.EnchantDisplay;
import org.jetbrains.annotations.NotNull;

public class PacketSetCreativeSlot extends AbstractPacketAdapter {
    public PacketSetCreativeSlot(AbstractEcoPlugin plugin) {
        super(plugin, PacketType.Play.Client.SET_CREATIVE_SLOT, false);
    }

    @Override
    public void onReceive(@NotNull PacketContainer packet) {
        packet.getItemModifier().modify(0, (item) -> {
            item = EnchantDisplay.revertDisplay(item);
            return item;
        });
    }
}
