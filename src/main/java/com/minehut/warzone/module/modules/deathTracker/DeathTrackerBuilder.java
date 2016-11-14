package com.minehut.warzone.module.modules.deathTracker;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;

public class DeathTrackerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<DeathTracker> load(Match match) {
        return new ModuleCollection<>(new DeathTracker());
    }

}
