package com.minehut.warzone.command;

import com.google.common.base.Optional;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.chat.UnlocalizedChatMessage;
import com.minehut.warzone.event.TimeLimitChangeEvent;
import com.minehut.warzone.module.modules.timeLimit.TimeLimit;
import com.minehut.warzone.module.modules.timeNotifications.TimeNotifications;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Strings;
import com.minehut.warzone.util.Teams;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.minehut.warzone.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TimeLimitCommand {

    @Command(aliases = {"timelimit", "tl"}, desc = "Modify the time limit of the current match.", usage = "<cancel, add, set> <time> [result]")
    public static void timeLimit(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (cmd.argsLength() == 0) {
            if (TimeLimit.getMatchTimeLimit() != 0) {
                sender.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.GENERIC_TIME_LIMIT_WITH_RESULT, ChatColor.AQUA + Strings.formatTimeWithMillis(TimeLimit.getMatchTimeLimit()) + ChatColor.YELLOW, ChatColor.WHITE + (GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().equals(TimeLimit.Result.TEAM) ? GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTeam().getCompleteName() + " wins" : GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().name().toLowerCase().replaceAll("_", " ")) + ChatColor.YELLOW).getMessage(ChatUtil.getLocale(sender)));
            } else {
                sender.sendMessage(ChatColor.YELLOW + ChatConstant.GENERIC_NO_TIME_LIMIT.getMessage(ChatUtil.getLocale(sender)));
            }
            return;
        }
        if (!sender.hasPermission("cardinal.timelimit")) {
            throw new CommandPermissionsException();
        }
        if (cmd.argsLength() == 1 && cmd.getString(0).equalsIgnoreCase("cancel")) {
            sender.sendMessage(ChatColor.YELLOW + ChatConstant.GENERIC_TIME_LIMIT_CANCELLED.getMessage(ChatUtil.getLocale(sender)));
            GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).setTimeLimit(0);
            Bukkit.getServer().getPluginManager().callEvent(new TimeLimitChangeEvent());
        } else if (cmd.argsLength() > 1 && (cmd.getString(0).equalsIgnoreCase("add") || cmd.getString(0).equalsIgnoreCase("set"))) {
            int time = cmd.getString(0).equalsIgnoreCase("add") ? TimeLimit.getMatchTimeLimit() : 0;
            try {
                time += Strings.timeStringToSeconds(cmd.getString(1));
            } catch (NumberFormatException e) {
                throw new CommandException(ChatConstant.ERROR_TIME_FORMAT_STRING.getMessage(ChatUtil.getLocale(sender)));
            }
            if (time != 0) {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}" + " " + ChatColor.AQUA + "{1}" + ChatColor.YELLOW + " with the result " + ChatColor.WHITE + "{2}", "The time limit is", Strings.formatTimeWithMillis(TimeLimit.getMatchTimeLimit()), GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().equals(TimeLimit.Result.TEAM) ? GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTeam().getCompleteName() + " wins" : GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().name().toLowerCase().replaceAll("_", " ")).getMessage(ChatUtil.getLocale(sender)));
            } else {
                sender.sendMessage(ChatColor.YELLOW + ChatConstant.GENERIC_TIME_LIMIT_CANCELLED.getMessage(ChatUtil.getLocale(sender)));
            }
            GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).setTimeLimit(time);
            if (cmd.argsLength() > 2) {
                try {
                    if (TimeLimit.Result.valueOf(cmd.getJoinedStrings(2).toUpperCase().replaceAll(" ", "_")).equals(TimeLimit.Result.TEAM)) {
                        throw new CommandException(ChatConstant.ERROR_NO_RESULT_MATCH.getMessage(ChatUtil.getLocale(sender)));
                    }
                    for (TimeLimit module : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimit.class)) {
                        module.setResult(TimeLimit.Result.valueOf(cmd.getJoinedStrings(2).toUpperCase().replaceAll(" ", "_")));
                    }
                } catch (IllegalArgumentException e) {
                    if (Teams.getTeamByName(cmd.getJoinedStrings(2)) == null) {
                        throw new CommandException(ChatConstant.ERROR_NO_RESULT_MATCH.getMessage(ChatUtil.getLocale(sender)));
                    }
                    for (TimeLimit module : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimit.class)) {
                        module.setResult(TimeLimit.Result.TEAM);
                        Optional<TeamModule> team = Teams.getTeamByName(cmd.getJoinedStrings(2));
                        if (team.isPresent()) module.setTeam(team.get());
                    }
                }
            }
            Bukkit.getServer().getPluginManager().callEvent(new TimeLimitChangeEvent());
        } else {
            throw new CommandUsageException(ChatConstant.ERROR_TOO_FEW_ARGUMENTS.getMessage(ChatUtil.getLocale(sender)), "/timelimit <cancel, add, set> <time> [result]");
        }
    }

}
