package com.minehut.warzone.command;

import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.module.modules.matchTimer.MatchTimer;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MatchCommand {

    @Command(aliases = {"matchinfo", "match"}, desc = "Shows information about the currently playing match.", usage = "")
    public static void match(final CommandContext args, CommandSender sender) throws CommandException {
        sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "------" + ChatColor.DARK_AQUA + " " + new LocalizedChatMessage(ChatConstant.UI_MATCH_INFO).getMessage(ChatUtil.getLocale(sender)) + " " + ChatColor.GRAY + "(" + GameHandler.getGameHandler().getMatch().getNumber() + ")" + ChatColor.RED + " " + ChatColor.STRIKETHROUGH + "------");
        sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TIME).getMessage(ChatUtil.getLocale(sender)) + ": " + ChatColor.GOLD + (Warzone.getInstance().getConfig().getBoolean("matchTimeMillis") ? Strings.formatTimeWithMillis(MatchTimer.getTimeInSeconds()) : Strings.formatTime(MatchTimer.getTimeInSeconds())));
//        String teams = "";
//        boolean hasObjectives = false;
//        for (TeamModule team : Teams.getTeams()) {
//            int players = 0;
//            for (Player player : Bukkit.getOnlinePlayers()) {
//                if (Teams.getTeamByPlayer(player).isPresent()) {
//                    if (Teams.getTeamByPlayer(player).get() == team) {
//                        players++;
//                    }
//                }
//            }
//            teams += team.getCompleteName() + ChatColor.GRAY + ": " + ChatColor.RESET + players + (team.isObserver() ? "" : ChatColor.GRAY + "/" + team.getMax() + ChatColor.AQUA + " | ");
//            if (Teams.getShownObjectives(team).size() > 0) hasObjectives = true;
//        }
//        sender.sendMessage(teams);
//        Match match = GameHandler.getGameHandler().getMatch();
//        if (match.isRunning() || match.getState().equals(MatchState.ENDED) || match.getState().equals(MatchState.CYCLING)) {
//            if (hasObjectives) {
//                sender.sendMessage(ChatColor.RED + "---- " + new LocalizedChatMessage(ChatConstant.UI_GOALS).getMessage(ChatUtil.getLocale(sender)) + " ----");
//                for (TeamModule team : Teams.getTeams()) {
//                    if (!team.isObserver()) {
//                        if (Teams.getShownObjectives(team).size() > 0) {
//                            String objectives = "";
//                            for (GameObjective objective : Teams.getShownObjectives(team)) {
//                                objectives += (objective.isComplete() ? ChatColor.GREEN : ChatColor.DARK_RED) + WordUtils.capitalizeFully(objective.getName().replaceAll("_", " ")) + "  ";
//                            }
//                            objectives = objectives.trim();
//                            sender.sendMessage(team.getCompleteName() + ChatColor.GRAY + ": " + objectives);
//                        }
//                    }
//                }
//            }
//        }
    }

}
