package com.minehut.warzone.module.modules.observers;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class ObserverModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ObserverModule> load(Match match) {
        return new ModuleCollection<>(new ObserverModule(match));
    }

}
