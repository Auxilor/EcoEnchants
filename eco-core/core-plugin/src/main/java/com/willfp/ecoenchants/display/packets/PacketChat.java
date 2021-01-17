package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.protocollib.AbstractPacketAdapter;
import com.willfp.ecoenchants.proxy.proxies.ChatComponentProxy;
import com.willfp.ecoenchants.util.ProxyUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PacketChat extends AbstractPacketAdapter {
    /**
     * Instantiate a new listener for {@link PacketType.Play.Server#CHAT}.
     *
     * @param plugin The plugin to listen through.
     */
    public PacketChat(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, PacketType.Play.Server.CHAT, ListenerPriority.MONITOR, true);
    }

    @Override
    public void onSend(@NotNull final PacketContainer packet,
                       @NotNull final Player player) {
        for (int i = 0; i < packet.getChatComponents().size(); i++) {
            WrappedChatComponent component = packet.getChatComponents().read(i);
            if (component == null) {
                continue;
            }

            if (component.getHandle() == null) {
                return;
            }

            WrappedChatComponent newComponent = WrappedChatComponent.fromHandle(ProxyUtils.getProxy(ChatComponentProxy.class).modifyComponent(component.getHandle()));
            packet.getChatComponents().write(i, newComponent);
        }
    }
}
