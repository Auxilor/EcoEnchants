package com.willfp.ecoenchants.util.interfaces;

/**
 * Functional Interface to return a value of a specified type given a certain parameter
 *
 * @param <A> The type of object to return
 * @param <B> The type of object for the parameter
 */
@FunctionalInterface
public interface ObjectBiCallable<A, B> {
    A call(B object);
}
