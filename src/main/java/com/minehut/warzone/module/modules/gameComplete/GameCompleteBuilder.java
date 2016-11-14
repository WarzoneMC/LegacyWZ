package com.minehut.warzone.module.modules.gameComplete;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleCollection;

public class GameCompleteBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<GameComplete> load(Match match) {
        return new ModuleCollection<>(new GameComplete());
    }

}
