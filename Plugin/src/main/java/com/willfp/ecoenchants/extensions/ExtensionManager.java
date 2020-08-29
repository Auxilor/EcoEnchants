package com.willfp.ecoenchants.extensions;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Class containing method to load extensions
 */
public class ExtensionManager {
    private static final Map<Extension, String> extensions = new HashMap<>();

    /**
     * Load all extensions
     */
    public static void loadExtensions() {
        File dir = new File(EcoEnchantsPlugin.getInstance().getDataFolder(), "/extensions");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File[] extensionJars = dir.listFiles();
        if(extensionJars != null) {
            for (File extensionJar : extensionJars) {
                try {
                    if (extensionJar.isFile()) {
                        try {
                            URL url = extensionJar.toURI().toURL();
                            URL[] urls = {url};

                            ClassLoader cl = new URLClassLoader(urls, EcoEnchantsPlugin.class.getClassLoader());

                            InputStream ymlIn = cl.getResourceAsStream("extension.yml");
                            URL extensionYmlUrl = cl.getResource("extension.yml");

                            if (extensionYmlUrl == null || ymlIn == null) {
                                throw new MalformedExtensionException("No extension.yml found in " + extensionJar.getName());
                            }

                            YamlConfiguration extensionYml = YamlConfiguration.loadConfiguration(new InputStreamReader(ymlIn));
                            if (!extensionYml.getKeys(false).contains("main") || !extensionYml.getKeys(false).contains("name")) {
                                throw new MalformedExtensionException("Invalid extension.yml found in " + extensionJar.getName());
                            }

                            String mainClass = extensionYml.getString("main");
                            String name = extensionYml.getString("name");

                            Class<?> cls = cl.loadClass(mainClass);

                            Object object = cls.newInstance();

                            if (object instanceof Extension) {
                                Extension extension = (Extension) object;
                                extension.onEnable();
                                extensions.put(extension, name);
                            } else {
                                throw new MalformedExtensionException(extensionJar.getName() + " is invalid");
                            }
                        } catch (IllegalAccessException | InstantiationException | IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (MalformedExtensionException e) {
                    Bukkit.getLogger().info(extensionJar.getName() + " caused MalformedExtensionException: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Unload all extensions
     */
    public static void unloadExtensions() {
        extensions.forEach(((extension, s) -> {
            extension.onDisable();
        }));
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
     * Get Map of all loaded extensions and their names
     * @return {@link Map} of {@link Extension}s and their names
     */
    public static Map<Extension, String> getLoadedExtensions() {
        return extensions;
    }
}
