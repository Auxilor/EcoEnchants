package com.willfp.ecoenchants.display;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.willfp.ecoenchants.Main;

import java.util.Arrays;
import java.util.List;

public class DisplayPacketAdapter extends PacketAdapter {
    private static final List<PacketType> packets = Arrays.asList(
            PacketType.Play.Server.WINDOW_ITEMS,
            PacketType.Play.Server.SET_SLOT,
            PacketType.Play.Client.SET_CREATIVE_SLOT
    );

    public DisplayPacketAdapter() {
        super(Main.getInstance(), packets);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if(event.getPacket() == null) return;

        PacketType packetType = event.getPacketType();
        if (PacketType.Play.Server.WINDOW_ITEMS.equals(packetType)) {
            event.getPacket().getItemListModifier().modify(0, (itemStacks) -> {
                itemStacks.forEach(EnchantDisplay::displayEnchantments);
                return itemStacks;
            });
        } else if (PacketType.Play.Server.SET_SLOT.equals(packetType)) {
            event.getPacket().getItemModifier().modify(0, (item) -> {
                item = EnchantDisplay.displayEnchantments(item);
                return item;
            });
        }
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if(event.getPacket() == null) return;

        if(!event.getPacketType().equals(PacketType.Play.Client.SET_CREATIVE_SLOT)) return;

        event.getPacket().getItemModifier().modify(0, (item) -> {
            item = EnchantDisplay.displayEnchantments(item);
            return item;
        });
    }
}
