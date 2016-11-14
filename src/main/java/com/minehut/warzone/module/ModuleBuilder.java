package com.minehut.warzone.module;

import com.minehut.warzone.match.Match;

public interface ModuleBuilder {

    ModuleCollection<? extends Module> load(Match match);

}
