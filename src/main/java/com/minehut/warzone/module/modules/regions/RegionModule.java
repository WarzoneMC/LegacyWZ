package com.minehut.warzone.module.modules.regions;

import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.regions.type.BlockRegion;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.List;

public abstract class RegionModule implements Module {

    protected String name;

    public RegionModule(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean contains(BlockRegion region) {
        return contains(region.getVector());
    }

    public boolean contains(Location location) {
        return contains(location.toVector());
    }

    public abstract boolean contains(Vector vector);

    public abstract BlockRegion getCenterBlock();

    public abstract List<Block> getBlocks();

    @Override
    public void unload() {
    }

}
