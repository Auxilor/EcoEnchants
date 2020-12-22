package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.willfp.eco.core.proxy.ProxyFactory;
import com.willfp.eco.core.proxy.proxies.ChatComponentProxy;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.packets.AbstractPacketAdapter;

public class PacketChat extends AbstractPacketAdapter {
    public PacketChat(AbstractEcoPlugin plugin) {
        super(plugin, PacketType.Play.Server.CHAT, ListenerPriority.MONITOR, true);
    }

    @Override
    public void onSend(PacketContainer packet) {
        for (int i = 0; i < packet.getChatComponents().size(); i++) {
            WrappedChatComponent component = packet.getChatComponents().read(i);
            if (component == null)
                continue;
            if (component.getHandle() == null)
                return;
            WrappedChatComponent newComponent = WrappedChatComponent.fromHandle(new ProxyFactory<>(ChatComponentProxy.class).getProxy().modifyComponent(component.getHandle()));
            packet.getChatComponents().write(i, newComponent);
        }
    }
}
