package com.minehut.warzone.module.modules.sound;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class SoundModuleBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection<SoundModule> load(Match match) {
        return new ModuleCollection<>(new SoundModule());
    }
}
