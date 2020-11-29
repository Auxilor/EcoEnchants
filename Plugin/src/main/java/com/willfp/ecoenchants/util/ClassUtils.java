package com.willfp.ecoenchants.util;

public class ClassUtils {
    /**
     * Get if a class exists
     *
     * @param className The class to check
     * @return If the class exists
     * @see Class#forName(String)
     */
    public static boolean exists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
