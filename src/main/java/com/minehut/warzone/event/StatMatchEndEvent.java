package com.minehut.warzone.event;

import com.minehut.cloud.bukkit.util.MongoPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class StatMatchEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private ArrayList<MongoPlayer> winners = new ArrayList<>();
    private ArrayList<MongoPlayer> losers = new ArrayList<>();

    private boolean cancelWinStats = false;
    private boolean cancelLoseStats = false;

    private MatchEndEvent matchEndEvent;

    public StatMatchEndEvent(MatchEndEvent matchEndEvent) {
        this.matchEndEvent = matchEndEvent;
    }

    public ArrayList<MongoPlayer> getWinners() {
        return winners;
    }

    public void setWinners(ArrayList<MongoPlayer> winners) {
        this.winners = winners;
    }

    public ArrayList<MongoPlayer> getLosers() {
        return losers;
    }

    public void setLosers(ArrayList<MongoPlayer> losers) {
        this.losers = losers;
    }

    public boolean isCancelWinStats() {
        return cancelWinStats;
    }

    public void setCancelWinStats(boolean cancelWinStats) {
        this.cancelWinStats = cancelWinStats;
    }

    public boolean isCancelLoseStats() {
        return cancelLoseStats;
    }

    public void setCancelLoseStats(boolean cancelLoseStats) {
        this.cancelLoseStats = cancelLoseStats;
    }

    public MatchEndEvent getMatchEndEvent() {
        return matchEndEvent;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
