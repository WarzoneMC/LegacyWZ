package com.minehut.warzone.command;

import com.minehut.warzone.util.Players;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListCommand {

    @Command(aliases = {"list"}, desc = "Lists all online players.")
    public static void list(final CommandContext args, CommandSender sender) {
        StringBuilder result = new StringBuilder()
                .append(ChatColor.GRAY)
                .append(ChatConstant.UI_ONLINE.asMessage().getMessage(ChatUtil.getLocale(sender)))
                .append(" (")
                .append(Bukkit.getOnlinePlayers().size())
                .append("/")
                .append(Bukkit.getMaxPlayers())
                .append(") ")
                .append(ChatColor.RESET);
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (int i = 0; i < onlinePlayers.size(); i++) {
            result.append(Players.getName(onlinePlayers.get(i))).append(ChatColor.RESET);
            if (i < onlinePlayers.size() - 1) result.append(", ");
        }
        sender.sendMessage(result.toString());
    }
}
