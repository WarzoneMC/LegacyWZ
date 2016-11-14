package com.minehut.warzone.module.modules.motd;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.module.ModuleBuilder;

public class MOTDBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<MOTD> load(Match match) {
        ModuleCollection<MOTD> results = new ModuleCollection<>();
        if (Warzone.getInstance().getConfig().getBoolean("custom-motd")) {
            results.add(new MOTD(match));
        }
        return results;
    }

}
