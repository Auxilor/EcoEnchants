package com.willfp.ecoenchants.enchantments.support.merging.anvil;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public record AnvilResult(@Nullable ItemStack result,
                          @Nullable Integer xp) {
    /**
     * Fail result.
     */
    public static final AnvilResult FAIL = new AnvilResult(null, null);
}
