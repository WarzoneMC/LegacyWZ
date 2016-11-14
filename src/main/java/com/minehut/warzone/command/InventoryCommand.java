package com.minehut.warzone.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.minehut.warzone.GameHandler;
import com.minehut.warzone.chat.ChatConstant;
import com.minehut.warzone.module.modules.observers.ObserverModule;
import com.minehut.warzone.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InventoryCommand {

    @Command(aliases = {"inventory", "inv", "invsee"}, desc = "Opens a player's inventory", min = 1, usage = "<player>")
    public static void inventory(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        Player target = Bukkit.getPlayer(cmd.getString(0));
        if (target == null) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_NOT_FOUND.getMessage(ChatUtil.getLocale(sender)));
        }
        GameHandler.getGameHandler().getMatch().getModules().getModule(ObserverModule.class).openInventory((Player)sender, target, true);
    }
}
