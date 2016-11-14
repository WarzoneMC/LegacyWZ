package com.minehut.warzone.module.modules.doubleKillPatch;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleLoadTime;
import com.minehut.warzone.module.BuilderData;
import com.minehut.warzone.module.ModuleBuilder;

@BuilderData(load = ModuleLoadTime.LATEST)
public class DoubleKillPatchBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<DoubleKillPatch> load(Match match) {
        return new ModuleCollection<>(new DoubleKillPatch());
    }

}
