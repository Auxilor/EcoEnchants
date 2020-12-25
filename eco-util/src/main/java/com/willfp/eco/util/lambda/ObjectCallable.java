package com.willfp.eco.util.lambda;

/**
 * Functional Interface to return a value of a given type.
 *
 * @param <A> The type to return.
 */
@FunctionalInterface
public interface ObjectCallable<A> {
    /**
     * Execute the lambda.
     *
     * @return The return value of the specified type.
     */
    A call();
}
