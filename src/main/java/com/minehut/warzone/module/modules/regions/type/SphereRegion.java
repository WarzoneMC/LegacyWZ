package com.minehut.warzone.module.modules.regions.type;

import com.minehut.warzone.module.modules.regions.parsers.SphereParser;
import com.minehut.warzone.module.modules.regions.RegionModule;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SphereRegion extends RegionModule {

    private final Vector origin;
    private final double radius;

    public SphereRegion(String name, Vector origin, double radius) {
        super(name);
        this.origin = origin;
        this.radius = radius;
    }

    public SphereRegion(SphereParser parser) {
        this(parser.getName(), parser.getOrigin(), parser.getRadius());
    }

    public double getOriginX() {
        return origin.getX();
    }

    public double getOriginY() {
        return origin.getY();
    }

    public double getOriginZ() {
        return origin.getZ();
    }

    public Vector getOrigin() {
        return origin;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean contains(Vector vector) {
        return vector.isInSphere(getOrigin(), getRadius());
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(null, this.origin);
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        CuboidRegion bound = new CuboidRegion(null, getOriginX() - radius, getOriginY() - radius, getOriginZ() - radius, getOriginX() + radius, getOriginY() + radius, getOriginZ() + radius);
        for (Block block : bound.getBlocks()) {
            if (contains(new BlockRegion(null, block.getX(), block.getY(), block.getZ()))) results.add(block);
        }
        return results;
    }
}
