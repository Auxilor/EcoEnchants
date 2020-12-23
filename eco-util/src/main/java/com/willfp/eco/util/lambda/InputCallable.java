package com.willfp.eco.util.lambda;

/**
 * Functional Interface that requires an object as a parameter.
 *
 * @param <A> The type of the object to require.
 */
@FunctionalInterface
public interface InputCallable<A> {
    /**
     * Execute the lambda.
     *
     * @param object The lambda parameter.
     */
    void call(A object);
}
