package com.minehut.warzone.module.modules.killStreakCount;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class KillStreakBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<KillStreakCounter> load(Match match) {
        return new ModuleCollection<>(new KillStreakCounter());
    }

}
