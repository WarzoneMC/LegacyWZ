package com.minehut.warzone.command;

import com.google.common.base.Optional;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.chat.UnlocalizedChatMessage;
import com.minehut.warzone.module.modules.team.TeamModule;
import com.minehut.warzone.util.Players;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.event.TeamNameChangeEvent;
import com.minehut.warzone.match.MatchState;
import com.minehut.warzone.util.ChatUtil;
import com.minehut.warzone.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeamCommands {

    @Command(aliases = {"force"}, desc = "Forces a player onto the team specified.", usage = "<player> <team>", min = 2)
    @CommandPermissions("cardinal.team.force")
    public static void force(final CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = Bukkit.getPlayer(cmd.getString(0));
        if (player == null) {
            throw new CommandException(ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        Optional<TeamModule> team = Teams.getTeamByName(cmd.getJoinedStrings(1));
        if (!team.isPresent()) {
            throw new CommandException(ChatConstant.ERROR_NO_TEAM_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        if (team.get().contains(player)) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_ON_TEAM, Players.getName(player, false) + ChatColor.RED, team.get().getCompleteName()).getMessage(ChatUtil.getLocale(sender)));
        }
        team.get().add(player, true, false);
        sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_PLAYER_FORCE, Players.getName(player, false) + ChatColor.GRAY, team.get().getCompleteName() + ChatColor.GRAY).getMessage(ChatUtil.getLocale(sender)));

    }

    @Command(aliases = {"alias"}, desc = "Renames a the team specified.", usage = "<team> <name>", min = 2)
    @CommandPermissions("cardinal.team.alias")
    public static void alias(final CommandContext cmd, CommandSender sender) throws CommandException {
        Optional<TeamModule> team = Teams.getTeamByName(cmd.getString(0));
        if (!team.isPresent()) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_TEAM_MATCH).getMessage(ChatUtil.getLocale(sender)));
        }
        String msg = cmd.getJoinedStrings(1);
        ChatUtil.sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_ALIAS, team.get().getCompleteName() + ChatColor.GRAY, team.get().getColor() + msg + ChatColor.GRAY)));
        team.get().setName(msg);
        Bukkit.getServer().getPluginManager().callEvent(new TeamNameChangeEvent(team.get()));
    }

    @Command(aliases = {"shuffle"}, desc = "Shuffles the teams.")
    @CommandPermissions("cardinal.team.shuffle")
    public static void shuffle(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.WAITING) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
            List<Player> playersToShuffle = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (Teams.getTeamByPlayer(player).isPresent()) {
                    if (!Teams.getTeamByPlayer(player).get().isObserver()) {
                        playersToShuffle.add(player);
                        TeamModule observers = Teams.getTeamById("observers").get();
                        observers.add(player, true, false);
                    }
                }
            }
            while (playersToShuffle.size() > 0) {
                Player player = playersToShuffle.get(new Random().nextInt(playersToShuffle.size()));
                Optional<TeamModule> team = Teams.getTeamWithFewestPlayers(GameHandler.getGameHandler().getMatch());
                if (team.isPresent()) team.get().add(player, true);
                playersToShuffle.remove(player);
            }
            String locale = ChatUtil.getLocale(sender);
            sender.sendMessage(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_SHUFFLE).getMessage(locale));
        } else {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_SHUFFLE).getMessage(ChatUtil.getLocale(sender)));
        }
    }

    @Command(aliases = {"size"}, desc = "Changes the specified team's size.", usage = "<team> <size>", min = 2)
    @CommandPermissions("cardinal.team.size")
    public static void size(final CommandContext cmd, CommandSender sender) throws CommandException {
        Optional<TeamModule> team = Teams.getTeamByName(cmd.getString(0));
        if (!team.isPresent()) {
            throw new CommandException(ChatConstant.ERROR_NO_TEAM_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        team.get().setMaxOverfill(Integer.parseInt(cmd.getString(1)));
        team.get().setMax(Integer.parseInt(cmd.getString(1)));
        sender.sendMessage(new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_SIZE_CHANGED, team.get().getCompleteName() + ChatColor.WHITE, ChatColor.AQUA + cmd.getString(1)).getMessage(ChatUtil.getLocale(sender)));
    }

    @Command(aliases = {"myteam", "mt"}, desc = "Shows what team you are on", min = 0, max = 0)
    public static void myTeam(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        Optional<TeamModule> team = Teams.getTeamByPlayer((Player) sender);
        if (team.isPresent()) {
            sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_ON_TEAM, team.get().getCompleteName())).getMessage(((Player) sender).spigot().getLocale()));
        }
    }

    public static class TeamParentCommand {
        @Command(aliases = {"team"}, desc = "Manage the teams in the match.")
        @NestedCommand({TeamCommands.class})
        public static void team(final CommandContext args, CommandSender sender) throws CommandException {

        }
    }
}
