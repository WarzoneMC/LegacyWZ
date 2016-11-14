package com.minehut.warzone.module.modules.timeNotifications;

import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.module.modules.matchTimer.MatchTimer;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.chat.UnlocalizedChatMessage;
import com.minehut.warzone.module.TaskedModule;
import com.minehut.warzone.module.modules.timeLimit.TimeLimit;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;

public class TimeNotifications implements TaskedModule {

    private static int nextTimeMessage;

    protected TimeNotifications() {
        nextTimeMessage = TimeLimit.getMatchTimeLimit();
    }

    public static void resetNextMessage() {
        if (TimeLimit.getMatchTimeLimit() == 0) {
            nextTimeMessage = (int) Math.round(MatchTimer.getTimeInSeconds());
        } else {
            nextTimeMessage = (int) Math.round(TimeLimit.getMatchTimeLimit() - MatchTimer.getTimeInSeconds());
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            double time = MatchTimer.getTimeInSeconds();
            double timeRemaining;
            if (TimeLimit.getMatchTimeLimit() == 0) {
                if (time >= nextTimeMessage) {
//                    ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.AQUA + "{0}", new LocalizedChatMessage(ChatConstant.UI_TIME_ELAPSED, new UnlocalizedChatMessage(ChatColor.GREEN + Strings.formatTime(nextTimeMessage)))));
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Time Remaining: " + ChatColor.GREEN + Strings.formatTime(nextTimeMessage));
                    nextTimeMessage += 300;
                }
                return;
            }
            timeRemaining = TimeLimit.getMatchTimeLimit() - time;
            if (TimeLimit.getMatchTimeLimit() > 0) {
                int timeLeft = ((TimeLimit.getMatchTimeLimit() - (int) MatchTimer.getTimeInSeconds()));
                int percent = (int) ((100F * timeLeft) / TimeLimit.getMatchTimeLimit());
                if (percent == 0) percent = 1;
//                BossBar.sendGlobalBossBar(new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatUtil.getTimerColor(timeRemaining) + "{1}", new LocalizedChatMessage(ChatConstant.UI_TIMER), new UnlocalizedChatMessage(Strings.formatTime(timeRemaining + 1))), percent);
            }
            if (nextTimeMessage >= timeRemaining) {
                if (nextTimeMessage <= 5) {
//                    ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatColor.DARK_RED + Strings.formatTime(nextTimeMessage), new LocalizedChatMessage(ChatConstant.UI_TIMER)));
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Time Remaining: " + ChatColor.DARK_RED + Strings.formatTime(nextTimeMessage));
                    nextTimeMessage--;
                } else if (nextTimeMessage <= 30) {
//                    ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatColor.GOLD + Strings.formatTime(nextTimeMessage), new LocalizedChatMessage(ChatConstant.UI_TIMER)));
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Time Remaining: " + ChatColor.GOLD + Strings.formatTime(nextTimeMessage));
                    nextTimeMessage -= 5;
                } else if (nextTimeMessage <= 60) {
//                    ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatColor.YELLOW + Strings.formatTime(nextTimeMessage), new LocalizedChatMessage(ChatConstant.UI_TIMER)));
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Time Remaining: " + ChatColor.YELLOW + Strings.formatTime(nextTimeMessage));
                    nextTimeMessage -= 15;
                } else {
//                    ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.AQUA + "{0} " + ChatColor.GREEN + Strings.formatTime(nextTimeMessage), new LocalizedChatMessage(ChatConstant.UI_TIMER)));
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Time Remaining: " + ChatColor.GREEN + Strings.formatTime(nextTimeMessage));
                    if ((nextTimeMessage / 60) % 5 == 0 && nextTimeMessage != 300) {
                        nextTimeMessage -= 300;
                    } else if (nextTimeMessage % 60 == 0 && nextTimeMessage <= 300) {
                        nextTimeMessage -= 60;
                    } else {
                        nextTimeMessage = (nextTimeMessage / 300) * 300;
                    }
                }
            }
        }
    }

}