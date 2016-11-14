package com.minehut.warzone.module.modules.cycleTimer;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleCollection;

public class CycleTimerModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<CycleTimerModule> load(Match match) {
        return new ModuleCollection<>(new CycleTimerModule(match));
    }

}
