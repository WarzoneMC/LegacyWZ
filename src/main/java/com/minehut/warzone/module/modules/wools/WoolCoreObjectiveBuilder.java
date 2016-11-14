package com.minehut.warzone.module.modules.wools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.modules.appliedRegion.type.BlockPlaceRegion;
import com.minehut.warzone.module.modules.regions.type.CylinderRegion;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Parser;
import com.minehut.warzone.util.VectorUtil;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.BuilderData;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleLoadTime;
import com.minehut.warzone.module.modules.appliedRegion.type.BlockBreakRegion;
import com.minehut.warzone.module.modules.filter.type.TeamFilter;
import com.minehut.warzone.util.Teams;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.util.Vector;

import java.util.Iterator;

@BuilderData(load = ModuleLoadTime.EARLIER)
public class WoolCoreObjectiveBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<WoolObjective> load(Match match) {
        ModuleCollection result = new ModuleCollection<>();

        if (match.getJson().has("cores")) {
            JsonArray woolsArray = match.getJson().get("cores").getAsJsonArray();
            if (woolsArray != null) {
                Iterator<JsonElement> it = woolsArray.iterator();
                while (it.hasNext()) {
                    JsonObject json = it.next().getAsJsonObject();
                    TeamModule team;
                    try {
                        team = Teams.getTeamById(json.get("team").getAsString()).orNull();
                    } catch (NullPointerException e) {
                        team = Teams.getTeamById((json.get("team").getAsString())).orNull();
                    }
                    DyeColor color = Parser.parseDyeColor(json.get("color").getAsString());
                    String name = ChatColor.valueOf(json.get("color").getAsString().replace(" ", "_").toUpperCase()) + WordUtils.capitalize(json.get("color").getAsString());
                    String id = json.get("color").getAsString();

                    if (json.has("regions")) {
                        Iterator<JsonElement> regionIt = json.getAsJsonArray("regions").iterator();
                        while (regionIt.hasNext()) {
                            JsonObject regionJson = regionIt.next().getAsJsonObject();

                            int radius = 7;
                            if (regionJson.has("radius")) {
                                radius = regionJson.get("radius").getAsInt();
                            }

                            int height = 20;
                            if (regionJson.has("height")) {
                                height = regionJson.get("height").getAsInt();
                            }

                            CylinderRegion region = null;
                            if (regionJson.has("base")) {
                                Vector v = VectorUtil.fromString(regionJson.get("base").getAsString());
                                v = new Vector(v.getX(), v.getY() - height, v.getZ());
                                region = new CylinderRegion(name, v, radius, height * 2);

                                result.add(new BlockBreakRegion(region, new TeamFilter(team), "You cannot modify the Wool Cores."));

                                for (TeamModule teamModule : Teams.getTeams()) {
                                    if (!teamModule.isObserver()) {
                                        result.add(new BlockPlaceRegion(region, new TeamFilter(teamModule), "You cannot modify the Wool Cores."));
                                    }
                                }
                            }
                        }
                    }

                    int health = 10;
                    if (json.has("health")) {
                        health = json.get("health").getAsInt();
                    }


                    System.out.println("Adding WoolCoreObjective with health " + health);
                    result.add(new WoolCoreObjective(team, name, id, null, color, health));
                }
            }
        }

        return result;
    }

}
