package com.minehut.warzone.command;

import com.minehut.warzone.GameHandler;
import com.minehut.warzone.module.modules.chat.ChatModule;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by luke on 8/16/16.
 */
public class ChatCommands {

    @Command(aliases = {"team", "t"}, desc = "Send a message to your team")
    public static void team(final CommandContext cmd, CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        ChatModule chatModule = GameHandler.getGameHandler().getMatch().getModules().getModule(ChatModule.class);
        if (cmd.argsLength() == 0) {
//            chatModule.setChannel(player, "team");
//            player.sendMessage(ChatColor.YELLOW + "Chat channel set to " + ChatColor.GOLD + "/team");
            player.sendMessage(ChatColor.RED + "Please include a message!");
        } else {
            chatModule.sendMessage(player, null, true, cmd.getJoinedStrings(0));
        }
    }

    @Command(aliases = {"all", "a"}, desc = "Send a message to the entire match")
    public static void all(final CommandContext cmd, CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        ChatModule chatModule = GameHandler.getGameHandler().getMatch().getModules().getModule(ChatModule.class);
        if (cmd.argsLength() == 0) {
//            chatModule.setChannel(player, "all");
//            player.sendMessage(ChatColor.YELLOW + "Chat channel set to " + ChatColor.GOLD + "/all");
            player.sendMessage(ChatColor.RED + "Please include a message!");
        } else {
            chatModule.sendMessage(player, ChatColor.GOLD + "/all", false, cmd.getJoinedStrings(0));
        }
    }
//
//    @Command(aliases = {"d", "defend"}, desc = "Tell your team to defend")
//    public static void d(final CommandContext cmd, CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            return;
//        }
//        Player player = (Player) sender;
//        ChatModule chatModule = GameHandler.getGameHandler().getMatch().getModules().getModule(ChatModule.class);
//        chatModule.sendMessage(player, ChatColor.DARK_PURPLE + "/d", true, "Defend, please!");
//    }
//
//    @Command(aliases = {"gj"}, desc = "Tell your team good job")
//    public static void gj(final CommandContext cmd, CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            return;
//        }
//        Player player = (Player) sender;
//        ChatModule chatModule = GameHandler.getGameHandler().getMatch().getModules().getModule(ChatModule.class);
//        chatModule.sendMessage(player, ChatColor.DARK_PURPLE + "/gj", true, "Good job!");
//    }
//
//    @Command(aliases = {"at", "attack"}, desc = "Tell your team to attack")
//    public static void a(final CommandContext cmd, CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            return;
//        }
//        Player player = (Player) sender;
//        ChatModule chatModule = GameHandler.getGameHandler().getMatch().getModules().getModule(ChatModule.class);
//        chatModule.sendMessage(player, ChatColor.DARK_PURPLE + "/at", true, "Attack!");
//    }
//
//    @Command(aliases = {"gg"}, desc = "Good game!")
//    public static void gg(final CommandContext cmd, CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            return;
//        }
//        Player player = (Player) sender;
//        ChatModule chatModule = GameHandler.getGameHandler().getMatch().getModules().getModule(ChatModule.class);
//        chatModule.sendMessage(player, ChatColor.GOLD + "/all " + ChatColor.DARK_PURPLE + "/gg", false, "Good game!");
//    }
}
