package com.minehut.warzone.module.modules.wools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.cloud.core.util.ChatColor;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.BuilderData;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleLoadTime;
import com.minehut.warzone.module.modules.appliedRegion.type.BlockBreakRegion;
import com.minehut.warzone.module.modules.appliedRegion.type.BlockPlaceRegion;
import com.minehut.warzone.module.modules.appliedRegion.type.EnterRegion;
import com.minehut.warzone.module.modules.filter.type.TeamFilter;
import com.minehut.warzone.module.modules.regions.type.BlockRegion;
import com.minehut.warzone.module.modules.regions.type.CuboidRegion;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Parser;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.util.VectorUtil;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.modules.regions.RegionModule;
import org.bukkit.DyeColor;
import org.bukkit.util.Vector;

import java.util.Iterator;

@BuilderData(load = ModuleLoadTime.EARLIER)
public class WoolObjectiveBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<WoolObjective> load(Match match) {
        ModuleCollection result = new ModuleCollection<>();

        if (match.getJson().has("wools")) {
            JsonArray woolsArray = match.getJson().get("wools").getAsJsonArray();
            if (woolsArray != null) {
                Iterator<JsonElement> it = woolsArray.iterator();
                while (it.hasNext()) {

                    JsonObject woolTeamJson = it.next().getAsJsonObject();
                    TeamModule team;
                    try {
                        team = Teams.getTeamById(woolTeamJson.get("team").getAsString()).orNull();
                    } catch (NullPointerException e) {
                        team = Teams.getTeamById((woolTeamJson.get("team").getAsString())).orNull();
                    }

                    JsonArray actualWoolsArray = woolTeamJson.get("wools").getAsJsonArray();
                    Iterator<JsonElement> actualWoolsIterator = actualWoolsArray.iterator();
                    while (actualWoolsIterator.hasNext()) {
                        JsonObject woolJson = actualWoolsIterator.next().getAsJsonObject();

                        DyeColor color = Parser.parseDyeColor(woolJson.get("color").getAsString());
                        String name = color == null ? "Wool" : color.name() + " Wool";
                        if (woolJson.has("name")) {
                            name = woolJson.get("name").getAsString();
                        }
                        String id = woolJson.get("color").getAsString();
                        boolean craftable = false; //todo
                        boolean show = true;
                        boolean required = true;

                        Vector location = VectorUtil.fromString(woolJson.get("block").getAsString());

                        BlockRegion place = new BlockRegion(null, VectorUtil.fromString(woolJson.get("block").getAsString()));

                        result.add(new WoolObjective(team, name, id, color, place, craftable, show, required, location));

                        //add the region for the wool room
                        if (match.getJson().has("regions")) {
                            Iterator<JsonElement> it2 = match.getJson().get("regions").getAsJsonArray().iterator();
                            while (it2.hasNext()) {
                                JsonObject roomJson = it2.next().getAsJsonObject();
                                String regionName = woolJson.get("color").getAsString().toLowerCase().replace(" ", "-") + "-room";
                                if (roomJson.get("name").getAsString().equalsIgnoreCase(regionName)) {
                                    RegionModule room = new CuboidRegion(regionName, roomJson);
                                    result.add(new BlockBreakRegion(room, new TeamFilter(team), ChatColor.RED + "You cannot modify your Wool Room."));
                                    result.add(new BlockPlaceRegion(room, new TeamFilter(team), ChatColor.RED + "You cannot modify your Wool Room."));


                                    result.add(new EnterRegion(room, new TeamFilter(team), ChatColor.RED + "You cannot enter your Wool Room."));
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
