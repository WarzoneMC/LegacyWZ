package com.minehut.warzone.event;

import com.google.common.base.Optional;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangeTeamEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final boolean forced;
    private Optional<TeamModule> newTeam;
    private Optional<TeamModule> oldTeam;
    private boolean cancelled;
    private String cancelMessage;

    public PlayerChangeTeamEvent(Player player, boolean forced, Optional<TeamModule> newTeam, Optional<TeamModule> oldTeam) {
        this.player = player;
        this.forced = forced;
        this.newTeam = newTeam;
        this.oldTeam = oldTeam;
        cancelMessage = ChatUtil.getWarningMessage("Unable to join " + newTeam.get().getName() + " at this time.");
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Optional<TeamModule> getNewTeam() {
        return newTeam;
    }

    public Optional<TeamModule> getOldTeam() {
        return oldTeam;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    public void setCancelMessage(String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }
}
