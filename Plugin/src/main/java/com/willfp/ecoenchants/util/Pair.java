package com.willfp.ecoenchants.util;

import java.util.Map;

/**
 * Copy of {@link javafx.util.Pair}
 * Spigot doesn't include javafx
 *
 * @param <K> Key
 * @param <V> Value
 */
public class Pair<K, V> implements Map.Entry<K,V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        return this.value = value;
    }

    public K setKey(K key) {
        return this.key = key;
    }

    @Override
    public String toString() {
        String keyString;
        String valueString;

        if(key == null) keyString = "null"; else keyString = key.toString();
        if(value == null) valueString = "null"; else valueString = value.toString();

        return "Key: " + keyString + ", Value: " + valueString;
    }
}
