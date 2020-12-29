package com.willfp.eco.util.config;

import com.willfp.eco.util.config.annotations.ConfigUpdater;
import com.willfp.eco.util.config.annotations.InvalidUpdatableClassException;
import com.willfp.eco.util.config.annotations.InvalidUpdateMethodException;
import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConfigHandler extends PluginDependent {
    /**
     * A set of all classes that can be updated.
     */
    private final Set<Class<?>> updatableClasses = new HashSet<>();

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
        updatableClasses.forEach(clazz -> Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
            if (method.isAnnotationPresent(ConfigUpdater.class)) {
                if (method.getParameterTypes().length != 0) {
                    throw new InvalidUpdateMethodException("Update method must not have parameters.");
                }
                if (!Modifier.isStatic(method.getModifiers())) {
                    throw new InvalidUpdateMethodException("Update method must be static.");
                }

                try {
                    method.invoke(null);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new InvalidUpdateMethodException("Update method generated an exception.");
                }
            }
        }));
    }

    /**
     * Register an updatable class.
     *
     * @param updatableClass The class with an update method.
     */
    public void registerUpdatableClass(@NotNull final Class<?> updatableClass) {
        boolean isValid = false;
        for (Method method : updatableClass.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers()) && method.getParameterTypes().length == 0 && method.isAnnotationPresent(ConfigUpdater.class)) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new InvalidUpdatableClassException("Registered updatable class " + updatableClass + " must have an annotated static method with no modifiers.");
        }

        updatableClasses.add(updatableClass);
    }
}
