package com.willfp.eco.core.proxy;

import java.util.HashMap;

public class ProxyFactory<T extends AbstractProxy> {
    private static final HashMap<Class<? extends AbstractProxy>, Object> CACHE = new HashMap<>();

    private final Class<T> proxyClass;

    public ProxyFactory(Class<T> proxyClass) {
        this.proxyClass = proxyClass;
    }

    public T getProxy() {
        try {
            T cachedProxy = attemptCache();
            if(cachedProxy != null) return cachedProxy;

            final Class<?> class2 = Class.forName("com.willfp.eco.core.proxy." + ProxyConstants.NMS_VERSION + "." + proxyClass.getSimpleName().replace("Proxy", ""));
            Object instance = class2.getConstructor().newInstance();
            if (proxyClass.isAssignableFrom(class2) && proxyClass.isInstance(instance)) {
                T proxy = proxyClass.cast(instance);
                CACHE.put(proxyClass, proxy);
            }
        } catch (Exception e) {
            // If not returned, then throw error
        }

        throw new UnsupportedVersionException("You're running an unsupported server version: " + ProxyConstants.NMS_VERSION);
    }

    private T attemptCache() {
        Object proxy = CACHE.get(proxyClass);
        if(proxy == null) return null;

        if(proxyClass.isInstance(proxy)) {
            return proxyClass.cast(proxy);
        }

        return null;
    }
}