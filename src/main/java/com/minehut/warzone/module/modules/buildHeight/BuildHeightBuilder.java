package com.minehut.warzone.module.modules.buildHeight;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.util.Numbers;
import org.jdom2.Element;

public class BuildHeightBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<BuildHeight> load(Match match) {
        ModuleCollection<BuildHeight> result = new ModuleCollection<>();

        if (match.getJson().has("max-build-height")) {
            int height = Numbers.parseInt(match.getJson().get("max-build-height").getAsString());
            result.add(new BuildHeight(height));
        }
        return result;
    }

}
