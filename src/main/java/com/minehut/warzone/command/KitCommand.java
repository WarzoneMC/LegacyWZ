package com.minehut.warzone.command;

import com.minehut.warzone.Warzone;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand {

    @Command(aliases = {"kit", "kits"}, desc = "Select and purchase kits.")
    public static void say(final CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = (Player) sender;

        Warzone.getInstance().getKitManager().openKitMenu(player);
    }
}