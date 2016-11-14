package com.minehut.warzone.module.modules.portal;

import com.google.gson.JsonObject;
import com.minehut.cloud.bukkit.util.chat.S;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.module.modules.regions.RegionModule;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

/*
 * Created by lucas on 3/18/16.
 */
public class PortalModule implements Module {

    private RegionModule region, destination;
    private JsonObject json;

    private TeamModule team;

    protected PortalModule(RegionModule region, RegionModule destination, JsonObject json) {
        this.region = region;
        this.destination = destination;
        this.json = json;

        if (json.has("teamId")) {
            team = Teams.getTeamById(json.get("teamId").getAsString()).orNull();
        } else if (json.has("teamName")) {
            team = Teams.getTeamByName(json.get("teamName").getAsString()).orNull();
        }

    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (region.contains(event.getTo()) && !region.contains(event.getFrom())) {
            if (team == null || team.contains(player)) {
                if (destination != null) {
                    player.teleport(destination.getCenterBlock().getLocation());
                    S.playSound(player, Sound.ENTITY_ENDERMEN_TELEPORT);
                }
            }
        }
    }


    public JsonObject getJson() { return json; }

}
