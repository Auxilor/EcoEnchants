package com.willfp.ecoenchants.util.interfaces;

@FunctionalInterface
public interface ObjectCallable<A, B> {
    A call(B object);
}
