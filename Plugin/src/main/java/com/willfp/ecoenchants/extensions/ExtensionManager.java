package com.willfp.ecoenchants.extensions;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.util.Logger;
import com.willfp.ecoenchants.util.tuplets.Pair;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class containing method to load extensions
 */
public class ExtensionManager {
    private static final Set<Extension> extensions = new HashSet<>();

    /**
     * Load all extensions
     */
    public static void loadExtensions() {
        File dir = new File(EcoEnchantsPlugin.getInstance().getDataFolder(), "/extensions");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File[] extensionJars = dir.listFiles();

        if(extensionJars == null)
            return;

        for (File extensionJar : extensionJars) {
            if(!extensionJar.isFile()) continue;

            try {
                loadExtension(extensionJar);
            } catch (MalformedExtensionException e) {
                Bukkit.getLogger().info(extensionJar.getName() + " caused MalformedExtensionException: " + e.getMessage());
            }
        }
    }

    private static void loadExtension(File extensionJar) throws MalformedExtensionException {
        URL url = null;
        try {
            url = extensionJar.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ClassLoader cl = new URLClassLoader(new URL[]{url}, EcoEnchantsPlugin.class.getClassLoader());

        InputStream ymlIn = cl.getResourceAsStream("extension.yml");

        if (ymlIn == null) {
            throw new MalformedExtensionException("No extension.yml found in " + extensionJar.getName());
        }

        YamlConfiguration extensionYml = YamlConfiguration.loadConfiguration(new InputStreamReader(ymlIn));

        Set<String> keys = extensionYml.getKeys(false);
        ArrayList<String> required = new ArrayList<>(Arrays.asList("main", "name", "version"));
        required.removeAll(keys);
        if(!required.isEmpty()) {
            throw new MalformedExtensionException("Invalid extension.yml found in " + extensionJar.getName() + " - Missing: " + String.join(", ", required));
        }

        String mainClass = extensionYml.getString("main");
        String name = extensionYml.getString("name");
        String version = extensionYml.getString("version");
        Extension.ExtensionMetadata metadata = new Extension.ExtensionMetadata(name, version);

        Class<?> cls;
        Object object = null;
        try {
            cls = cl.loadClass(mainClass);
            object = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(!(object instanceof Extension))
            throw new MalformedExtensionException(extensionJar.getName() + " is invalid");

        Extension extension = (Extension) object;
        extension.setMetadata(metadata);
        extension.enable();
        extensions.add(extension);
    }

    /**
     * Unload all extensions
     */
    public static void unloadExtensions() {
        extensions.forEach(Extension::onDisable);
        extensions.clear();
    }

    /**
     * Reload all extensions
     */
    public static void reloadExtensions() {
        unloadExtensions();
        loadExtensions();
    }

    /**
     * Get set of all loaded extensions
     * @return {@link Set} of {@link Extension}s
     */
    public static Set<Extension> getLoadedExtensions() {
        return extensions;
    }
}
