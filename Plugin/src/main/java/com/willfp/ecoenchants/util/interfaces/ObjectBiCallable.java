package com.willfp.ecoenchants.util.interfaces;

@FunctionalInterface
public interface ObjectBiCallable<A, B> {
    A call(B object);
}
