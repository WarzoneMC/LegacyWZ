package com.minehut.warzone.util;

import com.google.common.base.Optional;
import com.minehut.cloud.bukkit.util.MongoPlayer;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.GameObjective;
import com.minehut.warzone.module.ModuleCollection;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.module.modules.wools.WoolCoreObjective;
import com.minehut.warzone.module.modules.wools.WoolObjective;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Teams {

    public static Optional<TeamModule> getTeamWithFewestPlayers(Match match) {
        TeamModule result = null;
        double percent = Double.POSITIVE_INFINITY;
        for (TeamModule team : getTeams()) {
            if (!team.isObserver() && team.isAllowJoin() && (team.size() / (double) team.getMax()) < percent) {
                result = team;
                percent = team.size() / (double) team.getMax();
            }
        }
        if (result == null) return Optional.absent();
        else return Optional.of(result);
    }

    public static Optional<TeamModule> getObservers() {
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.isObserver()) {
                return Optional.of(team);
            }
        }
        return Optional.absent();
    }

    public static Optional<TeamModule> getTeamByName(String name) {
        if (name == null) return null;
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getName().replaceAll(" ", "").toLowerCase().startsWith(name.replaceAll(" ", "").toLowerCase())) {
                return Optional.of(team);
            }
        }
        return Optional.absent();
    }

    public static Optional<TeamModule> getTeamById(String id) {
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getId().replaceAll(" ", "").equalsIgnoreCase(id.replaceAll(" ", ""))) {
                return Optional.of(team);
            }
        }
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getId().replaceAll(" ", "").toLowerCase().startsWith(id.replaceAll(" ", "").toLowerCase())) {
                return Optional.of(team);
            }
        }
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getId().replaceAll(" ", "-").equalsIgnoreCase(id.replaceAll(" ", "-"))) {
                return Optional.of(team);
            }
        }
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getId().replaceAll(" ", "-").toLowerCase().startsWith(id.replaceAll(" ", "-").toLowerCase())) {
                return Optional.of(team);
            }
        }
        return Optional.absent();
    }

    public static Optional<TeamModule> getTeamByPlayer(Player player) {
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.contains(player)) {
                return Optional.of(team);
            }
        }
        return Optional.absent();
    }

    public static ModuleCollection<TeamModule> getTeams() {
        return GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class);
    }

    public static ModuleCollection<GameObjective> getObjectives(TeamModule team) {
        ModuleCollection<GameObjective> objectives = new ModuleCollection<>();
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (objective instanceof WoolObjective || objective instanceof WoolCoreObjective) {
                if (objective.getTeam() == team) {
                    objectives.add(objective);
                }
            }
        }
        return objectives;
    }

    public static ModuleCollection<GameObjective> getShownObjectives(TeamModule team) {
        ModuleCollection<GameObjective> objectives = new ModuleCollection<>();
        for (GameObjective objective : getObjectives(team)) {
            if (objective.showOnScoreboard()) {
                objectives.add(objective);
            }
        }
        return objectives;
    }

    public static ModuleCollection<GameObjective> getRequiredObjectives(TeamModule team) {
        ModuleCollection<GameObjective> objectives = new ModuleCollection<>();
        for (GameObjective objective : getObjectives(team)) {
            if (objective.isRequired()) {
                objectives.add(objective);
            }
        }
        return objectives;
    }

    public static ChatColor getTeamColorByPlayer(OfflinePlayer player) {
        if (player.isOnline()) {
            Optional<TeamModule> team = getTeamByPlayer(player.getPlayer());
            if (team.isPresent()) return team.get().getColor();
            else return ChatColor.YELLOW;
        } else return ChatColor.DARK_AQUA;
    }

    public static boolean teamsReady() {
        for (TeamModule team : getTeams()) {
            if (!team.isReady()) return false;
        }
        return true;
    }

    public static boolean teamsNoObsReady() {
        for (TeamModule team : getTeams()) {
            if (!team.isReady() && !team.isObserver()) return false;
        }
        return true;
    }

    public static boolean isFFA(Match match) {
        for (TeamModule team : match.getModules().getModules(TeamModule.class)) {
            if (!team.isObserver()) return false;
        }
        return true;
    }

    public static boolean isObserver(Player player) {
        if(player == null) return false;

        Optional<TeamModule> teamModuleOptional = getTeamByPlayer(player);
        if(teamModuleOptional.orNull() == null) return false;


        return teamModuleOptional.get().isObserver();
    }

    public static boolean containsPlayer(ArrayList<MongoPlayer> mongoPlayerArrayList, Player player) {
        for (MongoPlayer mongoPlayer : mongoPlayerArrayList) {
            if (mongoPlayer.getName().equals(player.getName())) {
                return true;
            }
        }
        return false;
    }
}
