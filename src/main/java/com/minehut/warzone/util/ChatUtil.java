package com.minehut.warzone.util;

import com.minehut.warzone.chat.ChatMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class ChatUtil {

    public static BaseComponent baseComponentFromArray(BaseComponent[] array) {
        BaseComponent result = new TextComponent("");
        for (BaseComponent component : array) {
            result.addExtra(component);
        }
        return result;
    }

    public static void sendWarningMessage(Player player, String msg) {
        if (msg != null) player.sendMessage(ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + msg);
    }

    public static void sendLocalizedMessage(ChatMessage message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message.getMessage(player.spigot().getLocale()));
        }
        Bukkit.getServer().getConsoleSender().sendMessage(message.getMessage(message.getMessage(Locale.getDefault().toString())));
    }

    public static void sendWarningMessage(Player player, ChatMessage msg) {
        if (msg != null)
            player.sendMessage(ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + msg.getMessage(player.spigot().getLocale()));
    }

    public static String getWarningMessage(String msg) {
        if (msg == null) return null;
        else return ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + ChatColor.translateAlternateColorCodes('`', msg);
    }

    public static String getLocale(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).spigot().getLocale() : Locale.getDefault().toString();
    }

    public enum ChannelType {
        GLOBAL, ADMIN, TEAM
    }

    public static ChatColor getTimerColor(double time) {
        if (time <= 5) {
            return ChatColor.DARK_RED;
        } else if (time <= 30) {
            return ChatColor.GOLD;
        } else if (time <= 60) {
            return ChatColor.YELLOW;
        } else {
            return ChatColor.GREEN;
        }
    }

}
