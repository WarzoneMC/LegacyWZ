package com.minehut.warzone.module.modules.friendlyFire;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.BuilderData;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleLoadTime;

@BuilderData(load = ModuleLoadTime.LATEST)
public class FriendlyFireBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<FriendlyFire> load(Match match) {
        ModuleCollection<FriendlyFire> results = new ModuleCollection<>();
        boolean enabled = true;
        boolean arrowReturn = true;

        if (match.getJson().has("friendly_fire")) {
            if (!(match.getJson().get("friendly_fire").getAsString().equalsIgnoreCase("off") || match.getJson().get("friendly_fire").getAsString().equalsIgnoreCase("false"))) {
                enabled = false;
            }
        }

        results.add(new FriendlyFire(match, enabled, false));
        return results;
    }

}
