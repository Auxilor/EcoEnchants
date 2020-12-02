package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.willfp.ecoenchants.display.AbstractPacketAdapter;
import com.willfp.ecoenchants.nms.ChatComponent;

public class PacketChat extends AbstractPacketAdapter {
    public PacketChat() {
        super(PacketType.Play.Server.CHAT, ListenerPriority.MONITOR);
    }

    @Override
    public void onSend(PacketContainer packet) {
        for(int i = 0; i < packet.getChatComponents().size(); i++) {
            WrappedChatComponent component = packet.getChatComponents().read(i);
            WrappedChatComponent newComponent = WrappedChatComponent.fromHandle(ChatComponent.modifyComponent(component.getHandle()));
            packet.getChatComponents().write(i, newComponent);
        }
    }
}
