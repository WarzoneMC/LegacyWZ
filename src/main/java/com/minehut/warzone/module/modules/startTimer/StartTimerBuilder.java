package com.minehut.warzone.module.modules.startTimer;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;

public class StartTimerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<StartTimer> load(Match match) {
        return new ModuleCollection<>(new StartTimer(match, 30));
    }

}
