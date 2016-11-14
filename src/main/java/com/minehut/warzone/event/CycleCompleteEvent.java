package com.minehut.warzone.event;

import com.minehut.warzone.match.Match;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CycleCompleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Match match;

    public CycleCompleteEvent(Match match) {
        this.match = match;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Match getMatch() {
        return match;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
