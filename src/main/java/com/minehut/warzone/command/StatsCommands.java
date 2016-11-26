package com.minehut.warzone.command;

import com.minehut.warzone.user.WarzoneUser;
import com.minehut.warzone.util.Messages;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.minehut.warzone.Warzone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommands {

    @Command(aliases = {"stats", "stat"}, desc = "View your stats.")
    public static void say(final CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = (Player) sender;

        if (cmd.argsLength() == 0) {
            viewStats(player, player.getName());
        } else {
            viewStats(player, cmd.getJoinedStrings(0));
        }
    }

    public static void viewStats(Player player, String target) {
        Player targetPlayer = Bukkit.getServer().getPlayer(target);
        if (targetPlayer == null) {
            player.sendMessage(ChatColor.RED + "Unable to find online player " + ChatColor.YELLOW + target);
            return;
        }

        WarzoneUser targetUser = Warzone.getInstance().getUserManager().getUser(targetPlayer);
        player.sendMessage(Messages.DIVIDER);
        player.sendMessage(Messages.TAB + ChatColor.DARK_AQUA + "Viewing stats for " +  ChatColor.AQUA + targetPlayer.getName());
        player.sendMessage(Messages.TAB + "Level: " + ChatColor.GREEN + targetUser.getLevelString().replace("[", "").replace("]", ""));
        player.sendMessage(Messages.TAB + "Wins: " + ChatColor.GREEN + targetUser.getTotalWins());
        player.sendMessage(Messages.TAB + "Losses: " + ChatColor.GREEN + targetUser.getTotalLosses());
        player.sendMessage(Messages.TAB + "Kills: " + ChatColor.GREEN + targetUser.getTotalKills());
        player.sendMessage(Messages.TAB + "Longest Snipe: " + ChatColor.GREEN + targetUser.getLongestSnipe() + " blocks");
        player.sendMessage(Messages.DIVIDER);
    }
}