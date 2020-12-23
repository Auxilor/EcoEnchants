package com.willfp.eco.core.proxy.v1_16_R3;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.willfp.eco.core.proxy.proxies.ChatComponentProxy;
import com.willfp.ecoenchants.display.EnchantDisplay;
import net.minecraft.server.v1_16_R3.ChatBaseComponent;
import net.minecraft.server.v1_16_R3.ChatHoverable;
import net.minecraft.server.v1_16_R3.ChatMessage;
import net.minecraft.server.v1_16_R3.ChatModifier;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.MojangsonParser;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class ChatComponent implements ChatComponentProxy {
    @Override
    public Object modifyComponent(@NotNull final Object object) {
        if (!(object instanceof IChatBaseComponent)) {
            return object;
        }

        IChatBaseComponent chatComponent = (IChatBaseComponent) object;
        chatComponent.stream().forEach(this::modifyBaseComponent);

        return chatComponent;
    }

    private void modifyBaseComponent(@NotNull final IChatBaseComponent component) {
        component.getSiblings().forEach(this::modifyBaseComponent);
        if (component instanceof ChatMessage) {
            Arrays.stream(((ChatMessage) component).getArgs())
                    .filter(o -> o instanceof IChatBaseComponent)
                    .map(o -> (IChatBaseComponent) o)
                    .forEach(this::modifyBaseComponent);
        }

        ChatHoverable hoverable = component.getChatModifier().getHoverEvent();

        if (hoverable == null)
            return;

        JsonObject jsonObject = hoverable.b();
        JsonElement json = hoverable.b().get("contents");
        if (json.getAsJsonObject().get("id") == null) return;
        if (json.getAsJsonObject().get("tag") == null) return;
        String id = json.getAsJsonObject().get("id").toString();
        String tag = json.getAsJsonObject().get("tag").toString();
        ItemStack itemStack = getFromTag(tag, id);

        itemStack = EnchantDisplay.displayEnchantments(itemStack);

        json.getAsJsonObject().remove("tag");
        String newTag = toJson(itemStack);
        json.getAsJsonObject().add("tag", new JsonPrimitive(newTag));

        jsonObject.remove("contents");
        jsonObject.add("contents", json);
        ChatHoverable newHoverable = ChatHoverable.a(jsonObject);
        ChatModifier modifier = component.getChatModifier();
        modifier = modifier.setChatHoverable(newHoverable);

        ((ChatBaseComponent) component).setChatModifier(modifier);
    }

    private static ItemStack getFromTag(@NotNull String jsonTag, @NotNull String id) {
        id = id.replace("minecraft:", "");
        id = id.toUpperCase();
        id = id.replace("\"", "");
        jsonTag = jsonTag.substring(1, jsonTag.length() - 1);
        jsonTag = jsonTag.replace("id:", "\"id\":");
        jsonTag = jsonTag.replace("\\", "");
        Material material = Material.getMaterial(id);

        assert material != null;
        ItemStack itemStack = new ItemStack(material);
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);

        try {
            nmsStack.setTag(MojangsonParser.parse(jsonTag));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    private static String toJson(@NotNull final ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack).getOrCreateTag().toString();
    }
}
