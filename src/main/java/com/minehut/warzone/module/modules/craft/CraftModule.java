package com.minehut.warzone.module.modules.craft;

import com.minehut.warzone.module.Module;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftModule implements Module {

    protected CraftModule() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        switch (event.getRecipe().getResult().getType()) {
            case SHIELD:
            case FLINT_AND_STEEL:
            case SHEARS:
                event.setCancelled(true);
                event.getWhoClicked().sendMessage(ChatColor.RED + "You are unable to craft this com.minehut.tabbed.item.");
                break;
            default: break;
        }
    }

}
