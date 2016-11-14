package com.minehut.warzone.module.modules.craft;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class CraftModuleBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection<CraftModule> load(Match match) {
        return new ModuleCollection<>(new CraftModule());
    }
}
