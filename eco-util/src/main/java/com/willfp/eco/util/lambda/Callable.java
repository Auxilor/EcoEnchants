package com.willfp.eco.util.lambda;

/**
 * Simple functional interface to run some code on demand
 */
@FunctionalInterface
public interface Callable {
    void call();
}
