package com.willfp.ecoenchants.extensions.loader;

import com.willfp.ecoenchants.extensions.Extension;

import java.util.Set;

public interface ExtensionLoader {
    void loadExtensions();

    void unloadExtensions();

    void reloadExtensions();

    Set<Extension> getLoadedExtensions();
}
