package com.willfp.ecoenchants.enchantments.util;

import org.bukkit.entity.Entity;

import java.util.WeakHashMap;

public class WeakMetadata {

    /**
     * Summoned entities by the summoning enchantment extension.
     */
    public final static WeakHashMap<Entity, Object> ECO_TARGET = new WeakHashMap<>();

    /**
     * Victim entities which the summoned entities target at.
     */
    public final static WeakHashMap<Entity, Object> ECO_VICTIM = new WeakHashMap<>();
}
