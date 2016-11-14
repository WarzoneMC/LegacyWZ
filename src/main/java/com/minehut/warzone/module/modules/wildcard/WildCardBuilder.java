package com.minehut.warzone.module.modules.wildcard;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;

public class WildCardBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection<WildCard> load(Match match) {
        return new ModuleCollection<>(new WildCard());
    }
}
