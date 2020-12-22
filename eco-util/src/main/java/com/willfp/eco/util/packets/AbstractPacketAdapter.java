package com.willfp.eco.util.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;

import java.util.Collections;

public abstract class AbstractPacketAdapter extends PacketAdapter {
    private final PacketType type;
    private final boolean postLoad;

    protected AbstractPacketAdapter(AbstractEcoPlugin plugin, PacketType type, ListenerPriority priority, boolean postLoad) {
        super(plugin, priority, Collections.singletonList(type));
        this.type = type;
        this.postLoad = postLoad;
    }

    protected AbstractPacketAdapter(AbstractEcoPlugin plugin, PacketType type, boolean postLoad) {
        super(plugin, Collections.singletonList(type));
        this.type = type;
        this.postLoad = postLoad;
    }

    public void onReceive(PacketContainer packet) {
        // Empty rather than abstract as implementations don't need both
    }

    public void onSend(PacketContainer packet) {
        // Empty rather than abstract as implementations don't need both
    }

    @Override
    public final void onPacketReceiving(PacketEvent event) {
        if (event.getPacket() == null)
            return;

        if (!event.getPacket().getType().equals(type))
            return;

        onReceive(event.getPacket());
    }

    @Override
    public final void onPacketSending(PacketEvent event) {
        if (event.getPacket() == null)
            return;

        if (!event.getPacket().getType().equals(type))
            return;

        onSend(event.getPacket());
    }

    public final void register() {
        if (!ProtocolLibrary.getProtocolManager().getPacketListeners().contains(this)) {
            ProtocolLibrary.getProtocolManager().addPacketListener(this);
        }
    }

    public final boolean isPostLoad() {
        return postLoad;
    }
}
