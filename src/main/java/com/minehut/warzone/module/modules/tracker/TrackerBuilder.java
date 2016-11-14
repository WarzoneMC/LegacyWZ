package com.minehut.warzone.module.modules.tracker;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;

public class TrackerBuilder implements ModuleBuilder {

    @SuppressWarnings("unchecked")
    @Override
    public ModuleCollection load(Match match) {
        return new ModuleCollection(new DamageTracker(), new SpleefTracker());
    }

}
