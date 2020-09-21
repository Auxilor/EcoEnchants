package com.willfp.ecoenchants.display;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.willfp.ecoenchants.EcoEnchantsPlugin;

import java.util.Collections;

public abstract class AbstractPacketAdapter extends PacketAdapter {
    private final PacketType type;

    protected AbstractPacketAdapter(PacketType type) {
        super(EcoEnchantsPlugin.getInstance(), Collections.singletonList(type));
        this.type = type;
    }

    public void onReceive(PacketContainer packet) {}

    public void onSend(PacketContainer packet) {}

    @Override
    public final void onPacketReceiving(PacketEvent event) {
        if(event.getPacket() == null)
            return;

        if(!event.getPacket().getType().equals(type))
            return;

        onReceive(event.getPacket());
    }

    @Override
    public final void onPacketSending(PacketEvent event) {
        if(event.getPacket() == null)
            return;

        if(!event.getPacket().getType().equals(type))
            return;

        onSend(event.getPacket());
    }

    public final void register() {
        if(!EcoEnchantsPlugin.getInstance().protocolManager.getPacketListeners().contains(this)) {
            EcoEnchantsPlugin.getInstance().protocolManager.addPacketListener(this);
        }
    }
}
