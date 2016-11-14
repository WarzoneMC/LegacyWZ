package com.minehut.warzone.module.modules.hunger;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;

public class HungerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Hunger> load(Match match) {
        ModuleCollection<Hunger> results = new ModuleCollection<>();
        try {
            String data = match.getJson().get("hunger").getAsString();
            if (data.equalsIgnoreCase("on") || data.equalsIgnoreCase("true")) {
                results.add(new Hunger(true));
            } else {
                results.add(new Hunger(false));
            }
        } catch (NullPointerException e) {
            results.add(new Hunger(false));
        }
        return results;
    }

}
