package com.minehut.warzone.module.modules.gameComplete;

import com.minehut.warzone.module.modules.matchTimer.MatchTimer;
import com.minehut.warzone.module.modules.timeLimit.TimeLimit;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.event.PlayerChangeTeamEvent;
import com.minehut.warzone.event.objective.ObjectiveCompleteEvent;
import com.minehut.warzone.match.GameType;
import com.minehut.warzone.match.MatchState;
import com.minehut.warzone.module.GameObjective;
import com.minehut.warzone.module.TaskedModule;
import com.minehut.warzone.util.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

public class GameComplete implements TaskedModule {

    protected GameComplete() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        for (TeamModule team : Teams.getTeams()) {
            if (team.isObserver()) {
                continue;
            }
            boolean skipTeam = false;

            for (GameObjective condition : Teams.getRequiredObjectives(team)) {
                if (!condition.isComplete()) {
                    skipTeam = true;
                }
            }

            if (skipTeam) {
                continue;
            }

            GameHandler.getGameHandler().getMatch().end(team);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {

    }

    @Override
    public void run() {
        if (TimeLimit.getMatchTimeLimit() != 0 && MatchTimer.getTimeInSeconds() >= TimeLimit.getMatchTimeLimit() && GameHandler.getGameHandler().getMatch().getState() == MatchState.PLAYING) {

            if (GameHandler.getGameHandler().getMatch().getGameType() == GameType.INFECTED) {
                GameHandler.getGameHandler().getMatch().end(Teams.getTeamById("humans").orNull());
            }
            else {
                GameHandler.getGameHandler().getMatch().end(TimeLimit.getMatchWinner());
            }
        }
    }
}
