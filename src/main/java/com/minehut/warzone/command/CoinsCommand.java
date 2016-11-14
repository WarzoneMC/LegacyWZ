package com.minehut.warzone.command;

import com.minehut.cloud.core.Cloud;
import com.minehut.cloud.core.players.data.NetworkPlayer;
import com.minehut.cloud.core.util.ChatColor;
import com.minehut.cloud.core.util.MongoUtils;
import com.minehut.warzone.user.WarzoneUser;
import com.mongodb.BasicDBObject;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.minehut.warzone.Warzone;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand {

    @Command(aliases = {"coins", "bal"}, desc = "View your coins.")
    public static void say(final CommandContext cmd, CommandSender sender) throws CommandException {

        if (cmd.argsLength() == 0) {
            Player player = (Player) sender;

            NetworkPlayer networkPlayer = Cloud.getInstance().getPlayerManager().getNetworkPlayer(player.getName());
            player.sendMessage(ChatColor.GOLD + "You have " + ChatColor.GREEN + networkPlayer.getCoins() + ChatColor.GOLD + " coins.");
        }

        else if (cmd.argsLength() == 3) {
            if (!sender.hasPermission("core.ranks.admin")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission.");
                return;
            }

            if (cmd.getString(0).equalsIgnoreCase("add")) {
                String target = cmd.getString(1);
                int amount = Integer.parseInt(cmd.getString(2));

                Player targetPlayer = Bukkit.getPlayer(target);
                String msg = "Gift from " + sender.getName();
                if (targetPlayer == null) {
                    msg = null;
                }

                Cloud.getInstance().getPlayerManager().addCoins(target, amount, msg, true);

                sender.sendMessage(ChatColor.GOLD + "You gave " + ChatColor.AQUA + target + " " + ChatColor.GOLD + amount + " coins");
            }

            else {
                sender.sendMessage(ChatColor.RED + "/coins add <player> <amount>");
            }
        }

    }
}