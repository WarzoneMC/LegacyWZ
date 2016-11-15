package com.minehut.warzone.module.modules.cycleTimer;

import com.minehut.warzone.Warzone;
import com.minehut.warzone.chat.ChatMessage;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.chat.UnlocalizedChatMessage;
import com.minehut.warzone.event.MatchEndEvent;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.TaskedModule;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.match.MatchState;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.bossBar.BossBars;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class CycleTimerModule implements TaskedModule, Cancellable {

    private boolean cancelled = true;
    private MatchState originalState;
    private int time, originalTime;
    private String bossBar;

    private Match match;

    public CycleTimerModule(Match match) {
        this.match = match;
        this.bossBar = BossBars.addBroadcastedBossBar(new UnlocalizedChatMessage(""), BarColor.BLUE, BarStyle.SOLID, false);
    }

    @Override
    public void unload() {
        BossBars.removeBroadcastedBossBar(bossBar);
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (!isCancelled()) {
            BossBars.setProgress(bossBar, ((double)time / originalTime));
            if (time % 20 == 0) {
                if (time != 0) {
                    BossBars.setTitle(bossBar, getCycleTimerMessage());
                } else {
                    BossBars.setTitle(bossBar, new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLED_TO, ChatColor.AQUA + GameHandler.getGameHandler().getCycle().getMap().getName())));
                    if (match.getState() == MatchState.CYCLING) {
                        cancelled = true;
                        BossBars.setProgress(bossBar, 0D);
                        GameHandler.getGameHandler().cycleAndMakeMatch();
                    }
                }
            }
            if (time < 0) {
                setCancelled(true);
            }
            time--;
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        cycleTimer(10);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time * 20;
    }

    public void setOriginalTime(int time) {
        this.originalTime = time;
    }

    public MatchState getOriginalState() {
        return originalState;
    }

    public void setOriginalState(MatchState state) {
        this.originalState = state;
    }

    private ChatMessage getCycleTimerMessage() {
        int intTime = (time / 20);
        return new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLING_TIMER,
                new UnlocalizedChatMessage(ChatColor.AQUA + GameHandler.getGameHandler().getCycle().getMap().getName() + ChatColor.DARK_AQUA),
                new LocalizedChatMessage(intTime == 1 ? ChatConstant.UI_SECOND : ChatConstant.UI_SECONDS, ChatColor.RED + "" + intTime + ChatColor.DARK_AQUA)));
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.cancelled = isCancelled;
        if (this.cancelled) {
            GameHandler.getGameHandler().getMatch().setState(originalState);
            BossBars.setVisible(bossBar, false);
        } else {
            BossBars.setVisible(bossBar, true);
            if (time >= 20) ChatUtil.sendLocalizedMessage(getCycleTimerMessage());
            match.setState(MatchState.CYCLING);
        }
    }

    public boolean cycleTimer(int seconds) {
        if (match.getState() != MatchState.PLAYING) {
            setOriginalState(match.getState());
            setTime(seconds);
            setOriginalTime(time);
            setCancelled(false);
            return true;
        } else return false;
    }

}