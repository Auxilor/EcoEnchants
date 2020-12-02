package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.willfp.ecoenchants.display.AbstractPacketAdapter;
import com.willfp.ecoenchants.util.internal.Logger;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PacketChat extends AbstractPacketAdapter {
    public PacketChat() {
        super(PacketType.Play.Server.CHAT, ListenerPriority.MONITOR);
    }

    @Override
    public void onSend(PacketContainer packet) {
        for(int i = 0; i < packet.getChatComponents().size(); i++) {
            WrappedChatComponent component = packet.getChatComponents().read(i);
            String json = component.getJson();

            Gson gson = new Gson();
            Type objectArrayType = new TypeToken<Object[]>(){}.getType();
            Object[] objects = gson.fromJson(json, objectArrayType);
            List<Object> objectsList = Arrays.stream(objects).map(Object::toString).collect(Collectors.toList());
            Logger.info(objectsList + "");
            Logger.info(packet.getChatComponents().read(0)
                    .getJson());
        }
    }
}
