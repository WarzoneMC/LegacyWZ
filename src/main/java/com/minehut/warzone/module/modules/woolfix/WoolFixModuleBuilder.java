package com.minehut.warzone.module.modules.woolfix;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class WoolFixModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<WoolFixModule> load(Match match) {
        ModuleCollection<WoolFixModule> results = new ModuleCollection<>();
        results.add(new WoolFixModule());

        return results;
    }

}
