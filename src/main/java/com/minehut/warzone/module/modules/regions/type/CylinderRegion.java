package com.minehut.warzone.module.modules.regions.type;

import com.minehut.warzone.module.modules.regions.RegionModule;
import com.minehut.warzone.module.modules.regions.parsers.CylinderParser;
import com.minehut.warzone.util.Numbers;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CylinderRegion extends RegionModule {

    private final Vector base;
    private final double radius, height;

    public CylinderRegion(String name, Vector base, double radius, double height) {
        super(name);
        this.base = base;
        this.radius = radius;
        this.height = height;
    }

    public CylinderRegion(CylinderParser parser) {
        this(parser.getName(), parser.getBase(), parser.getRadius(), parser.getHeight());
    }

    public double getBaseX() {
        return base.getX();
    }

    public double getBaseY() {
        return base.getY();
    }

    public double getBaseZ() {
        return base.getZ();
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public boolean contains(Vector vector) {
        return (Math.hypot(Math.abs(vector.getX() - getBaseX()), Math.abs(vector.getZ() - getBaseZ())) <= getRadius()) && Numbers.checkInterval(vector.getY(), getBaseY(), getBaseY() + getHeight());
    }

    @Override
    public BlockRegion getCenterBlock() {
        return (new BlockRegion(null, new Vector(getBaseX(), getBaseY() + .5 * height, getBaseZ())));
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        CuboidRegion bound = new CuboidRegion(null, getBaseX() - radius, getBaseY(), getBaseZ() - radius, getBaseX() + radius, getBaseY() + height, getBaseZ() + radius);
        for (Block block : bound.getBlocks()) {
            if (contains(new BlockRegion(null, block.getX(), block.getY(), block.getZ()))) results.add(block);
        }
        return results;
    }
}
