package com.minehut.warzone.module.modules.invisibleBlock;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class InvisibleBlockBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<InvisibleBlock> load(Match match) {
        return new ModuleCollection<>(new InvisibleBlock());
    }

}
