package com.minehut.warzone.module.modules.appliedRegion.type;

import com.google.common.base.Optional;
import com.minehut.warzone.module.modules.filter.FilterState;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.module.modules.appliedRegion.AppliedRegion;
import com.minehut.warzone.module.modules.filter.FilterModule;
import com.minehut.warzone.module.modules.regions.RegionModule;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class EnterRegion extends AppliedRegion {

    public EnterRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(event.getPlayer());
        if (region.contains(event.getTo().toVector())
                && !region.contains(event.getFrom().toVector())
                && filter.evaluate(event.getPlayer(), event).equals(FilterState.DENY)
                && (!team.isPresent() || (team.isPresent() && !team.get().isObserver()))
                && GameHandler.getGameHandler().getMatch().isRunning()) {
            event.setTo(event.getFrom());
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }
}
