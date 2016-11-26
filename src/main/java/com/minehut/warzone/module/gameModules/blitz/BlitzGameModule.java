package com.minehut.warzone.module.gameModules.blitz;

import com.minehut.warzone.GameHandler;
import com.minehut.warzone.event.CardinalDeathEvent;
import com.minehut.warzone.event.MatchStartEvent;
import com.minehut.warzone.event.PlayerAttemptChangeTeamEvent;
import com.minehut.warzone.event.StatMatchEndEvent;
import com.minehut.warzone.match.MatchState;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.user.MongoPlayer;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.Teams;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by luke on 4/1/16.
 */
public class BlitzGameModule implements Module {
    private HashMap<TeamModule, ArrayList<MongoPlayer>> teamCache = new HashMap<>();

    public BlitzGameModule() {

    }

    @EventHandler
    public void onStatMatchEndEvent(StatMatchEndEvent event) {
        if (event.getMatchEndEvent().getTeam().orNull() != null) {
            event.setWinners(teamCache.get(event.getMatchEndEvent().getTeam().get()));

            ArrayList<MongoPlayer> losers = new ArrayList<>();
            for (TeamModule teamModule : teamCache.keySet()) {
                if (teamModule != event.getMatchEndEvent().getTeam().get()) {
                    Iterator it = teamModule.iterator();
                    while (it.hasNext()) {
                        losers.add(new MongoPlayer((Player) it));
                    }
                }
            }

            event.setLosers(losers);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (event.getKiller() != null) {
            event.getKiller().getInventory().addItem(new ItemStack(Material.ARROW));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) return;
        event.setDamage(Integer.MAX_VALUE);
    }

    @EventHandler
    public void onGameStart(MatchStartEvent event) {
        for (TeamModule teamModule : Teams.getTeams()) {
            if (!teamModule.isObserver()) {
                ArrayList<MongoPlayer> players = new ArrayList<>();

                Iterator it = teamModule.iterator();
                while (it.hasNext()) {
                    players.add(new MongoPlayer((Player) it.next()));
                }

                teamCache.put(teamModule, players);
            }
        }
    }

    @EventHandler
    public void onAttemptJoin(PlayerAttemptChangeTeamEvent event) {
        if (GameHandler.getGameHandler().getMatch().getState() == MatchState.PLAYING) {
            event.setCancelled(true);
            event.setCancelMessage(ChatUtil.getWarningMessage("You can only join at the start of the match."));
        }
    }

    @EventHandler
    public void onDeath(CardinalDeathEvent event) {
        TeamModule teamModule = Teams.getTeamByPlayer(event.getPlayer()).get();

        if (!teamModule.isObserver()) {
            Teams.getObservers().get().add(event.getPlayer());
            checkForWin();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        checkForWin();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerDeathEvent event) {
        checkForWin();
    }

    private void checkForWin() {
        ArrayList<TeamModule> teams = Teams.getTeams();
        int numberOfTeams = teams.size() - 1; //don't count observers
        int teamsEliminated = 0;
        ArrayList<TeamModule> aliveTeams = new ArrayList<>();

        for (TeamModule teamModule : teams) {
            if (!teamModule.isObserver()) {
                if (teamModule.size() == 0) {
                    teamsEliminated++;
                } else {
                    aliveTeams.add(teamModule);
                }
            }
        }

        if(aliveTeams.isEmpty()) return;

        if (aliveTeams.size() == 1) {
            GameHandler.getGameHandler().getMatch().end(aliveTeams.get(0));
        }
//        else {
//            Bukkit.broadcastMessage(ChatUtil.getWarningMessage("Match was a tie!"));
//            GameHandler.getGameHandler().getMatch().end();
//        }
    }

    @Override
    public void unload() {

    }
}
