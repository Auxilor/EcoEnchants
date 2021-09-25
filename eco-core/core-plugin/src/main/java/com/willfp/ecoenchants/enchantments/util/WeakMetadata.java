package com.willfp.ecoenchants.enchantments.util;

import org.bukkit.entity.Entity;

import java.util.Map;
import java.util.WeakHashMap;

public class WeakMetadata {
    /**
     * Weak metadata to prevent memory leaks.
     * <p>
     * Thanks Akiranya for the change!
     */
    public static final Map<Entity, Object> WEAK_META = new WeakHashMap<>();
}
