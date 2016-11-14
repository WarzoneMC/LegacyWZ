package com.minehut.warzone.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.minehut.warzone.GameHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class LoadMapsCommand {

    @Command(aliases = {"loadmaps", "loadmap"}, desc = "Load maps from rotation list.")
    @CommandPermissions("cardinal.loadmaps")
    public static void list(final CommandContext args, CommandSender sender) {
        try {
            GameHandler.getGameHandler().getRotation().refreshRepo();
            GameHandler.getGameHandler().getRotation().refreshRotation();
            sender.sendMessage(ChatColor.GREEN + "Refreshed map rotation.");
        }catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "Failed to load map rotation.");
        }
    }
}
