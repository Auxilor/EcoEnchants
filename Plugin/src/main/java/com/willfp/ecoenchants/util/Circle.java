package com.willfp.ecoenchants.util;

import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Circle {

    /**
     * Get circle as relative vectors
     * @param radius The radius
     * @return An array of {@link Vector}s
     */
    public static Vector[] getCircle(int radius) {
        ArrayList<Vector> circleVecs = new ArrayList<Vector>();

        int xoffset = -radius;
        int zoffset = -radius;

        while (zoffset <= radius) {
            while (xoffset <= radius) {
                if (Math.round(Math.sqrt((xoffset * xoffset) + (zoffset * zoffset))) <= radius) {
                    circleVecs.add(new Vector(xoffset, 0, zoffset));
                } else {
                    xoffset++;
                    continue;
                }
                xoffset++;
            }
            xoffset = -radius;
            zoffset++;
        }

        return circleVecs.toArray(new Vector[0]);
    }
}
