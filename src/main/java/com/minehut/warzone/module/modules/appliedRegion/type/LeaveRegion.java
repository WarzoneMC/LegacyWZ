package com.minehut.warzone.module.modules.appliedRegion.type;

import com.minehut.warzone.module.modules.filter.FilterState;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.module.modules.appliedRegion.AppliedRegion;
import com.minehut.warzone.module.modules.filter.FilterModule;
import com.minehut.warzone.module.modules.regions.RegionModule;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class LeaveRegion extends AppliedRegion {

    public LeaveRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!region.contains(event.getTo().toVector())
                && region.contains(event.getFrom().toVector())
                && filter.evaluate(event.getPlayer(), event).equals(FilterState.DENY)
                && (!Teams.getTeamByPlayer(event.getPlayer()).isPresent() || !Teams.getTeamByPlayer(event.getPlayer()).get().isObserver())
                && GameHandler.getGameHandler().getMatch().isRunning()) {
            event.setTo(event.getFrom());
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }
}
