package com.minehut.warzone.module.modules.playable;

import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.modules.regions.RegionModule;
import org.jdom2.Element;

public class PlayableBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Playable> load(Match match) {
        ModuleCollection<Playable> results = new ModuleCollection<>();
        RegionModule region = null;
        //todo: playable region
//        for (Element element : match.getDocument().getRootElement().getChildren("playable")) {
//            if (element != null) {
//                region = new NegativeRegion(new CombinationParser(element, match.getDocument()));
//            }
//        }
//        results.add(new Playable(region));
        return results;
    }
}
