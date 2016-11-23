package com.minehut.warzone.module.modules.oldPvP;

import com.minehut.warzone.GameHandler;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.*;
import com.minehut.warzone.module.modules.permissions.PermissionModule;

@BuilderData(load = ModuleLoadTime.EARLIEST)
public class OldPvPModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Module> load(Match match) {
        return new ModuleCollection<>(new OldPvPModule());
    }
}
