package com.minehut.warzone.module.modules.cycleTimer;

import com.minehut.warzone.Warzone;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.chat.UnlocalizedChatMessage;
import com.minehut.warzone.event.MatchEndEvent;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.TaskedModule;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.match.MatchState;
import com.minehut.warzone.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class CycleTimerModule implements TaskedModule, Cancellable {

    private boolean cancelled = true;
    private MatchState originalState;
    private int time, originalTime;

    private Match match;

    public CycleTimerModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (!isCancelled()) {
            match.setState(MatchState.CYCLING);
            if (time % 20 == 0 && time >= 1) {
                float percent = ((time * 100F) / originalTime) * 20;
//                BossBar.sendGlobalBossBar(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLING_TIMER, new UnlocalizedChatMessage(ChatColor.AQUA + GameHandler.getGameHandler().getCycle().getMap().getName() + ChatColor.DARK_AQUA), (this.time == 1 ? new LocalizedChatMessage(ChatConstant.UI_SECOND, ChatColor.DARK_RED + "1" + ChatColor.DARK_AQUA) : new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.DARK_RED + (time / 20 + "") + ChatColor.DARK_AQUA)))), percent);
            } else if (time < 1 && time >= 0) {
//                BossBar.sendGlobalBossBar(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLED_TO, ChatColor.AQUA + GameHandler.getGameHandler().getCycle().getMap().getName())), 0F);
            }
            if ((time % 100 == 0 && time > 0) || (time < 100 && time > 0 && time % 20 == 0)) {
                ChatUtil.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_CYCLING_TIMER, new UnlocalizedChatMessage(ChatColor.AQUA + GameHandler.getGameHandler().getCycle().getMap().getName() + ChatColor.DARK_AQUA), (this.time == 1 ? new LocalizedChatMessage(ChatConstant.UI_SECOND, ChatColor.DARK_RED + "1" + ChatColor.DARK_AQUA) : new LocalizedChatMessage(ChatConstant.UI_SECONDS, ChatColor.DARK_RED + (time / 20 + "") + ChatColor.DARK_AQUA)))));
            }
            if (time == 0 && match.getState() == MatchState.CYCLING) {
                cancelled = true;
                GameHandler.getGameHandler().cycleAndMakeMatch();
            }
            if (time < 0) {
                setCancelled(true);
//                BossBar.delete();
            }
            time--;
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        int time = Warzone.getInstance().getConfig().getInt("cycle");
        if (time < 0) {
            return;
        }
        cycleTimer(time);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time * 20;
    }

    public void setOriginalTime(int time) {
        this.originalTime = time * 20;
    }

    public MatchState getOriginalState() {
        return originalState;
    }

    public void setOriginalState(MatchState state) {
        this.originalState = state;
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
//            BossBar.delete();
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