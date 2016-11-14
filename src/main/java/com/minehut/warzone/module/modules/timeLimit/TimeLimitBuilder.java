package com.minehut.warzone.module.modules.timeLimit;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.GameObjective;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.module.ModuleBuilder;
import com.minehut.warzone.util.Strings;

public class TimeLimitBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TimeLimit> load(Match match) {
        ModuleCollection<TimeLimit> results = new ModuleCollection<>();
        int timeLimit = 0;
        TimeLimit.Result result = null;
        TeamModule team = null;

        if (match.getJson().has("time")) {
            timeLimit = Strings.timeStringToSeconds(match.getJson().get("time").getAsString());
        } else {
            timeLimit = 60 * 20;
        }
        result = TimeLimit.Result.MOST_OBJECTIVES;


//        for (Element time : match.getDocument().getRootElement().getChildren("time")) {
//            timeLimit = Strings.timeStringToSeconds(time.getText());
//            result = TimeLimit.Result.TIE;
//            if (time.getAttributeValue("result") != null) {
//                if (time.getAttributeValue("result").equalsIgnoreCase("objectives")) {
//                    result = TimeLimit.Result.MOST_OBJECTIVES;
//                } else if (time.getAttributeValue("result").equalsIgnoreCase("tie")) {
//                    result = TimeLimit.Result.TIE;
//                } else {
//                    result = TimeLimit.Result.TEAM;
//                    team = Teams.getTeamById(time.getAttributeValue("result")).orNull();
//                }
//            }
//        }
//        for (Element score : match.getDocument().getRootElement().getChildren("score")) {
//            if (timeLimit <= 0) {
//                result = TimeLimit.Result.HIGHEST_SCORE;
//            }
//            if (score.getChild("time") != null) {
//                timeLimit = Strings.timeStringToSeconds(score.getChild("time").getText());
//                result = TimeLimit.Result.HIGHEST_SCORE;
//            }
//        }
//        for (Element blitz : match.getDocument().getRootElement().getChildren("blitz")) {
//            if (timeLimit <= 0) {
//                result = TimeLimit.Result.MOST_PLAYERS;
//            }
//            if (blitz.getChild("time") != null) {
//                timeLimit = Strings.timeStringToSeconds(blitz.getChild("time").getText());
//                result = TimeLimit.Result.MOST_PLAYERS;
//            }
//        }


        if (timeLimit < 0) {
            timeLimit = 0;
        }
        if (result == null && GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class).size() > 0) {
            result = TimeLimit.Result.MOST_OBJECTIVES;
        }
        results.add(new TimeLimit(timeLimit, result, team));
        return results;
    }

}
