package com.minehut.warzone.util;

import org.bukkit.util.Vector;

/**
 * Created by luke on 2/11/16.
 */
public class VectorUtil {
    public static Vector fromString(String coords) {
        double x = 0, y = 0, z = 0;
        if (coords.contains(", ")) {
            String[] split = coords.split(", ");
            x = Double.valueOf(split[0]);
            y = Double.valueOf(split[1]);
            z = Double.valueOf(split[2]);
        } else {
            String[] split = coords.split(",");
            x = Double.valueOf(split[0]);
            y = Double.valueOf(split[1]);
            z = Double.valueOf(split[2]);
        }
        return new Vector(x, y, z);
    }
}
