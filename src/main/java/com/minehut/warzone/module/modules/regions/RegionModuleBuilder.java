package com.minehut.warzone.module.modules.regions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.BuilderData;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleLoadTime;
import com.minehut.warzone.module.modules.regions.type.EmptyRegion;
import com.minehut.warzone.module.modules.regions.type.EverywhereRegion;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.modules.regions.type.BlockRegion;
import com.minehut.warzone.module.modules.regions.type.CuboidRegion;

import java.util.Iterator;

@BuilderData(load = ModuleLoadTime.EARLIEST)
public class RegionModuleBuilder implements ModuleBuilder {

    public static RegionModule defineJsonRegion(JsonObject json) {
        RegionModule region;

        String name = json.get("name").getAsString();

        if (json.has("type") && !json.get("type").getAsString().equalsIgnoreCase("cuboid")) {
            String type = json.get("type").getAsString();


            if (type.equalsIgnoreCase("block")) {
                region = new BlockRegion(name, json.get("coords").getAsString());
                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
                return region;
            }

        }

        else { //cuboid
            region = new CuboidRegion(name, json);
            if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
            return region;
        }

        return null;
    }

//    public static RegionModule getRegion(Element element, Document document) {
//        RegionModule region;
//        switch (element.getName().toLowerCase()) {
//            case "block":
//                region = new BlockRegion(new BlockParser(element));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "point":
//                region = new PointRegion(new PointParser(element));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "circle":
//                region = new CircleRegion(new CircleParser(element));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "cuboid":
//                region = new CuboidRegion(new CuboidParser(element));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "cylinder":
//                region = new CylinderRegion(new CylinderParser(element));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "empty":
//                region = new EmptyRegion(new EmptyParser(element));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "nowhere":
//                region = new EmptyRegion(new EmptyParser(element));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "everywhere":
//                region = new EverywhereRegion(new EverywhereParser(element));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "rectangle":
//                region = new RectangleRegion(new RectangleParser(element));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "sphere":
//                region = new SphereRegion(new SphereParser(element));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "complement":
//                region = new ComplementRegion(new CombinationParser(element, document));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "intersect":
//                region = new IntersectRegion(new CombinationParser(element, document));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "negative":
//                region = new NegativeRegion(new CombinationParser(element, document));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "union":
//            case "regions":
//                CombinationParser parser = new CombinationParser(element, document);
//                for (RegionModule regionChild : parser.getRegions()) {
//                    GameHandler.getGameHandler().getMatch().getModules().add(regionChild);
//                }
//                region = new UnionRegion(parser);
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "translate":
//                region = new TranslatedRegion(new TranslateParser(element));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            case "mirror":
//                region = new MirroredRegion(new MirrorParser(element));
//                if (region.getName() != null) GameHandler.getGameHandler().getMatch().getModules().add(region);
//                return region;
//            default:
//                if (element.getAttributeValue("name") != null) {
//                    for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
//                        if (element.getAttributeValue("name").equalsIgnoreCase(regionModule.getName())) {
//                            return regionModule;
//                        }
//                    }
//                } else if (element.getAttributeValue("id") != null) {
//                    for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
//                        if (element.getAttributeValue("id").equalsIgnoreCase(regionModule.getName())) {
//                            return regionModule;
//                        }
//                    }
//                } else if (element.getAttributeValue("region") != null) {
//                    for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
//                        if (element.getAttributeValue("region").equalsIgnoreCase(regionModule.getName())) {
//                            return regionModule;
//                        }
//                    }
//                } else {
//                    return getRegion(element.getChildren().get(0));
//                }
//                return null;
//        }
//    }

//    public static RegionModule getRegion(Element element) {
//        return getRegion(element, GameHandler.getGameHandler().getMatch().getDocument());
//    }

    public static RegionModule getRegion(String string) {
        for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
            if (string.equalsIgnoreCase(regionModule.getName())) return regionModule;
        }
        return null;
    }

    @Override
    public ModuleCollection<RegionModule> load(Match match) {
        match.getModules().add(new EverywhereRegion("everywhere"));
        match.getModules().add(new EmptyRegion("nowhere"));
        ModuleCollection<RegionModule> results = new ModuleCollection<>();

        System.out.println("");
        System.out.println("");
        System.out.println("author: " + match.getJson().get("author").getAsString());

        if (match.getJson().has("regions")) {
            Iterator<JsonElement> it = match.getJson().getAsJsonArray("regions").iterator();
            while (it.hasNext()) {
                JsonObject json = it.next().getAsJsonObject();
                defineJsonRegion(json);
            }
        }

//
//        for (Element element : match.getDocument().getRootElement().getChildren("regions")) {
//            for (Element givenRegion : element.getChildren()) {
//                for (Element givenChild : givenRegion.getChildren()) {
//                    for (Element givenSubChild : givenChild.getChildren()) {
//                        for (Element givenChildRegion : givenSubChild.getChildren()) {
//                            getRegion(givenChildRegion);
//                        }
//                        getRegion(givenSubChild);
//                    }
//                    getRegion(givenChild);
//                }
//                if (!givenRegion.getName().equals("apply")) {
//                    getRegion(givenRegion);
//                }
//            }
//        }
        return results;
    }
}
