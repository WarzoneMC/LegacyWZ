package com.minehut.warzone.module.modules.worldFreeze;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleCollection;

public class WorldFreezeBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<WorldFreeze> load(Match match) {
        return new ModuleCollection<>(new WorldFreeze(match));
    }

}
