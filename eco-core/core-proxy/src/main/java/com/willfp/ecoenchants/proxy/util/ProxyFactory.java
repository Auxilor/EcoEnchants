package com.willfp.ecoenchants.proxy.util;

import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.proxy.AbstractProxy;
import com.willfp.eco.util.proxy.ProxyConstants;
import com.willfp.eco.util.proxy.UnsupportedVersionException;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;

public class ProxyFactory<T extends AbstractProxy> extends PluginDependent {
    /**
     * Cached proxy implementations in order to not perform expensive reflective class-finding.
     */
    private static final Map<Class<? extends AbstractProxy>, AbstractProxy> CACHE = new IdentityHashMap<>();

    /**
     * The class of the proxy interface.
     */
    private final Class<T> proxyClass;

    /**
     * Create a new Proxy Factory for a specific type.
     *
     * @param plugin     The plugin to create proxies for.
     * @param proxyClass The class of the proxy interface.
     */
    public ProxyFactory(@NotNull final AbstractEcoPlugin plugin,
                        @NotNull final Class<T> proxyClass) {
        super(plugin);
        this.proxyClass = proxyClass;
    }

    /**
     * Get the implementation of a proxy.
     *
     * @return The proxy implementation.
     */
    public @NotNull T getProxy() {
        try {
            T cachedProxy = attemptCache();
            if (cachedProxy != null) {
                return cachedProxy;
            }

            String className = this.getPlugin().getProxyPackage() + "." + ProxyConstants.NMS_VERSION + "." + proxyClass.getSimpleName().replace("Proxy", "");
            final Class<?> class2 = Class.forName(className);
            Object instance = class2.getConstructor().newInstance();
            if (proxyClass.isAssignableFrom(class2) && proxyClass.isInstance(instance)) {
                T proxy = proxyClass.cast(instance);
                CACHE.put(proxyClass, proxy);
                return proxy;
            }
        } catch (Exception e) {
            // If not returned, then throw error
        }

        throw new UnsupportedVersionException("You're running an unsupported server version: " + ProxyConstants.NMS_VERSION);
    }

    private T attemptCache() {
        Object proxy = CACHE.get(proxyClass);
        if (proxy == null) {
            return null;
        }

        if (proxyClass.isInstance(proxy)) {
            return proxyClass.cast(proxy);
        }

        return null;
    }
}
