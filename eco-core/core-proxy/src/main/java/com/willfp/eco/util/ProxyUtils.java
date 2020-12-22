package com.willfp.eco.util;

import com.willfp.eco.core.proxy.AbstractProxy;
import com.willfp.eco.core.proxy.ProxyFactory;

public class ProxyUtils {
    public static <T extends AbstractProxy> T getProxy(Class<T> proxyClass) {
        return new ProxyFactory<>(proxyClass).getProxy();
    }
}
