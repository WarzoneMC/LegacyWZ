package com.minehut.warzone.module.modules.scoreboard;

import com.minehut.warzone.Warzone;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.event.PlayerChangeTeamEvent;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.util.damage.DamageInfo;
import com.minehut.warzone.util.scoreboard.SimpleScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

/**
 * Created by luke on 4/1/16.
 */
public class ScoreboardModule implements Module {

    private TeamModule team;
    private SimpleScoreboard simpleScoreboard;
    private ArrayList<TeamModule> teams = new ArrayList<>();

    private ScoreboardUpdater scoreboardUpdater;

    public ScoreboardModule(final TeamModule team) {
        this.team = team;
        this.simpleScoreboard = new SimpleScoreboard(ChatColor.AQUA + "Objectives");
        Scoreboard scoreboard = simpleScoreboard.getScoreboard();

        for (TeamModule teams : Teams.getTeams()) {
            Team t = scoreboard.registerNewTeam(teams.getId());
            t.setPrefix(teams.getColor() + "");
            t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

            //todo: add friendly fire option
            t.setAllowFriendlyFire(false);

            if (teams.isObserver()) {
                t.setCanSeeFriendlyInvisibles(true);
            } else {
                Team spy = scoreboard.registerNewTeam(teams.getId() + "_spy");
                spy.setPrefix(teams.getColor() == ChatColor.RED ? ChatColor.DARK_AQUA.toString() : ChatColor.DARK_RED.toString());
                spy.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

                spy.setAllowFriendlyFire(false);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (!event.isCancelled()) {
            if (event.getNewTeam().orNull() == this.team) {
                this.simpleScoreboard.send(event.getPlayer());
            }
            for (TeamModule team : Teams.getTeams()) {
                remove(team, event.getPlayer());
            }
            if (event.getNewTeam().isPresent()) {
                add(event.getNewTeam().get(), event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        DamageInfo info = new DamageInfo(event);
        if (info.getDamagerPlayer() != null && info.getHurtPlayer() != null) {
            Player damager = info.getDamagerPlayer();
            Player hurt = info.getHurtPlayer();
            Team dTeam = simpleScoreboard.getScoreboard().getPlayerTeam(damager);
            Team hTeam = simpleScoreboard.getScoreboard().getPlayerTeam(hurt);
            if (dTeam != null && hTeam != null && (dTeam.getName().contains(hTeam.getName()) || hTeam.getName().contains(dTeam.getName()))) {
                event.setCancelled(true);
            }
        }
    }

    public static void add(TeamModule team, Player player) {
        for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
            scoreboard.getSimpleScoreboard().getScoreboard().getTeam(team.getId()).addPlayer(player);
        }
    }

    public static void remove(TeamModule team, Player player) {
        for (ScoreboardModule scoreboard : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreboardModule.class)) {
            scoreboard.getSimpleScoreboard().getScoreboard().getTeam(team.getId()).removePlayer(player);
        }
    }

    public void setScoreboardUpdater(ScoreboardUpdater scoreboardUpdater) {
        this.scoreboardUpdater = scoreboardUpdater;
        Bukkit.getPluginManager().registerEvents(scoreboardUpdater, Warzone.getInstance());
        this.scoreboardUpdater.initialSetup(team, simpleScoreboard);
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this.scoreboardUpdater);
    }

    public TeamModule getTeam() {
        return team;
    }

    public SimpleScoreboard getSimpleScoreboard() {
        return simpleScoreboard;
    }

    public ArrayList<TeamModule> getTeams() {
        return teams;
    }


}
