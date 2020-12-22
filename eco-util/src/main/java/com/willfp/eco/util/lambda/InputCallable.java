package com.willfp.eco.util.lambda;

/**
 * Functional Interface that requires an object
 *
 * @param <A> The type of the object to require
 */
@FunctionalInterface
public interface InputCallable<A> {
    void call(A object);
}
