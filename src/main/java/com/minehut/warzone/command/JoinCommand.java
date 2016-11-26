package com.minehut.warzone.command;

import com.google.common.base.Optional;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.module.modules.teamPicker.TeamPicker;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.chat.S;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.match.MatchState;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand {

    @Command(aliases = {"join", "play"}, desc = "Join a team.", usage = "[team]")
    public static void join(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.CYCLING)) {
            throw new CommandException(ChatUtil.getWarningMessage(new LocalizedChatMessage(ChatConstant.ERROR_MATCH_OVER).getMessage(((Player) sender).spigot().getLocale())));
        }
        Optional<TeamModule> originalTeam = Teams.getTeamByPlayer((Player) sender);
        if (cmd.argsLength() == 0 && originalTeam.isPresent() && !originalTeam.get().isObserver()) {
            throw new CommandException(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_JOINED, Teams.getTeamByPlayer((Player) sender).get().getCompleteName() + ChatColor.RED).getMessage(((Player) sender).spigot().getLocale())));
        }
        Optional<TeamModule> destinationTeam = Optional.absent();
        if (cmd.argsLength() > 0) {
            Player player = (Player) sender;
            if (!player.hasPermission("cardinal.join.pick") && GameHandler.getGameHandler().getMatch().getModules().getModule(TeamPicker.class).isDonorOnlyTeamPicking()) {
                sender.sendMessage(ChatColor.RED + "Only " + ChatColor.LIGHT_PURPLE + "donators" + ChatColor.RED + " can choose their team.");
                sender.sendMessage(ChatColor.RED + "You can use " + ChatColor.GREEN + "Auto Join" + ChatColor.RED + "instead.");
                S.playSound((Player) sender, Sound.ENTITY_VILLAGER_NO);
                return;
            }

            for (TeamModule teamModule : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
                if (teamModule.getName().toLowerCase().startsWith(cmd.getJoinedStrings(0).toLowerCase())) {

                    if (!teamModule.isAllowJoin()) {
                        ChatUtil.sendWarningMessage(player, teamModule.getColor() + teamModule.getName() + ChatColor.RED + " is locked.");
                        return;
                    }

                    destinationTeam = Optional.of(teamModule);
                    break;
                }
            }
            if (!destinationTeam.isPresent()) {
                throw new CommandException(ChatConstant.ERROR_NO_TEAM_MATCH.getMessage(ChatUtil.getLocale(sender)));
            }
            if (destinationTeam.get().contains(sender)) {
                throw new CommandException(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_JOINED, destinationTeam.get().getCompleteName() + ChatColor.RED).getMessage(ChatUtil.getLocale(sender))));
            }
            destinationTeam.get().add((Player) sender, false);
        } else {
            destinationTeam = Teams.getTeamWithFewestPlayers(GameHandler.getGameHandler().getMatch());
            if (destinationTeam.isPresent()) {
                destinationTeam.get().add((Player) sender, false);
            } else {
                throw new CommandException(ChatConstant.ERROR_TEAMS_FULL.getMessage(ChatUtil.getLocale(sender)));
            }
        }
    }

    @Command(aliases = {"leave", "spectate", "spec"}, desc = "Leave the game.")
    public static void leave(final CommandContext cmd, CommandSender sender) {
        Bukkit.getServer().dispatchCommand(sender, "join observers");
    }
}
