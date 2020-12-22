package com.willfp.eco.util;

import com.willfp.eco.util.integrations.placeholder.PlaceholderManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;

public class StringUtils {
    /**
     * Translate a string - converts Placeholders and Color codes
     *
     * @param message The message to translate
     * @param player  The player to translate placeholders with respect to
     *
     * @return The message, translated
     */
    public static String translate(String message, @Nullable Player player) {
        message = PlaceholderManager.translatePlaceholders(message, player);
        message = translateHexColorCodes(message);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return ChatColor.translateAlternateColorCodes('&', translateHexColorCodes(message));
    }

    /**
     * Translate a string without respect to a player
     *
     * @param message The message to translate
     *
     * @return The message, translated
     *
     * @see StringUtils#translate(String, Player)
     */
    public static String translate(String message) {
        message = PlaceholderManager.translatePlaceholders(message, null);
        message = translateHexColorCodes(message);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return ChatColor.translateAlternateColorCodes('&', translateHexColorCodes(message));
    }

    private static String translateHexColorCodes(String message) {
        Pattern hexPattern = Pattern.compile("&#" + "([A-Fa-f0-9]{6})" + "");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }

    /**
     * Internal implementation of {@link String#valueOf}
     * Formats collections and doubles better
     *
     * @param object The object to convert to string
     *
     * @return The object stringified
     */
    public static String internalToString(@Nullable Object object) {
        if (object == null) return "null";

        if (object instanceof Integer) {
            return ((Integer) object).toString();
        } else if (object instanceof String) {
            return (String) object;
        } else if (object instanceof Double) {
            return NumberUtils.format((Double) object);
        } else if (object instanceof Collection<?>) {
            Collection<?> c = (Collection<?>) object;
            return c.stream().map(StringUtils::internalToString).collect(Collectors.joining(", "));
        } else return String.valueOf(object);
    }

    /**
     * Remove a string of characters from the start of a string
     *
     * @param s      The string to remove the prefix from
     * @param prefix The substring to remove
     *
     * @return The string with the prefix removed
     */
    public static String removePrefix(String s, String prefix) {
        if (s != null && prefix != null && s.startsWith(prefix)) {
            return s.substring(prefix.length());
        }
        return s;
    }
}
