package com.minehut.warzone.module.modules.portal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.modules.regions.RegionModuleBuilder;

/*
 * Created by lucas on 3/18/16.
 */
public class PortalModuleBuilder implements ModuleBuilder {

    public static PortalModule makePortal(JsonObject object) {
        PortalModule portal = new PortalModule(RegionModuleBuilder.defineJsonRegion(object.getAsJsonObject("region")),
                RegionModuleBuilder.defineJsonRegion(object.getAsJsonObject("destination")), object);
        Warzone.getInstance().getGameHandler().getMatch().getModules().add(portal);
        return portal;
    }

    @Override
    public ModuleCollection<PortalModule> load(Match match) {
        if (match.getJson().has("portals")) {
            for (JsonElement jsonElement : match.getJson().getAsJsonArray("portals")) {
                JsonObject json = jsonElement.getAsJsonObject();
                makePortal(json);
            }
        }
        return Warzone.getInstance().getGameHandler().getMatch().getModules().getModules(PortalModule.class);
    }

}
