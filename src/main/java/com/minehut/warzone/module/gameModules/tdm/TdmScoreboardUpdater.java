package com.minehut.warzone.module.gameModules.tdm;

import com.minehut.cloud.bukkit.util.scoreboard.SimpleScoreboard;
import com.minehut.cloud.core.util.ChatColor;
import com.minehut.warzone.event.CardinalDeathEvent;
import com.minehut.warzone.module.modules.scoreboard.ScoreboardUpdater;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.util.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.HashMap;

/**
 * Created by luke on 4/1/16.
 */
public class TdmScoreboardUpdater implements ScoreboardUpdater {

    TdmGameModule tdmGameModule;
    private SimpleScoreboard simpleScoreboard;

    private HashMap<String, Integer> scoreLines = new HashMap<>();

    @Override
    public void initialSetup(TeamModule teamModule, SimpleScoreboard simpleScoreboard) {
        this.simpleScoreboard = simpleScoreboard;
        this.tdmGameModule = GameHandler.getGameHandler().getMatch().getModules().getModule(TdmGameModule.class);

        simpleScoreboard.setTitle(ChatColor.AQUA.toString() + tdmGameModule.getMax() + " Kills Wins");

        // -1 is to not count spectate team.
        // +3 is for the other 3 lines
        int i = (Teams.getTeams().size() - 1) + 3;

        simpleScoreboard.add(" ", i);
        i--;

        for (TeamModule teams : Teams.getTeams()) {
            if (!teams.isObserver()) {
                simpleScoreboard.add(getTeamScoreString(teams, 0), i);
                scoreLines.put(teams.getId(), i);
                i--;
            }
        }

        simpleScoreboard.add("  ", i);
        i--;

        simpleScoreboard.add(ChatColor.YELLOW + "minehut.com", i);

        simpleScoreboard.update();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (event.getPlayer() != null && event.getKiller() != null) {
            TeamModule team = Teams.getTeamByPlayer(event.getKiller()).orNull();
            if (team != null && !team.isObserver()) {
                this.simpleScoreboard.remove(this.scoreLines.get(team.getId()));
                this.simpleScoreboard.add(getTeamScoreString(team, this.tdmGameModule.getScore(team)), this.scoreLines.get(team.getId()));
                this.simpleScoreboard.update();
            }
        }
    }

    private String getTeamScoreString(TeamModule teamModule, int score) {
        return teamModule.getColor() + teamModule.getName() + ": " + ChatColor.WHITE + score;
    }

}
