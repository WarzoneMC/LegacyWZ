package com.minehut.warzone.module.modules.teamPicker;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class TeamPickerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TeamPicker> load(Match match) {
        return new ModuleCollection<>(new TeamPicker());
    }

}
