package com.minehut.warzone.module.modules.mobSpawn;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleBuilder;

public class MobSpawnModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<MobSpawnModule> load(Match match) {
        ModuleCollection<MobSpawnModule> results = new ModuleCollection<>();

        if (match.getJson().has("mobspawn")) {
            if (!(match.getJson().get("mobspawn").getAsString().equalsIgnoreCase("on") || match.getJson().get("mobspawn").getAsString().equalsIgnoreCase("true"))) {
                return results;
            }
        }

        results.add(new MobSpawnModule());
        return results;
    }

}
