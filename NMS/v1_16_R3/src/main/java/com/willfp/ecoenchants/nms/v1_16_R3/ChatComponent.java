package com.willfp.ecoenchants.nms.v1_16_R3;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.willfp.ecoenchants.nms.api.ChatComponentWrapper;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ChatComponent implements ChatComponentWrapper {
    @Override
    public Object modifyComponent(Object object) {
        if (!(object instanceof IChatBaseComponent)) {
            return object;
        }

        IChatBaseComponent chatComponent = (IChatBaseComponent) object;
        chatComponent.stream().forEach(this::modifyBaseComponent);

        return chatComponent;
    }

    public void modifyBaseComponent(IChatBaseComponent component) {
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
        try {
            itemStack = (ItemStack) Class.forName("com.willfp.ecoenchants.display.EnchantDisplay").getMethod("displayEnchantments", ItemStack.class).invoke(null, itemStack);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }

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

    private static ItemStack getFromTag(String jsonTag, String id) {
        id = id.replaceAll("minecraft:", "");
        id = id.toUpperCase();
        id = id.replaceAll("\"", "");
        jsonTag = jsonTag.substring(1, jsonTag.length() - 1);
        jsonTag = jsonTag.replaceAll("id:", "\"id\":");
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

    private static String toJson(ItemStack itemStack) {
        return CraftItemStack.asNMSCopy(itemStack).getOrCreateTag().toString();
    }
}
