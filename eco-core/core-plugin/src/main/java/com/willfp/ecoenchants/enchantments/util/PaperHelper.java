package com.willfp.ecoenchants.enchantments.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class PaperHelper {
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .character('§')
            .build();

    /**
     * Convert string to a component.
     *
     * @param string The string.
     * @return The component.
     */
    public static Component toComponent(@NotNull final String string) {
        return SERIALIZER.deserialize(string);
    }
}
