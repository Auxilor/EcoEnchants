package com.willfp.eco.core.proxy.proxies;


import com.willfp.eco.core.proxy.AbstractProxy;

/**
 * Utility class to manage chat components
 */
public interface ChatComponentProxy extends AbstractProxy {
    Object modifyComponent(Object object);
}