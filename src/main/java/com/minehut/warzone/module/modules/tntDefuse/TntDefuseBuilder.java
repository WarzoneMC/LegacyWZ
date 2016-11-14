package com.minehut.warzone.module.modules.tntDefuse;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;

public class TntDefuseBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TntDefuse> load(Match match) {
        return new ModuleCollection<>(new TntDefuse());
    }

}
