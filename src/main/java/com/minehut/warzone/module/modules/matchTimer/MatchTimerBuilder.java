package com.minehut.warzone.module.modules.matchTimer;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class MatchTimerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<MatchTimer> load(Match match) {
        return new ModuleCollection<>(new MatchTimer());
    }

}
