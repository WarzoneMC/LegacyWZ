package com.minehut.warzone.module.modules.blood;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class BloodBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection<Blood> load(Match match) {
        return new ModuleCollection<>(new Blood());
    }
}
