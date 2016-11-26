package com.minehut.warzone.util;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

/**
 * Created by luke on 6/7/15.
 */
public class LocationUtils {

    public static Location convert(World world, String locString) {
        String[] coords = locString.split(", ");
        try {

            double x = Double.valueOf(coords[0]);
            if(x >= 0) {
                x += .5;
            } else {
                x -= .5;
            }

            double z = Double.valueOf(coords[2]);
            if(z >= 0) {
                z += .5;
            } else {
                z += .5;
            }

            return new Location(
                    world,
                    Double.valueOf(x),
                    Double.valueOf(coords[1]),
                    Double.valueOf(z));
        } catch (Exception e) {
            System.out.println("World Data Read Error: Invalid Location String [" + locString + "]");
        }

        return null;
    }

    public static boolean isInBounds(Location location, double minX, double maxX, double minY, double maxY, double minZ, double maxZ){
        if(location.getX() > minX && location.getX() < maxX && location.getY() > minY && location.getY() < maxY && location.getZ() > minZ && location.getZ() < maxZ){
            return true;
        }else{
            return false;
        }
    }

    public static Location add(Location initialLocation, double xAddition, double yAddition, double zAddition){
        Location newLocation = new Location(initialLocation.getWorld(), initialLocation.getX() + xAddition, initialLocation.getY() + yAddition, initialLocation.getZ() + zAddition);
        return newLocation;
    }

    public static ArrayList<Location> getCylinder(Location center, int radius){

        //Code used for domination cylinders

        ArrayList<Location> locations = new ArrayList<Location>();

        if(radius <= 0){
            radius = 1;
        }

        Location corner = add(center, -1*(radius), 0, -1*(radius));

        for(int x = 0; x < radius*2; x++){
            for(int z = 0; z < radius*2; z++){

                Location current = add(corner, x, 0, z);

                if(current.distance(center) < radius){

                    locations.add(add(current, 0, 0, 0));


                }
            }

        }

        return locations;
    }

    public static ArrayList<Location> getCircle(Location center, double radius, int amount) {
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        ArrayList<Location> locations = new ArrayList<>();
        for(int i = 0;i < amount; i++)
        {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }
}