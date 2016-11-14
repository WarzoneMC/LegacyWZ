package com.minehut.warzone.module.modules.deathMessages;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class DeathMessagesBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<DeathMessages> load(Match match) {
        return new ModuleCollection<>(new DeathMessages());
    }
}
