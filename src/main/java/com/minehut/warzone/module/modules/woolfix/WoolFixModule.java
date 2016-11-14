package com.minehut.warzone.module.modules.woolfix;

import com.minehut.warzone.module.Module;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;

public class WoolFixModule implements Module {

    private World matchworld;

    protected WoolFixModule() {

    }

    @EventHandler
    public void onWoolCraft(CraftItemEvent event) {
        if (event.getRecipe().getResult().getType() == Material.WOOL) {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(ChatColor.RED + "You are now allowed to craft wool in this gamemode.");
        }
    }

    @Override
    public void unload() {

    }


}
