package com.minehut.warzone.module.modules.stats;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleCollection;

public class StatsModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<StatsModule> load(Match match) {
        ModuleCollection<StatsModule> results = new ModuleCollection<>();
        results.add(new StatsModule());

        return results;
    }

}
