package com.willfp.ecoenchants.enchantments.util;

import org.bukkit.entity.Entity;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Author: Akiranya (Nailm)
 */
public class WeakMetadata {

    /**
     * Summoned entities by the summoning enchantments.
     * <p>
     * K: summoned entity
     * V: the target of the summoned entity
     */
    public final static Map<Entity, Object> SUMMONED_ENTITY_MEMORY = new WeakHashMap<>();

    /**
     * Victim entities which the summoned entities target at.
     * <p>
     * K: entity which the summoned entities target at
     * V: nothing (passing null is fine)
     */
    public final static Map<Entity, Object> SUMMONED_ENTITY_TARGET = new WeakHashMap<>();
}
