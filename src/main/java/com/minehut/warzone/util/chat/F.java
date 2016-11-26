package com.minehut.warzone.util.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by luke on 12/23/15.
 */
public class F {

    public static void log(String msg) {
        System.out.print(msg);
    }

    public static void message(Player player, String... message) {
        for (String s : message) {
            player.sendMessage(s);
        }
    }

    public static void broadcast(String... message) {
        for (String s : message) {
            Bukkit.getServer().getOnlinePlayers().forEach(p -> p.sendMessage(s));
        }
    }

    public static void sendWarningMessage(Player player, String msg) {
        if (msg != null) player.sendMessage(ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + msg);
    }

    public static String getWarningMessage(String msg) {
        if (msg == null) return null;
        else return ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + ChatColor.translateAlternateColorCodes('`', msg);
    }

}
