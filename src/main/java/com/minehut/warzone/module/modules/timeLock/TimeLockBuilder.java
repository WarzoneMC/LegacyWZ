package com.minehut.warzone.module.modules.timeLock;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;
import org.jdom2.Element;

public class TimeLockBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TimeLock> load(Match match) {
        ModuleCollection<TimeLock> results = new ModuleCollection<>();

        if (match.getJson().has("timelock")) {
            if (!(match.getJson().get("timelock").getAsString().equalsIgnoreCase("on") || match.getJson().get("timelock").getAsString().equalsIgnoreCase("true"))) {
                return results;
            }
        }

        results.add(new TimeLock());
        GameHandler.getGameHandler().getMatchWorld().setTime(6000);

        return results;
    }

}
