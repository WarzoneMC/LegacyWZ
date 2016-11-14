package com.minehut.warzone.module.gameModules.tdm;

import com.minehut.warzone.event.CardinalDeathEvent;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.util.Teams;
import org.bukkit.event.EventHandler;

import java.util.HashMap;

/**
 * Created by luke on 4/1/16.
 */
public class TdmGameModule implements Module {
    private HashMap<String, Integer> scores = new HashMap<>();
    private int max = 100;

    public TdmGameModule() {

        for (TeamModule teamModule : Teams.getTeams()) {
            if (!teamModule.isObserver()) {
                System.out.println("inserted " + teamModule.getName());
                scores.put(teamModule.getId(), 0);
            }
        }
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (event.getKiller() != null && event.getPlayer() != null) {
            TeamModule teamModule = Teams.getTeamByPlayer(event.getKiller()).orNull();
            if (teamModule != null && !teamModule.isObserver()) {
                int updatedScore = this.scores.get(teamModule.getId()) + 1;
                this.scores.put(teamModule.getId(), updatedScore);

                if (updatedScore >= max) {
                    GameHandler.getGameHandler().getMatch().end(teamModule);
                }
            }
        }
    }

    public int getScore(TeamModule teamModule) {
        return this.scores.get(teamModule.getId());
    }

    public int getMax() {
        return max;
    }

    @Override
    public void unload() {

    }
}
