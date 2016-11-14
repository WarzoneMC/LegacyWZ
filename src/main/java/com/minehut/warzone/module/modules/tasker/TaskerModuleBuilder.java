package com.minehut.warzone.module.modules.tasker;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleCollection;

public class TaskerModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TaskerModule> load(Match match) {
        return new ModuleCollection<>(new TaskerModule(match));
    }
}
