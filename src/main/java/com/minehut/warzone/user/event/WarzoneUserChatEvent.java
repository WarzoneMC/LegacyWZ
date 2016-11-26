package com.minehut.warzone.user.event;

import com.minehut.warzone.module.modules.snowflakes.Snowflakes;
import com.minehut.warzone.util.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarzoneUserChatEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private String message;
    private String prefix;
    private boolean muted = false;

    public WarzoneUserChatEvent(Player player, String message) {
        this.player = player;
        this.message = message;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
