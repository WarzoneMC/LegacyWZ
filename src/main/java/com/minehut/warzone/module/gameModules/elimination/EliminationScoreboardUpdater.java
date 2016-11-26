package com.minehut.warzone.module.gameModules.elimination;

import com.minehut.warzone.Warzone;
import com.minehut.warzone.event.CardinalDeathEvent;
import com.minehut.warzone.event.PlayerChangeTeamEvent;
import com.minehut.warzone.module.modules.scoreboard.ScoreboardUpdater;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.match.MatchState;
import com.minehut.warzone.util.scoreboard.SimpleScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

/**
 * Created by luke on 4/1/16.
 */
public class EliminationScoreboardUpdater implements ScoreboardUpdater {

    private SimpleScoreboard simpleScoreboard;

    private HashMap<String, Integer> scoreLines = new HashMap<>();

    @Override
    public void initialSetup(TeamModule teamModule, SimpleScoreboard simpleScoreboard) {
        this.simpleScoreboard = simpleScoreboard;

        simpleScoreboard.setTitle(ChatColor.AQUA + "Elimination");

        // -1 is to not count spectate team.
        // +3 is for the other 3 lines
        int i = (Teams.getTeams().size() - 1) + 3;

        simpleScoreboard.add(" ", i);
        i--;

        for (TeamModule teams : Teams.getTeams()) {
            if (!teams.isObserver()) {
                simpleScoreboard.add(getTeamScoreString(teams), i);
                scoreLines.put(teams.getId(), i);
                i--;
            }
        }

        simpleScoreboard.add("  ", i);
        i--;

        simpleScoreboard.add(ChatColor.YELLOW + "minehut.com", i);

        simpleScoreboard.update();
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        if (GameHandler.getGameHandler().getMatch().getState() != MatchState.PLAYING) {
            Bukkit.getScheduler().runTask(Warzone.getInstance(), new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(CardinalDeathEvent event) {
        Bukkit.getScheduler().runTask(Warzone.getInstance(), new Runnable() {
            @Override
            public void run() {
                update();
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTask(Warzone.getInstance(), new Runnable() {
            @Override
            public void run() {
                update();
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerKickEvent event) {
        Bukkit.getScheduler().runTask(Warzone.getInstance(), new Runnable() {
            @Override
            public void run() {
                update();
            }
        });
    }

    private void update() {
        for (TeamModule teamModule : Teams.getTeams()) {
            if (!teamModule.isObserver()) {
                this.simpleScoreboard.remove(this.scoreLines.get(teamModule.getId()));
                this.simpleScoreboard.add(getTeamScoreString(teamModule), this.scoreLines.get(teamModule.getId()));
                this.simpleScoreboard.update();
            }
        }
    }

    private String getTeamScoreString(TeamModule teamModule) {
        return teamModule.getColor() + teamModule.getName() + ": " + ChatColor.WHITE + teamModule.size();
    }

}
