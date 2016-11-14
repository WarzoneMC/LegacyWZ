package com.minehut.warzone.module.modules.regions.type;

import com.minehut.warzone.GameHandler;
import com.minehut.warzone.module.modules.regions.RegionModule;
import com.minehut.warzone.module.modules.regions.parsers.BlockParser;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BlockRegion extends RegionModule {

    protected final Vector vector;

    public BlockRegion(String name, Vector vector) {
        super(name);
        this.vector = vector;
    }

    public BlockRegion(String name, double x, double y, double z) {
        super(name);
        this.vector = new Vector(x, y, z);
    }

    public BlockRegion(String name, String coords) {
        super(name);

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

        this.vector = new Vector(x, y, z);
    }

    public BlockRegion(BlockParser parser) {
        this(parser.getName(), parser.getVector());
    }

    public double getX() {
        return getVector().getX();
    }

    public double getY() {
        return getVector().getY();
    }

    public double getZ() {
        return getVector().getZ();
    }

    public Vector getVector() {
        return vector.clone().add(new Vector(0.5, 0.5, 0.5));
    }

    @Override
    public boolean contains(Vector vector) {
        return vector.getBlockX() == getVector().getBlockX() &&
                vector.getBlockY() == getVector().getBlockY() &&
                vector.getBlockZ() == getVector().getBlockZ();
    }

    @Override
    public BlockRegion getCenterBlock() {
        return this;
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        results.add(getBlock());
        return results;
    }

    public Location getLocation() {
        return getVector().toLocation(GameHandler.getGameHandler().getMatchWorld());
    }

    public Block getBlock() {
        return vector.toLocation(GameHandler.getGameHandler().getMatchWorld()).getBlock();
    }

}
