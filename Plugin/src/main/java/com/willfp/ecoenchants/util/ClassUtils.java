package com.willfp.ecoenchants.util;

public class ClassUtils {
    public static boolean exists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
