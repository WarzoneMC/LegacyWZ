package com.minehut.warzone.module.modules.matchTimer;

import com.minehut.warzone.GameHandler;
import com.minehut.warzone.event.MatchEndEvent;
import com.minehut.warzone.event.MatchStartEvent;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.match.MatchState;
import com.minehut.warzone.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class MatchTimer implements Module {

    private long startTime;
    private double endTime;

    protected MatchTimer() {
        this.endTime = 0;
    }

    public static double getTimeInSeconds() {
        Match match = GameHandler.getGameHandler().getMatch();
        if (match.isRunning()) {
            return ((double) System.currentTimeMillis() - (GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTimer.class)).getTime()) / 1000.0;
        }
        if (match.getState().equals(MatchState.ENDED) || match.getState().equals(MatchState.CYCLING)) {
            return GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTimer.class).getEndTime();
        }
        return 0;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        this.startTime = System.currentTimeMillis();
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        this.endTime = ((double) System.currentTimeMillis() - (GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTimer.class)).getTime()) / 1000.0;
    }

    /**
     * @return The current time stored in the module.
     */
    public long getTime() {
        return startTime;
    }

    public double getEndTime() {
        return endTime;
    }
}
