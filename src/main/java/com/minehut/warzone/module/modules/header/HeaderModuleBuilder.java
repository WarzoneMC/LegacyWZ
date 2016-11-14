package com.minehut.warzone.module.modules.header;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleLoadTime;
import com.minehut.warzone.module.BuilderData;
import com.minehut.warzone.module.ModuleBuilder;

@BuilderData(load = ModuleLoadTime.LATEST)
public class HeaderModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<HeaderModule> load(Match match) {
        return new ModuleCollection<>(new HeaderModule(match.getLoadedMap()));
    }
}
