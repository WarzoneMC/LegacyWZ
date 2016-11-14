package com.minehut.warzone.module.modules.regions.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.warzone.module.modules.regions.parsers.CuboidParser;
import com.minehut.warzone.util.VectorUtil;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.module.modules.regions.RegionModule;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CuboidRegion extends RegionModule {

    private final Vector min, max;

    public CuboidRegion(String name, Vector min, Vector max) {
        super(name);
        this.min = Vector.getMinimum(min, max);
        this.max = Vector.getMaximum(min, max);

    }

    public CuboidRegion(String name, double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        this(name, new Vector(xMin, yMin, zMin), new Vector(xMax, yMax, zMax));
    }

    public CuboidRegion(String name, JsonObject jsonObject) {
        super(name);

        Vector v1 = new Vector();
        Vector v2 = new Vector();

        JsonArray array = jsonObject.get("coords").getAsJsonArray();
        Iterator<JsonElement> it = array.iterator();
        int i = 1;
        while (it.hasNext()) {
            String coords = it.next().getAsString();
            if(i == 1) {
                v1 = VectorUtil.fromString(coords);
            } else {
                v2 = VectorUtil.fromString(coords);
            }
            i++;
        }

        this.min = Vector.getMinimum(v1, v2);
        this.max = Vector.getMaximum(v1, v2);
    }

    public CuboidRegion(CuboidParser parser) {
        this(parser.getName(), parser.getMin(), parser.getMax());
    }

    public double getXMin() {
        return min.getX();
    }

    public double getYMin() {
        return min.getY();
    }

    public double getZMin() {
        return min.getZ();
    }

    public double getXMax() {
        return max.getX();
    }

    public double getYMax() {
        return max.getY();
    }

    public double getZMax() {
        return max.getZ();
    }

    @Override
    public boolean contains(Vector vector) {
        return vector.isInAABB(min, max);
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(null, min.getMidpoint(max));
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        for (int x = (int) getXMin(); x < getXMax(); x++) {
            for (int z = (int) getZMin(); z < getZMax(); z++) {
                for (int y = (int) getYMin(); y < getYMax(); y++) {
                    results.add((new Location(GameHandler.getGameHandler().getMatchWorld(), x, y, z).getBlock()));
                }
            }
        }
        return results;
    }


}
