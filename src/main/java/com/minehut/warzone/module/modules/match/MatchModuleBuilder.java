package com.minehut.warzone.module.modules.match;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class MatchModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<MatchModule> load(Match match) {
        return new ModuleCollection<>(new MatchModule(match));
    }

}
