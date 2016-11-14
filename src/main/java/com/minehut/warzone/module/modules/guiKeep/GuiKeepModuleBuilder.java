package com.minehut.warzone.module.modules.guiKeep;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class GuiKeepModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<GuiKeepModule> load(Match match) {
        return new ModuleCollection<>(new GuiKeepModule());
    }

}
