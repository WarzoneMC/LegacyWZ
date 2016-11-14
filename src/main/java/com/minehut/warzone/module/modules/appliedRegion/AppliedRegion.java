package com.minehut.warzone.module.modules.appliedRegion;

import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.filter.FilterModule;
import com.minehut.warzone.module.modules.regions.RegionModule;
import org.bukkit.event.HandlerList;

public abstract class AppliedRegion implements Module {

    protected final RegionModule region;
    protected final FilterModule filter;
    protected final String message;

    public AppliedRegion(RegionModule region, FilterModule filter, String message) {
        this.region = region;
        this.filter = filter;
        this.message = message;
    }

    public RegionModule getRegion() {
        return region;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
}
