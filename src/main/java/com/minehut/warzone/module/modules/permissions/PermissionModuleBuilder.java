package com.minehut.warzone.module.modules.permissions;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleLoadTime;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.module.BuilderData;
import com.minehut.warzone.module.ModuleBuilder;

@BuilderData(load = ModuleLoadTime.EARLIEST)
public class PermissionModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<PermissionModule> load(Match match) {
        return new ModuleCollection<>(new PermissionModule(GameHandler.getGameHandler().getPlugin()));
    }
}
