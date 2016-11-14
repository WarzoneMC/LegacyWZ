package com.minehut.warzone.module.modules.teamManager;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class TeamManagerModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TeamManagerModule> load(Match match) {
        return new ModuleCollection<>(new TeamManagerModule(match));
    }

}
