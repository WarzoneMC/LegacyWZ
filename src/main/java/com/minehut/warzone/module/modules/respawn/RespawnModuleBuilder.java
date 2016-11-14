package com.minehut.warzone.module.modules.respawn;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleCollection;

public class RespawnModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<RespawnModule> load(Match match) {
        return new ModuleCollection<>(new RespawnModule(match));
    }

}
