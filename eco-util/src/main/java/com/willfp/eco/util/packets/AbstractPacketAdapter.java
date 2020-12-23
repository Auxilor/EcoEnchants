package com.willfp.eco.util.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public abstract class AbstractPacketAdapter extends PacketAdapter {
    /**
     * The packet type to listen for.
     */
    private final PacketType type;

    /**
     * Whether the packet adapter should be registered after the server has loaded.
     * <p>
     * Useful for monitor priority adapters that <b>must</b> be ran last.
     */
    @Getter
    private final boolean postLoad;

    /**
     * Create a new packet adapter for a specified plugin and type.
     *
     * @param plugin   The plugin that ProtocolLib should mark as the owner.
     * @param type     The {@link PacketType} to listen for.
     * @param priority The priority at which the adapter should be ran on packet send/receive.
     * @param postLoad If the packet adapter should be registered after the server has loaded.
     */
    protected AbstractPacketAdapter(@NotNull final AbstractEcoPlugin plugin,
                                    @NotNull final PacketType type,
                                    @NotNull final ListenerPriority priority,
                                    final boolean postLoad) {
        super(plugin, priority, Collections.singletonList(type));
        this.type = type;
        this.postLoad = postLoad;
    }

    /**
     * Create a new packet adapter for a specified plugin and type.
     *
     * @param plugin   The plugin that ProtocolLib should mark as the owner.
     * @param type     The {@link PacketType} to listen for.
     * @param postLoad If the packet adapter should be registered after the server has loaded.
     */
    protected AbstractPacketAdapter(@NotNull final AbstractEcoPlugin plugin,
                                    @NotNull final PacketType type,
                                    final boolean postLoad) {
        this(plugin, type, ListenerPriority.NORMAL, postLoad);
    }

    /**
     * The code that should be executed once the packet has been received.
     *
     * @param packet The packet.
     */
    public void onReceive(@NotNull final PacketContainer packet) {
        // Empty rather than abstract as implementations don't need both
    }

    /**
     * THe code that should be executed once the packet has been sent.
     *
     * @param packet The packet.
     */
    public void onSend(@NotNull final PacketContainer packet) {
        // Empty rather than abstract as implementations don't need both
    }

    /**
     * Boilerplate to assert that the packet is of the specified type.
     *
     * @param event The ProtocolLib event.
     */
    @Override
    public final void onPacketReceiving(final PacketEvent event) {
        if (event.getPacket() == null) {
            return;
        }

        if (!event.getPacket().getType().equals(type)) {
            return;
        }

        onReceive(event.getPacket());
    }

    /**
     * Boilerplate to assert that the packet is of the specified type.
     *
     * @param event The ProtocolLib event.
     */
    @Override
    public final void onPacketSending(final PacketEvent event) {
        if (event.getPacket() == null) {
            return;
        }

        if (!event.getPacket().getType().equals(type)) {
            return;
        }

        onSend(event.getPacket());
    }

    /**
     * Register the packet adapter with ProtocolLib.
     */
    public final void register() {
        if (!ProtocolLibrary.getProtocolManager().getPacketListeners().contains(this)) {
            ProtocolLibrary.getProtocolManager().addPacketListener(this);
        }
    }
}
