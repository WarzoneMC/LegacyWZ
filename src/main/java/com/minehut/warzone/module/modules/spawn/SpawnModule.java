package com.minehut.warzone.module.modules.spawn;

import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.regions.RegionModule;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class SpawnModule implements Module {

    private final TeamModule team;
    private final List<Pair<RegionModule, Vector>> regions;
    private final boolean safe;
    private final boolean sequential;
    //private final Filter filter;
    private int position;

    public SpawnModule(TeamModule team, List<Pair<RegionModule, Vector>> regions, boolean safe, boolean sequential) {
        this.team = team;
        this.regions = regions;
        this.safe = safe;
        this.sequential = sequential;
        this.position = 0;
    }

    @Override
    public void unload() {
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if(this.safe) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                for (Pair<RegionModule, Vector> p : this.regions) {
                    if (p.getKey().contains(player.getLocation())) {
                        TeamModule team = Teams.getTeamByPlayer(player).orNull();
                        if (team != null && team == this.team) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    public TeamModule getTeam() {
        return team;
    }

    public List<Pair<RegionModule, Vector>> getRegions() {
        return regions;
    }

    public boolean isSafe() {
        return safe;
    }

    public boolean isSequential() {
        return sequential;
    }

    public Location getLocation() {
        if (sequential) {
            Location location = regions.get(position).getLeft().getCenterBlock().getLocation();
            location.setDirection(regions.get(position).getRight());
            position++;
            if (position == regions.size()) position = 0;
            return location;
        } else {
            Random random = new Random();
            int use = random.nextInt(regions.size());
            Location location = regions.get(use).getLeft().getCenterBlock().getLocation();
            location.setDirection(regions.get(use).getRight());
            return location;
        }
    }
}
