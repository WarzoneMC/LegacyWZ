package com.minehut.warzone.command;

import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.chat.UnlocalizedChatMessage;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.match.MatchState;
import com.minehut.warzone.module.modules.cycleTimer.CycleTimerModule;
import com.minehut.warzone.module.modules.startTimer.StartTimer;
import com.minehut.warzone.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CancelCommand {

    @Command(aliases = {"cancel"}, desc = "Cancels the current countdown.")
    @CommandPermissions("cardinal.cancel")
    public static void cancel(final CommandContext cmd, CommandSender sender) {
        GameHandler handler = GameHandler.getGameHandler();
//        if (GameHandler.getGameHandler().getMatch().getState() == MatchState.CYCLING) BossBar.delete();
//        if (GameHandler.getGameHandler().getMatch().getState() == MatchState.STARTING) BossBar.delete();
        if (!handler.getMatch().getModules().getModule(CycleTimerModule.class).isCancelled()) {
            handler.getMatch().getModules().getModule(CycleTimerModule.class).setCancelled(true);
        }
        if (!handler.getMatch().getModules().getModule(StartTimer.class).isCancelled()) {
            handler.getMatch().getModules().getModule(StartTimer.class).setCancelled(true);
        }
        if (handler.getMatch().getState().equals(MatchState.STARTING)) {
            handler.getMatch().setState(MatchState.WAITING);
        } else if (handler.getMatch().getState().equals(MatchState.CYCLING)) {
            handler.getMatch().setState(MatchState.ENDED);
        }
        ChatUtil.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_COUNTDOWN_CANELLED)));
    }
}
