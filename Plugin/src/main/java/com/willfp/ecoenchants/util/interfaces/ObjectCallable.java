package com.willfp.ecoenchants.util.interfaces;

/**
 * Functional Interface to return a value of a given type
 *
 * @param <A> The type to return
 */
@FunctionalInterface
public interface ObjectCallable<A> {
    A call();
}
