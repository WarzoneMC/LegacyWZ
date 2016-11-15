package com.minehut.warzone.module.modules.startTimer;

import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.chat.ChatMessage;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.chat.UnlocalizedChatMessage;
import com.minehut.warzone.event.MatchStartEvent;
import com.minehut.warzone.event.PlayerChangeTeamEvent;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.TaskedModule;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.Players;
import com.minehut.warzone.util.Teams;
import com.minehut.warzone.util.bossBar.BossBars;
import com.minehut.warzone.util.tabHeader.TitleAPI;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.match.MatchState;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

public class StartTimer implements TaskedModule, Cancellable {

    private int time, originalTime;
    private Match match;
    private boolean cancelled, forced;
    private String bossBar;
    private String neededPlayers;

    public StartTimer(Match match, int ticks) {
        this.time = ticks;
        this.originalTime = ticks;
        this.match = match;
        this.cancelled = true;
        this.bossBar = BossBars.addBroadcastedBossBar(new UnlocalizedChatMessage(""), BarColor.GREEN, BarStyle.SOLID, false);
        this.neededPlayers = BossBars.addBroadcastedBossBar(new UnlocalizedChatMessage(""), BarColor.RED, BarStyle.SOLID, false);
    }

    @Override
    public void unload() {
        BossBars.removeBroadcastedBossBar(bossBar);
        BossBars.removeBroadcastedBossBar(neededPlayers);
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (isCancelled()) {
            return;
        }
        BossBars.setProgress(bossBar, (double)time / originalTime);
        if (time % 20 == 0) {
            int intTime = (time / 20);
            if (time != 0) {
                BossBars.setTitle(bossBar, getStartTimerMessage(intTime));
                if (intTime <= 3) {
                    Players.broadcastSoundEffect(Sound.BLOCK_NOTE_PLING, 1, 1);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!Teams.getTeamByPlayer(player).get().isObserver()) {
                            player.showTitle(new TextComponent(ChatColor.YELLOW + "" + intTime), new TextComponent(""), 0, 5, 15);
                        }
                    }
                }
            } else {
                if (match.getState() != MatchState.STARTING) return;
                if (!forced) {
                    for (TeamModule team : Teams.getTeams()) {
                        if (!team.isObserver() && team.size() < team.getMin()) {
                            ChatUtil.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.ERROR_NOT_ENOUGH_PLAYERS)));
                            this.setCancelled(true);
                            return;
                        }
                    }
                }
                Players.broadcastSoundEffect(Sound.BLOCK_NOTE_PLING, 1, 2);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!Teams.getTeamByPlayer(player).get().isObserver()) {
                        String title = new LocalizedChatMessage(ChatConstant.UI_MATCH_START_TITLE).getMessage(player.getLocale());
                        player.showTitle(new TextComponent(ChatColor.GREEN + title), new TextComponent(""), 0, 5, 15);
                    }
                }
                BossBars.removeBroadcastedBossBar(bossBar);
                BossBars.removeBroadcastedBossBar(neededPlayers);
                match.setState(MatchState.PLAYING);
                ChatUtil.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTED)));
                Bukkit.getServer().getPluginManager().callEvent(new MatchStartEvent());
            }
        }
        if (time < 0) {
            setCancelled(true);
        }
        time--;
    }

    private void updateNeededPlayers(boolean show) {
        if (show && (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.WAITING) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING))) {
            BossBars.setTitle(neededPlayers, waitingPlayerMessage());
            BossBars.setVisible(neededPlayers, true);
        } else {
            BossBars.setVisible(neededPlayers, false);
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
        if (this.cancelled && GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
            GameHandler.getGameHandler().getMatch().setState(MatchState.WAITING);
            BossBars.setVisible(bossBar, false);
        } else {
            BossBars.setVisible(bossBar, true);
            if (time >= 20) ChatUtil.sendLocalizedMessage(getStartTimerMessage((time / 20)));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        updateNeededPlayers(neededPlayers() > 0);
    }

    public void setTime(int time) {
        this.time = time;
        setOriginalTime(time);
    }

    public void setOriginalTime(int time) {
        this.originalTime = time;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }

    private static ChatMessage getStartTimerMessage(int time) {
        return new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.UI_MATCH_STARTING_IN, new LocalizedChatMessage(time == 1 ? ChatConstant.UI_SECOND : ChatConstant.UI_SECONDS, ChatColor.RED + "" + time + ChatColor.GREEN)));
    }

    public int neededPlayers(){
        int count = 0;
        for (TeamModule teams : Teams.getTeams()) {
            if (!teams.isObserver() && teams.size() < teams.getMin()) {
                count += teams.getMin() - teams.size();
            }
        }
        return count;
    }

    public UnlocalizedChatMessage waitingPlayerMessage(){
        int count = 0;
        TeamModule team = Teams.getTeamById("observers").get();
        for (TeamModule teams : Teams.getTeams()) {
            if (!teams.isObserver() && teams.size() < teams.getMin()) {
                count ++;
                team = teams;
            }
        }
        return new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(neededPlayers() == 1 ? ChatConstant.UI_WAITING_PLAYER : ChatConstant.UI_WAITING_PLAYERS, ChatColor.AQUA + "" + neededPlayers() + ChatColor.RED, count == 1 ? team.getCompleteName() : ""));
    }

}