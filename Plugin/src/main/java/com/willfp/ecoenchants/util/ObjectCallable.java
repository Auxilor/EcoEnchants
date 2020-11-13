package com.willfp.ecoenchants.util;

@FunctionalInterface
public interface ObjectCallable<A, B> {
    A call(B object);
}
