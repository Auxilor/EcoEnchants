package com.willfp.ecoenchants.enchantments.util;

import org.bukkit.entity.Entity;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Weak metadata to prevent memory leaks.
 * <p>
 * @author Akiranya (Nailm)
 */
public class WeakMetadata {

    /**
     * Summoned entities by the summoning enchantments.
     * <p>
     * <p>K: summoned entity
     * <p>V: the target of the summoned entity
     */
    public static final Map<Entity, Object> SUMMONED_ENTITY_MEMORY = new WeakHashMap<>();

    /**
     * Victim entities which the summoned entities target at.
     * <p>
     * <p>K: entity which the summoned entities target at
     * <p>V: nothing (passing null is fine)
     */
    public static final Map<Entity, Object> SUMMONED_ENTITY_TARGET = new WeakHashMap<>();
}
