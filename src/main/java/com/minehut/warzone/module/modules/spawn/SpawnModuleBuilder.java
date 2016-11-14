package com.minehut.warzone.module.modules.spawn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.match.GameType;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.BuilderData;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleLoadTime;
import com.minehut.warzone.module.modules.appliedRegion.type.BlockBreakRegion;
import com.minehut.warzone.module.modules.appliedRegion.type.BlockPlaceRegion;
import com.minehut.warzone.module.modules.filter.type.TeamFilter;
import com.minehut.warzone.module.modules.regions.RegionModule;
import com.minehut.warzone.module.modules.regions.type.BlockRegion;
import com.minehut.warzone.module.modules.regions.type.CylinderRegion;
import com.minehut.warzone.util.Teams;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@BuilderData(load = ModuleLoadTime.EARLY)
public class SpawnModuleBuilder implements ModuleBuilder {

    ArrayList<GameType> ignoreSafezoneGames = new ArrayList<>();
    ArrayList<GameType> ignoreAntiBuildZoneGames = new ArrayList<>();

    @Override
    public ModuleCollection<SpawnModule> load(Match match) {
        ModuleCollection results = new ModuleCollection<>();

        ignoreSafezoneGames.add(GameType.ELIMINATION);
        ignoreSafezoneGames.add(GameType.BLITZ);
        ignoreSafezoneGames.add(GameType.INFECTED);

        ignoreAntiBuildZoneGames.add(GameType.ELIMINATION);
        ignoreAntiBuildZoneGames.add(GameType.BLITZ);

        Iterator<JsonElement> it = match.getJson().get("spawns").getAsJsonArray().iterator();
        while (it.hasNext()) {
            JsonObject json = it.next().getAsJsonObject();

            TeamModule team;
            if (json.has("team") && !json.get("team").getAsString().equalsIgnoreCase("observers")) {
                team = Teams.getTeamById(json.get("team").getAsString()).orNull();
            } else {
                team = Teams.getTeamById("observers").get();
            }

            List<Pair<RegionModule, Vector>> regions = new ArrayList<>();
            Location location = new Location(GameHandler.getGameHandler().getMatchWorld(), 0, 0, 0);
            if (json.has("yaw")) {
                location.setYaw(json.get("yaw").getAsFloat());
            }

            BlockRegion spawnRegion = new BlockRegion(team.getName() + "-spawn", json.get("coords").getAsString());

            regions.add(new ImmutablePair<RegionModule, Vector>(spawnRegion, location.getDirection()));

            boolean safe = true;
            if(this.ignoreSafezoneGames.contains(match.getGameType())) safe = false;

            results.add(new SpawnModule(team, regions, safe, true));


            if (!this.ignoreAntiBuildZoneGames.contains(match.getGameType())) {
                if (!team.isObserver()) {
                    for (TeamModule t : Teams.getTeams()) {
                        Vector ve = spawnRegion.getVector();
                        Vector v = new Vector(ve.getX(), ve.getY() - 50, ve.getZ());
                        CylinderRegion spawnZone = new CylinderRegion(null, v, 10, 100);
                        results.add(new BlockBreakRegion(spawnZone, new TeamFilter(t), null));
                        results.add(new BlockPlaceRegion(spawnZone, new TeamFilter(t), null));
                    }
                }
            }

        }

        return results;




//        for (Element spawns : match.getDocument().getRootElement().getChildren("spawns")) {
//            for (Element spawn : spawns.getChildren("spawn")) {
//                TeamModule team = Teams.getTeamById(spawns.getAttributeValue("team") != null ? spawns.getAttributeValue("team") : spawn.getAttributeValue("team")).orNull();
//                List<Pair<RegionModule, Vector>> regions = new ArrayList<>();
//                if (spawn.getChildren("regions").size() > 0) {
//                    regions.addAll(getRegions(spawn.getChild("regions")));
//                } else {
//                    regions.addAll(getRegions(spawn));
//                }
//                String kits = null;
//                if (spawns.getAttributeValue("kit") != null)
//                    kits = spawns.getAttributeValue("kit");
//                if (spawn.getAttributeValue("kit") != null)
//                    kits = spawn.getAttributeValue("kit");
//                Kit kit = null;
//                for (Kit kitModule : match.getModules().getModules(Kit.class)) {
//                    if (kitModule.getName().equals(kits)) kit = kitModule;
//                }
//                results.add(new SpawnModule(team, regions, kit, true, true));
//            }
//            for (Element spawn : spawns.getChildren("default")) {
//                TeamModule team = Teams.getTeamById("observers").get();
//                List<Pair<RegionModule, Vector>> regions = new ArrayList<>();
//                if (spawn.getChildren().size() > 0) {
//                    if (spawn.getChildren("regions").size() > 0) {
//                        regions.addAll(getRegions(spawn.getChild("regions")));
//                    } else {
//                        regions.addAll(getRegions(spawn));
//                    }
//                } else {
//                    PointRegion point = new PointRegion(new PointParser(spawn));
//                    regions.add(new ImmutablePair<RegionModule, Vector>(point, point.getLocation().getDirection()));
//                }
//                String kits = null;
//                if (spawns.getAttributeValue("kit") != null)
//                    kits = spawns.getAttributeValue("kit");
//                if (spawn.getAttributeValue("kit") != null)
//                    kits = spawn.getAttributeValue("kit");
//                Kit kit = null;
//                for (Kit kitModule : match.getModules().getModules(Kit.class)) {
//                    if (kitModule.getName().equals(kits)) kit = kitModule;
//                }
//                results.add(new SpawnModule(team, regions, kit, true, true));
//            }
//            for (Element element : spawns.getChildren("spawns")) {
//                for (Element spawn : element.getChildren("spawn")) {
//                    TeamModule team = null;
//                    if (spawns.getAttributeValue("team") != null)
//                        team = Teams.getTeamById(spawns.getAttributeValue("team")).orNull();
//                    if (element.getAttributeValue("team") != null)
//                        team = Teams.getTeamById(element.getAttributeValue("team")).orNull();
//                    if (spawn.getAttributeValue("team") != null)
//                        team = Teams.getTeamById(spawn.getAttributeValue("team")).orNull();
//                    List<Pair<RegionModule, Vector>> regions = new ArrayList<>();
//                    if (spawn.getChildren("regions").size() > 0) {
//                        regions.addAll(getRegions(spawn.getChild("regions")));
//                    } else {
//                        regions.addAll(getRegions(spawn));
//                    }
//                    String kits = null;
//                    if (spawns.getAttributeValue("kit") != null)
//                        kits = spawns.getAttributeValue("kit");
//                    if (element.getAttributeValue("kit") != null)
//                        kits = element.getAttributeValue("kit");
//                    if (spawn.getAttributeValue("kit") != null)
//                        kits = spawn.getAttributeValue("kit");
//                    Kit kit = null;
//                    for (Kit kitModule : match.getModules().getModules(Kit.class)) {
//                        if (kitModule.getName().equals(kits)) kit = kitModule;
//                    }
//                    results.add(new SpawnModule(team, regions, kit, true, true));
//                }
//            }
//        }
//        return results;
    }

//    private List<Pair<RegionModule, Vector>> getRegions(JsonObject json){
//        List<Pair<RegionModule, Vector>> regions = new ArrayList<>();
////        for (Element region : element.getChildren()) {
////            RegionModule current = RegionModuleBuilder.getRegion(region);
////            Location location = new Location(GameHandler.getGameHandler().getMatchWorld(), 0, 0, 0);
////            if (region.getParentElement().getParentElement().getParentElement().getAttributeValue("yaw") != null)
////                location.setYaw(Float.parseFloat(region.getParentElement().getParentElement().getParentElement().getAttributeValue("yaw")));
////            if (region.getParentElement().getParentElement().getAttributeValue("yaw") != null)
////                location.setYaw(Float.parseFloat(region.getParentElement().getParentElement().getAttributeValue("yaw")));
////            if (region.getParentElement().getAttributeValue("yaw") != null)
////                location.setYaw(Float.parseFloat(region.getParentElement().getAttributeValue("yaw")));
////            if (region.getAttributeValue("yaw") != null)
////                location.setYaw(Float.parseFloat(region.getAttributeValue("yaw")));
////            if (region.getParentElement().getParentElement().getParentElement().getAttributeValue("angle") != null)
////                location.setDirection(parseAngle(region.getParentElement().getParentElement().getParentElement().getAttributeValue("angle")).subtract(current.getCenterBlock().getVector()));
////            if (region.getParentElement().getParentElement().getAttributeValue("angle") != null)
////                location.setDirection(parseAngle(region.getParentElement().getParentElement().getAttributeValue("angle")).subtract(current.getCenterBlock().getVector()));
////            if (region.getParentElement().getAttributeValue("angle") != null)
////                location.setDirection(parseAngle(region.getParentElement().getAttributeValue("angle")).subtract(current.getCenterBlock().getVector()));
////            regions.add(new ImmutablePair<>(current, location.getDirection()));
////        }
//
//
//        Location location = new Location(GameHandler.getGameHandler().getMatchWorld(), 0, 0, 0);
//        if (json.has("yaw")) {
//            location.setY(json.get("yaw").getAsInt());
//        }
//        regions.add(new ImmutablePair<RegionModule, Vector>(new BlockRegion(team.getName() + "-spawn", json.get("coords").getAsString())));
//
//        return regions;
//    }

    private Vector parseAngle(String string) {
        String[] loc = string.split(",");
        return new Vector(Double.parseDouble(loc[0].trim()), Double.parseDouble(loc[1].trim()), Double.parseDouble(loc[2].trim()));
    }
}
