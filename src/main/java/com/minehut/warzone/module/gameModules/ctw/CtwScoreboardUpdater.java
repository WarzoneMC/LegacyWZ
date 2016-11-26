package com.minehut.warzone.module.gameModules.ctw;

import com.minehut.warzone.event.objective.ObjectiveCompleteEvent;
import com.minehut.warzone.event.objective.ObjectiveTouchEvent;
import com.minehut.warzone.module.modules.wools.WoolObjective;
import com.minehut.warzone.module.modules.scoreboard.ScoreboardUpdater;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.util.MiscUtil;
import com.minehut.warzone.util.scoreboard.SimpleScoreboard;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

import java.util.HashMap;

/**
 * Created by luke on 4/1/16.
 */
public class CtwScoreboardUpdater implements ScoreboardUpdater {

    private SimpleScoreboard simpleScoreboard;

    private HashMap<WoolObjective, Integer> woolLines = new HashMap<>();

    @Override
    public void initialSetup(TeamModule teamModule, SimpleScoreboard simpleScoreboard) {
        this.simpleScoreboard = simpleScoreboard;

        int i = Teams.getTeams().size() + GameHandler.getGameHandler().getMatch().getModules().getModules(WoolObjective.class).size() + 2;
        for (TeamModule teams : Teams.getTeams()) {
            if(teams.isObserver()) continue;

            simpleScoreboard.add(teams.getColor() + teams.getName(), i);
            i--;

            for (WoolObjective woolObjective : GameHandler.getGameHandler().getMatch().getModules().getModules(WoolObjective.class)) {
                if (woolObjective.getTeam() == teams) {
                    simpleScoreboard.add(getWoolObjectDisplayString(woolObjective), i);
                    woolLines.put(woolObjective, i);
                    i--;
                }
            }

            simpleScoreboard.add(StringUtils.repeat(" ", i), i);
            i--;
        }

        simpleScoreboard.add(ChatColor.YELLOW + "minehut.com", i);
        simpleScoreboard.update();
    }

    private String getWoolObjectDisplayString(WoolObjective woolObjective) {
        String box = "";
        if (woolObjective.isComplete()) {
            box = MiscUtil.convertDyeColorToChatColor(woolObjective.getColor()) + "\u2B1B";
        }
        else if (woolObjective.isTouched()) {
            box = MiscUtil.convertDyeColorToChatColor(woolObjective.getColor()) + "\u2592";
        }
        else {
            box = MiscUtil.convertDyeColorToChatColor(woolObjective.getColor()) + "\u2B1C";
        }

        String s = box + "  " + ChatColor.WHITE + StringUtils.capitalize(woolObjective.getColor().toString().toLowerCase() + " Wool");
        return s;
    }

    @EventHandler
    public void onObjectComplete(ObjectiveCompleteEvent event) {
        if (event.getObjective() instanceof WoolObjective) {
            updateObjective((WoolObjective) event.getObjective());
        }
    }

    @EventHandler
    public void onObjectiveTouch(ObjectiveTouchEvent event) {
        if (event.getObjective() instanceof WoolObjective) {
            updateObjective((WoolObjective) event.getObjective());
        }
    }

    public void updateObjective(WoolObjective woolObjective) {
        simpleScoreboard.remove(this.woolLines.get(woolObjective));
        simpleScoreboard.add(getWoolObjectDisplayString((WoolObjective) woolObjective), this.woolLines.get(woolObjective));

        simpleScoreboard.update();
    }

}
