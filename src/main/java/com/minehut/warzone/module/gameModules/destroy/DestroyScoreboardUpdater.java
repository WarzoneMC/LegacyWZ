package com.minehut.warzone.module.gameModules.destroy;

import com.minehut.warzone.GameHandler;
import com.minehut.warzone.event.objective.ObjectiveCompleteEvent;
import com.minehut.warzone.module.modules.wools.WoolCoreObjective;
import com.minehut.warzone.module.modules.scoreboard.ScoreboardUpdater;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.util.scoreboard.SimpleScoreboard;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

/**
 * Created by luke on 4/1/16.
 */
public class DestroyScoreboardUpdater implements ScoreboardUpdater {

    private SimpleScoreboard simpleScoreboard;

    @Override
    public void initialSetup(TeamModule teamModule, SimpleScoreboard simpleScoreboard) {
        this.simpleScoreboard = simpleScoreboard;

        TeamModule redTeam = Teams.getTeamById("red").orNull();

        for (WoolCoreObjective woolCoreObjective : GameHandler.getGameHandler().getMatch().getModules().getModules(WoolCoreObjective.class)) {
            if (woolCoreObjective.getTeam() == redTeam) {
                simpleScoreboard.add(ChatColor.RED + "Red " + ChatColor.WHITE + getWoolPercentage(woolCoreObjective) + "%", 4);
            } else {
                simpleScoreboard.add(ChatColor.BLUE + "Blue " + ChatColor.WHITE + getWoolPercentage(woolCoreObjective) + "%", 3);
            }
        }

        simpleScoreboard.add(" ", 5);
        simpleScoreboard.add("  ", 2);

        simpleScoreboard.add(ChatColor.YELLOW + "minehut.com", 1);

        simpleScoreboard.update();
    }

    @EventHandler
    public void onObjectComplete(ObjectiveCompleteEvent event) {
        if (event.getObjective() instanceof WoolCoreObjective) {
            if (event.getObjective().getTeam() == Teams.getTeamById("blue").orNull()) {
                simpleScoreboard.remove(4);
                simpleScoreboard.add(ChatColor.RED + "Red " + ChatColor.WHITE + getWoolPercentage((WoolCoreObjective) event.getObjective()) + "%", 4);
            } else {
                simpleScoreboard.remove(3);
                simpleScoreboard.add(ChatColor.BLUE + "Blue " + ChatColor.WHITE + getWoolPercentage((WoolCoreObjective) event.getObjective()) + "%", 3);
            }
            simpleScoreboard.update();
        }
    }

    private int getWoolPercentage(WoolCoreObjective woolCoreObjective) {
        int health = woolCoreObjective.getHealth() * 100;
        int max = woolCoreObjective.getMaxHealth();

        int result = health / max;
        return result;
    }

}
