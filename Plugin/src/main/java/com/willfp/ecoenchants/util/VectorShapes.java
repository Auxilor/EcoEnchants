package com.willfp.ecoenchants.util;

import org.bukkit.util.Vector;

import java.util.ArrayList;

public class VectorShapes {

    /**
     * Get circle as relative vectors
     * @param radius The radius
     * @return An array of {@link Vector}s
     */
    public static Vector[] getCircle(int radius) {
        ArrayList<Vector> circleVecs = new ArrayList<>();

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

    /**
     * Get square as relative vectors
     * @param radius The radius of the square
     * @return An array of {@link Vector}s
     */
    public static Vector[] getSquare(int radius) {
        ArrayList<Vector> circleVecs = new ArrayList<>();

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

    /**
     * Get cube as relative vectors
     * @param radius The radius of the cube
     * @return An array of {@link Vector}s
     */
    public static Vector[] getCube(int radius) {
        ArrayList<Vector> cubeVecs = new ArrayList<>();

        int xoffset = -radius;
        int zoffset = -radius;
        int yoffset = -radius;

        while (yoffset <= radius) {
            while (zoffset <= radius) {
                while (xoffset <= radius) {
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
