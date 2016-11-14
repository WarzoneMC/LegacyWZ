package com.minehut.warzone.module.modules.scoreboard;

import com.minehut.warzone.match.GameType;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.ModuleLoadTime;
import com.minehut.warzone.module.gameModules.blitz.BlitzScoreboardUpdater;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.module.BuilderData;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.module.gameModules.ctw.CtwScoreboardUpdater;
import com.minehut.warzone.module.gameModules.destroy.DestroyScoreboardUpdater;
import com.minehut.warzone.module.gameModules.elimination.EliminationScoreboardUpdater;
import com.minehut.warzone.module.gameModules.tdm.TdmScoreboardUpdater;
import com.minehut.warzone.util.Teams;

@BuilderData(load = ModuleLoadTime.LATEST)
public class ScoreboardModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ScoreboardModule> load(Match match) {
        ModuleCollection<ScoreboardModule> results = new ModuleCollection<>();

        for (TeamModule teamModule : Teams.getTeams()) {
            ScoreboardModule scoreboardModule = new ScoreboardModule(teamModule);

            if (match.getGameType() == GameType.DTW) {
                scoreboardModule.setScoreboardUpdater(new DestroyScoreboardUpdater());
            }

            else if (match.getGameType() == GameType.CTW) {
                scoreboardModule.setScoreboardUpdater(new CtwScoreboardUpdater());
            }

            else if (match.getGameType() == GameType.TDM) {
                scoreboardModule.setScoreboardUpdater(new TdmScoreboardUpdater());
            }

            else if (match.getGameType() == GameType.ELIMINATION) {
                scoreboardModule.setScoreboardUpdater(new EliminationScoreboardUpdater());
            }

            else if (match.getGameType() == GameType.BLITZ) {
                scoreboardModule.setScoreboardUpdater(new BlitzScoreboardUpdater());
            }

            results.add(scoreboardModule);
        }

        return results;
    }

}
