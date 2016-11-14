package com.minehut.warzone.module.modules.motd;

import com.minehut.warzone.match.Match;
import com.minehut.warzone.Warzone;
import com.minehut.warzone.module.Module;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.ServerListPingEvent;

public class MOTD implements Module {

    private final Match match;

    protected MOTD(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerListPing(ServerListPingEvent event) {
        String name = match.getLoadedMap().getName();
        ChatColor color = ChatColor.GRAY;
        switch (match.getState()) {
            case ENDED:
                color = ChatColor.AQUA;
                break;
            case PLAYING:
                color = ChatColor.GOLD;
                break;
            case STARTING:
                color = ChatColor.GREEN;
                break;
        }
        ChatColor messageColor = ChatColor.valueOf(
                Warzone.getInstance().getConfig().getString("motd-message-color").toUpperCase().trim().replaceAll(" ", "_"));
        if (messageColor == null) messageColor = ChatColor.DARK_GRAY;
        if (Warzone.getInstance().getConfig().getBoolean("motd-message")) {
            event.setMotd(color + "\u00BB " + ChatColor.AQUA + name + color + " \u00AB" + '\n'
                    + messageColor + Warzone.getInstance().getConfig().getString("server-message"));
        } else {
            event.setMotd(color + "\u00BB " + ChatColor.AQUA + name + color + " \u00AB");
        }

    }
}
