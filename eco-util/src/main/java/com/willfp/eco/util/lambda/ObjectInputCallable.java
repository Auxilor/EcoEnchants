package com.willfp.eco.util.lambda;

/**
 * Functional Interface to return a value of a specified type given a certain parameter.
 *
 * @param <A> The type of object to return
 * @param <B> The type of object for the parameter
 */
@FunctionalInterface
public interface ObjectInputCallable<A, B> {
    /**
     * Execute the lambda.
     *
     * @param object The lambda parameter.
     * @return The required return type.
     */
    A call(B object);
}
