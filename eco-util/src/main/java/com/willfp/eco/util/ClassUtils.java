package com.willfp.eco.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ClassUtils {
    /**
     * Get if a class exists.
     *
     * @param className The class to check.
     * @return If the class exists.
     * @see Class#forName(String)
     */
    public boolean exists(@NotNull final String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
