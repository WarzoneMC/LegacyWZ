package com.minehut.warzone.command;

import com.minehut.warzone.chat.LocalizedChatMessage;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.rotation.LoadedMap;
import com.minehut.warzone.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MapCommands {

    @Command(aliases = {"map", "mapinfo"}, desc = "Shows information about the currently playing map.", usage = "")
    public static void map(final CommandContext args, CommandSender sender) throws CommandException {
        LoadedMap mapInfo;
        if (args.argsLength() == 0) {
            mapInfo = GameHandler.getGameHandler().getMatch().getLoadedMap();
        } else {
            String search = "";
            for (int a = 0; a < args.argsLength(); a++) {
                search = search + args.getString(a) + " ";
            }
            mapInfo = GameHandler.getGameHandler().getRotation().getMap(search.trim());
            if (mapInfo == null) {
                throw new CommandException(ChatConstant.ERROR_NO_MAP_MATCH.getMessage(ChatUtil.getLocale(sender)));
            }
        }
        sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "----------" + ChatColor.DARK_AQUA + " " + mapInfo.getName() + ChatColor.RED + " " + ChatColor.STRIKETHROUGH + "----------");
        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + ChatConstant.UI_MAP_OBJECTIVE.getMessage(ChatUtil.getLocale(sender)) + ": " + ChatColor.RESET + "");
//        if (mapInfo.getAuthors().size() > 1) {
//            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + ChatConstant.UI_MAP_AUTHORS.getMessage(ChatUtil.getLocale(sender)) + ":");
//            for (Contributor contributor : mapInfo.getAuthors()) {
//                if (contributor.getContribution() != null) {
//                    sender.sendMessage("  " + contributor.getDisplayName() + ChatColor.GRAY + " - " + ChatColor.ITALIC + contributor.getContribution());
//                } else {
//                    sender.sendMessage("  " + contributor.getDisplayName());
//                }
//            }
//        } else {
//            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + ChatConstant.UI_MAP_AUTHOR.getMessage(ChatUtil.getLocale(sender)) + ": " + mapInfo.getAuthors().get(0).getDisplayName());
//        }
//        if (mapInfo.getContributors().size() > 0) {
//            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + ChatConstant.UI_MAP_CONTRIBUTORS.getMessage(ChatUtil.getLocale(sender)) + ":");
//            for (Contributor contributor : mapInfo.getContributors()) {
//                if (contributor.getContribution() != null) {
//                    sender.sendMessage("  " + contributor.getDisplayName() + ChatColor.GRAY + " - " + ChatColor.ITALIC + contributor.getContribution());
//                } else {
//                    sender.sendMessage("  " + contributor.getDisplayName());
//                }
//            }
//        }
//        if (mapInfo.getRules().size() > 0) {
//            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + ChatConstant.UI_MAP_RULES.getMessage(ChatUtil.getLocale(sender)) + ":");
//            for (int i = 1; i <= mapInfo.getRules().size(); i++) {
//                sender.sendMessage(ChatColor.WHITE + "" + i + ") " + ChatColor.GOLD + mapInfo.getRules().get(i - 1));
//            }
//        }
        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + ChatConstant.UI_MAP_MAX.getMessage(ChatUtil.getLocale(sender)) + ": " + ChatColor.RESET + "" + ChatColor.GOLD + mapInfo.getMaxPlayers());
    }

    @Command(aliases = {"next", "nextmap", "nm", "mn", "mapnext"}, desc = "Shows next map.", usage = "")
    public static void next(final CommandContext cmd, CommandSender sender) {
        LoadedMap next = GameHandler.getGameHandler().getCycle().getMap();
        sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_MAP_NEXT, ChatColor.GOLD + next.getName() + ChatColor.DARK_PURPLE + " " + ChatConstant.MISC_BY.getMessage(ChatUtil.getLocale(sender)) + " " + ChatColor.RED + next.getBuilder()).getMessage(ChatUtil.getLocale(sender)));
    }
}
