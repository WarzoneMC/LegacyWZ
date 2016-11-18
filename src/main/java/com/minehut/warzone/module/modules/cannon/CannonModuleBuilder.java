package com.minehut.warzone.module.modules.cannon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.modules.regions.RegionModuleBuilder;
import com.minehut.warzone.module.modules.regions.type.BlockRegion;
import com.minehut.warzone.module.modules.timeNotifications.TimeNotifications;

import java.util.ArrayList;

/*
 * Created by lucas on 3/18/16.
 */
public class CannonModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<CannonModule> load(Match match) {
        ModuleCollection<CannonModule> modules = new ModuleCollection<>();

        if (match.getJson().has("cannons")) {
            for (JsonElement jsonElement : match.getJson().getAsJsonArray("cannons")) {
                JsonObject json = jsonElement.getAsJsonObject();
                BlockRegion blockRegion = (BlockRegion) RegionModuleBuilder.defineJsonRegion(json.getAsJsonObject("region"));

                int fireRate = 40;
                if (json.has("fireRate")) {
                    fireRate = json.get("fireRate").getAsInt();
                }

                float range = 1;
                if (json.has("range")) {
                    range = json.get("range").getAsFloat();
                }

                modules.add(new CannonModule(blockRegion, fireRate, range));
            }
        }

        return modules;
    }

}
