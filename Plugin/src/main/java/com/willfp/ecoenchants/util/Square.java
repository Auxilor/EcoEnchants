package com.willfp.ecoenchants.util;

import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Square {

    /**
     * Get square as relative vectors
     * @param radius The radius of the square
     * @return An array of {@link Vector}s
     */
    public static Vector[] getSquare(int radius) {
        ArrayList<Vector> circleVecs = new ArrayList<Vector>();

        int xoffset = -radius;
        int zoffset = -radius;

        while (zoffset <= radius) {
            while (xoffset <= radius) {
                circleVecs.add(new Vector(xoffset, 0, zoffset));
                xoffset++;
            }
            xoffset = -radius;
            zoffset++;
        }

        return circleVecs.toArray(new Vector[0]);
    }
}
