package com.minehut.warzone.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.chat.LocalizedChatMessage;
import com.minehut.warzone.rotation.LoadedMap;
import com.minehut.warzone.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RotationCommands {

    @Command(aliases = {"rotation", "rot", "maps", "maplist", "rota", "maprot", "maprotation", }, desc = "Shows the current rotation.", usage = "[page]")
    public static void rotation(final CommandContext cmd, CommandSender sender) throws CommandException {
        int index = cmd.argsLength() == 0 ? 1 : cmd.getInteger(0);
        List<LoadedMap> rot = GameHandler.getGameHandler().getRotation().getRotation();
        int pages = (int) Math.ceil((rot.size() + 7) / 8);
        if (index > pages) {
            throw new CommandException("Invalid page number specified! Maximum page number is " + pages + ".");
        }
        sender.sendMessage(ChatColor.RED + "------------- " + ChatColor.WHITE + new LocalizedChatMessage(ChatConstant.UI_ROTATION_CURRENT).getMessage(ChatUtil.getLocale(sender)) + ChatColor.DARK_AQUA + " (" + ChatColor.AQUA + index + ChatColor.DARK_AQUA + " of " + ChatColor.AQUA + pages + ChatColor.DARK_AQUA + ") " + ChatColor.RED + "-------------");
        String[] maps = {"", "", "", "", "", "", "", ""};
        for (int i = 0; i <= maps.length - 1; i++) {
            int position = 8 * (index - 1) + i;
            try {
                LoadedMap mapInfo = rot.get(position);
                maps[i] = maps[i] + ChatColor.GOLD + mapInfo.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_BY).getMessage(ChatUtil.getLocale(sender)) + " " + ChatColor.RED + mapInfo.getBuilder();

                if (GameHandler.getGameHandler().getRotation().getNextIndex() == position) {
                    maps[i] = ChatColor.DARK_AQUA + "" + (position + 1) + ". " + maps[i];
                } else {
                    maps[i] = ChatColor.WHITE + "" + (position + 1) + ". " + maps[i];
                }

            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        for (String map : maps) {
            if (!map.equalsIgnoreCase("")) {
                sender.sendMessage(map);
            }
        }
    }

    @Command(aliases = {"maprepo"}, desc = "Shows all currently loaded maps.", usage = "[page]")
    public static void maps(final CommandContext cmd, CommandSender sender) throws CommandException {
        int index;
        try {
            index = cmd.getInteger(0);
        } catch (IndexOutOfBoundsException ex) {
            index = 1;
        }
        List<LoadedMap> loadedList = GameHandler.getGameHandler().getRotation().getLoaded();
        List<String> mapNames = new ArrayList<>();
        for (LoadedMap map : loadedList) {
            mapNames.add(map.getName());
        }
        Collections.sort(mapNames);
        List<LoadedMap> ordered = new ArrayList<>();
        for (String map : mapNames) {
            for (LoadedMap loadedMap : loadedList) {
                if (loadedMap.getName().equals(map)) {
                    ordered.add(loadedMap);
                    break;
                }
            }
        }
        int pages = (int) Math.ceil((loadedList.size() + 7) / 8);
        if (index > pages)
            throw new CommandException("Invalid page number specified! Maximum page number is " + pages + ".");
        sender.sendMessage(ChatColor.RED + "--------------- " + ChatColor.WHITE + new LocalizedChatMessage(ChatConstant.UI_MAPLOADED).getMessage(ChatUtil.getLocale(sender)) + ChatColor.DARK_AQUA + " (" + ChatColor.AQUA + index + ChatColor.DARK_AQUA + " of " + ChatColor.AQUA + pages + ChatColor.DARK_AQUA + ") " + ChatColor.RED + "---------------");
        String[] maps = {"", "", "", "", "", "", "", ""};
        for (int i = 0; i <= maps.length - 1; i++) {
            int position = 8 * (index - 1) + i;
            if (position < ordered.size()) {
                LoadedMap mapInfo = ordered.get(position);

                maps[i] = maps[i] + ChatColor.GOLD + mapInfo.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_BY).getMessage(ChatUtil.getLocale(sender)) + " " + ChatColor.RED + mapInfo.getBuilder();
                maps[i] = ChatColor.WHITE + "" + (position + 1) + ". " + maps[i];
            }
        }
        for (String map : maps) {
            if (!map.equalsIgnoreCase("")) {
                sender.sendMessage(map);
            }
        }
    }

}
