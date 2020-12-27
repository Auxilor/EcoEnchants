package com.willfp.eco.util.extensions.loader;


import com.willfp.eco.util.extensions.Extension;
import com.willfp.eco.util.extensions.MalformedExtensionException;
import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Concrete implementation of {@link ExtensionLoader}.
 */
public class EcoExtensionLoader extends PluginDependent implements ExtensionLoader {
    /**
     * All currently loaded extensions.
     */
    private final Set<Extension> extensions = new HashSet<>();

    /**
     * Create a new extension loader and link it to a specific {@link AbstractEcoPlugin}.
     *
     * @param plugin The plugin to manage
     */
    public EcoExtensionLoader(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Load all present extensions.
     */
    @Override
    public void loadExtensions() {
        File dir = new File(this.getPlugin().getDataFolder(), "/extensions");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File[] extensionJars = dir.listFiles();

        if (extensionJars == null) {
            return;
        }

        for (File extensionJar : extensionJars) {
            if (!extensionJar.isFile()) {
                continue;
            }

            try {
                loadExtension(extensionJar);
            } catch (MalformedExtensionException e) {
                this.getPlugin().getLog().error(extensionJar.getName() + " caused MalformedExtensionException: " + e.getMessage());
            }
        }
    }

    private void loadExtension(@NotNull final File extensionJar) {
        URL url = null;
        try {
            url = extensionJar.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ClassLoader cl = new URLClassLoader(new URL[]{url}, this.getPlugin().getClass().getClassLoader());

        InputStream ymlIn = cl.getResourceAsStream("extension.yml");

        if (ymlIn == null) {
            throw new MalformedExtensionException("No extension.yml found in " + extensionJar.getName());
        }

        YamlConfiguration extensionYml = YamlConfiguration.loadConfiguration(new InputStreamReader(ymlIn));

        Set<String> keys = extensionYml.getKeys(false);
        ArrayList<String> required = new ArrayList<>(Arrays.asList("main", "name", "version"));
        required.removeAll(keys);
        if (!required.isEmpty()) {
            throw new MalformedExtensionException("Invalid extension.yml found in " + extensionJar.getName() + " - Missing: " + String.join(", ", required));
        }

        String mainClass = extensionYml.getString("main");
        String name = extensionYml.getString("name");
        String version = extensionYml.getString("version");
        Extension.ExtensionMetadata metadata = new Extension.ExtensionMetadata(version, name);

        Class<?> cls;
        Object object;
        try {
            cls = cl.loadClass(mainClass);
            object = cls.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new Error(e);
        }

        if (!(object instanceof Extension)) {
            throw new MalformedExtensionException(extensionJar.getName() + " is invalid");
        }

        Extension extension = (Extension) object;
        extension.setMetadata(metadata);
        extension.enable();
        extensions.add(extension);
    }

    /**
     * Unload all existing extensions.
     */
    @Override
    public void unloadExtensions() {
        extensions.forEach(Extension::disable);
        extensions.clear();
    }

    /**
     * Unloads, then loads all extensions.
     */
    @Override
    public void reloadExtensions() {
        unloadExtensions();
        loadExtensions();
    }

    /**
     * Returns all loaded extensions.
     *
     * @return A {@link Set} of all loaded extensions.
     */
    @Override
    public Set<Extension> getLoadedExtensions() {
        return extensions;
    }
}
