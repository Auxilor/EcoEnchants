package com.willfp.ecoenchants.util;

import com.willfp.ecoenchants.integrations.placeholder.PlaceholderManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

public class StringUtils {
    public static String translate(String message, Player player) {
        message = PlaceholderManager.translatePlaceholders(message, player);
        message = translateHexColorCodes("&#", "", message);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return ChatColor.translateAlternateColorCodes('&', translateHexColorCodes("&#", "", message));
    }

    public static String translate(String message) {
        message = PlaceholderManager.translatePlaceholders(message, null);
        message = translateHexColorCodes("&#", "", message);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return ChatColor.translateAlternateColorCodes('&', translateHexColorCodes("&#", "", message));
    }

    private static String translateHexColorCodes(String startTag, String endTag, String message) {
        Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while(matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
            + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
            + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
            + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }
}
