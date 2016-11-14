package com.minehut.warzone.user;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by luke on 10/18/15.
 */
public class WarzoneUserJoinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private WarzoneUser warzoneUser;

    public WarzoneUserJoinEvent(Player player, WarzoneUser warzoneUser) {
        this.player = player;
        this.warzoneUser = warzoneUser;
    }

    public Player getPlayer() {
        return player;
    }

    public WarzoneUser getWarzoneUser() {
        return warzoneUser;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }




}
