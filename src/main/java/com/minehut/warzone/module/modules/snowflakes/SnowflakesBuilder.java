package com.minehut.warzone.module.modules.snowflakes;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;

public class SnowflakesBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Snowflakes> load(Match match) {
        return new ModuleCollection<>(new Snowflakes());
    }

}
