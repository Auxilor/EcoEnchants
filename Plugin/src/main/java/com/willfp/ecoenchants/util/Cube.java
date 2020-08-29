package com.willfp.ecoenchants.util;

import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Cube {

    /**
     * Get cube as relative vectors
     *
     * @param radius The radius of the cube
     * @return An array of {@link Vector}s
     */
    public static Vector[] getCube(int radius) {
        ArrayList<Vector> cubeVecs = new ArrayList<Vector>();

        int xoffset = -radius;
        int zoffset = -radius;
        int yoffset = -radius;

        while(yoffset <= radius) {
            while(zoffset <= radius) {
                while(xoffset <= radius) {
                    cubeVecs.add(new Vector(xoffset, yoffset, zoffset));
                    xoffset++;
                }
                xoffset = -radius;
                zoffset++;
            }
            zoffset = -radius;
            yoffset++;
        }

        return cubeVecs.toArray(new Vector[0]);
    }
}
