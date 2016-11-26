package com.minehut.warzone.module.gameModules.infected;

import com.minehut.warzone.event.*;
import com.minehut.warzone.kit.Kit;
import com.minehut.warzone.module.modules.stats.StatsModule;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.user.WarzoneUser;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.kit.KitManager;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.module.modules.teamPicker.TeamPicker;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.Messages;
import com.minehut.warzone.util.Players;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.util.titleAPI.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by luke on 4/1/16.
 */
public class InfectedGameModule implements Module {
    public static int INITIAL_SPREAD_TIME = 10;
    public static int REQUIRED_PLAYERS = 2;

    TeamModule infected;
    TeamModule humans;

    private int initialSpreadTime = INITIAL_SPREAD_TIME;
    private int initialSpreadRunnable = -1;

    private Kit defaultHumanKit;

    public InfectedGameModule() {
        GameHandler.getGameHandler().getMatch().getModules().getModule(TeamPicker.class).setDonorOnlyTeamPicking(false);

        infected = Teams.getTeamById("infected").get();
        humans = Teams.getTeamById("humans").get();

        infected.setAllowJoin(false);

        StatsModule.COINS_KILL = 1;
    }

    @EventHandler
    public void onCycle(CycleCompleteEvent event) {
        this.defaultHumanKit = Warzone.getInstance().getKitManager().getKitFromName("survivor");

        for (WarzoneUser user : Warzone.getInstance().getUserManager().getUsers()) {
            Kit kit = Warzone.getInstance().getKitManager().getKitFromId(user.getSelectedKit());
            if (kit.getTeamId().equals("infected")) {
                user.setSelectedKit(KitManager.getIdFromKit(defaultHumanKit));
                user.setActiveKit(KitManager.getIdFromKit(defaultHumanKit));
            }
        }
    }

    @EventHandler
    public void onStart(MatchStartEvent event) {
        this.initialSpreadRunnable = Bukkit.getScheduler().scheduleSyncRepeatingTask(Warzone.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (initialSpreadTime <= 0) {

                    if (humans.size() < REQUIRED_PLAYERS) {
                        Bukkit.broadcastMessage(ChatUtil.getWarningMessage("Not enough players to start!"));
                        initialSpreadTime = INITIAL_SPREAD_TIME;
                        return;
                    }

                    initialSpread();
                    checkInfectedWin();
                    Bukkit.getScheduler().cancelTask(initialSpreadRunnable);
                    return;
                }

                if (initialSpreadTime <= 5 || initialSpreadTime % 10 == 0) {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Infection spreading in " + ChatColor.DARK_RED + initialSpreadTime + ChatColor.YELLOW + " seconds.");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 0.75f);
                    }
                }

                initialSpreadTime--;
            }
        }, 20L, 20L);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity() != null && humans.contains(event.getEntity())) {
            Players.resetPlayer(event.getEntity()); //cancels the death event
            infected.add(event.getEntity(), true);

            WarzoneUser deadUser = Warzone.getInstance().getUserManager().getUser(event.getEntity());
            deadUser.setSelectedKit("zombie");
            deadUser.setActiveKit("zombie");

            Warzone.getInstance().getKitManager().respawnApplyKit(event.getEntity());

            event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10, 8));

            checkInfectedWin();
        }
    }

    //todo: try to combine these two events

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if(this.infected.contains(event.getKiller())) {
            if (event.getKiller() != null) {
                Bukkit.broadcastMessage(ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.RED +
                        " was " + ChatColor.DARK_RED + "infected" + ChatColor.RED + " by " + ChatColor.RED + event.getKiller().getName() + "!");
            } else {
                Bukkit.broadcastMessage(ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.RED +
                        " was " + ChatColor.DARK_RED + "infected" + ChatColor.RED + "!");
            }
        }
    }

    private void checkInfectedWin() {
        if (this.humans.size() <= 0) {
            GameHandler.getGameHandler().getMatch().end(infected);
        }
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        if (event.getNewTeam().get() == this.infected) {
            TitleAPI.sendTitle(event.getPlayer(), 0, 50, 10, ChatColor.RED + "You are Infected", ChatColor.DARK_RED + "Kill the Humans");
        }
    }

    private void initialSpread() {
        humans.setAllowJoin(false);
        infected.setAllowJoin(true);

        int infectedCount = 1;

        if (humans.size() >= 8) {
            infectedCount = 2;
        }
        else if (humans.size() >= 14) {
            infectedCount = 3;
        }
        else if (humans.size() >= 20) {
            infectedCount = 4;
        }

        ArrayList<Player> choosen = humans;
        Collections.shuffle(choosen);

        for (int i = 0; i < infectedCount; i++) {
            Player player = choosen.get(i);
            infected.add(player);

            player.sendMessage(Messages.DIVIDER);
            player.sendMessage("");
            player.sendMessage(Messages.TAB + "You are " + ChatColor.RED + "Infected!");
            player.sendMessage(Messages.TAB + "Kill the humans!");
            player.sendMessage("");
            player.sendMessage(Messages.DIVIDER);

            player.getWorld().spigot().strikeLightningEffect(player.getLocation(), false);
        }

        Iterator iterator = humans.iterator();
        while (iterator.hasNext()) {
            Player player = (Player) iterator.next();

            player.sendMessage(Messages.DIVIDER);
            player.sendMessage("");
            player.sendMessage(Messages.TAB + "You are a " + ChatColor.YELLOW + "Human!");
            player.sendMessage("");
            player.sendMessage(Messages.DIVIDER);
        }
    }

    @EventHandler
    public void onGameEnd(MatchEndEvent event) {
        Bukkit.getScheduler().cancelTask(initialSpreadRunnable);
    }

    @Override
    public void unload() {
        Bukkit.getScheduler().cancelTask(initialSpreadRunnable);
    }
}
