package com.minehut.warzone.module.modules.visibility;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;

public class VisibilityBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Visibility> load(Match match) {
        return new ModuleCollection<>(new Visibility(match));
    }

}
