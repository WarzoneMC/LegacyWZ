package com.minehut.warzone.module.modules.scoreboard;

import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.scoreboard.SimpleScoreboard;
import org.bukkit.event.Listener;

/**
 * Created by luke on 4/1/16.
 */
public interface ScoreboardUpdater extends Listener {

    public void initialSetup(TeamModule teamModule, SimpleScoreboard simpleScoreboard);

}
