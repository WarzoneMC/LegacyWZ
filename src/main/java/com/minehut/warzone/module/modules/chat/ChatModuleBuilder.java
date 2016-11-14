package com.minehut.warzone.module.modules.chat;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;

public class ChatModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ChatModule> load(Match match) {
        return new ModuleCollection<>(new ChatModule());
    }
}
