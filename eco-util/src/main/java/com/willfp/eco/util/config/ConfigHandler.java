package com.willfp.eco.util.config;

import com.willfp.eco.util.config.annotations.Updatable;
import com.willfp.eco.util.config.annotations.InvalidUpdatableClassException;
import com.willfp.eco.util.config.annotations.InvalidUpdateMethodException;
import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ConfigHandler extends PluginDependent {
    /**
     * Creates a new config handler and links it to an {@link AbstractEcoPlugin}.
     *
     * @param plugin The plugin to manage.
     */
    public ConfigHandler(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Invoke all update methods.
     */
    public void callUpdate() {
        for (Class<?> clazz : new Reflections("com.willfp").getTypesAnnotatedWith(Updatable.class)) {
            boolean valid = false;

            for (Method method : clazz.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(Updatable.class)) {
                    continue;
                }

                if (!Modifier.isStatic(method.getModifiers())) {
                    continue;
                }

                if (method.getParameterTypes().length != 0) {
                    continue;
                }

                try {
                    method.invoke(null);
                    valid = true;
                    break;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new InvalidUpdateMethodException("Update method generated an exception.");
                }
            }

            if (!valid) {
                throw new InvalidUpdatableClassException("No valid update method found! (Must be static, have no parameters)");
            }
        }
    }
}
