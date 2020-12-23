package com.willfp.eco.util.lambda;

/**
 * Represents code that can be later executed.
 */
@FunctionalInterface
public interface Callable {
    /**
     * Execute the lambda.
     */
    void call();
}
