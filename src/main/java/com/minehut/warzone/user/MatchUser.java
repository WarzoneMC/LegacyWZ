package com.minehut.warzone.user;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by luke on 2/24/16.
 */
public class MatchUser {
    private UUID uuid;

    private int kills = 0;
    private int deaths = 0;
    private int longestSnipe = 0;
    private int coinsEarned = 0;
    private int xpEarned = 0;

    public MatchUser(Player player) {
        this.uuid = player.getUniqueId();
    }

    public void reset() {
        kills = 0;
        deaths = 0;
        longestSnipe = 0;
        xpEarned = 0;
        coinsEarned = 0;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getLongestSnipe() {
        return longestSnipe;
    }

    public void setLongestSnipe(int longestSnipe) {
        this.longestSnipe = longestSnipe;
    }

    public int getXpEarned() {
        return xpEarned;
    }

    public void setXpEarned(int xpEarned) {
        this.xpEarned = xpEarned;
    }

    public int getCoinsEarned() {
        return coinsEarned;
    }

    public void setCoinsEarned(int coinsEarned) {
        this.coinsEarned = coinsEarned;
    }

    public int addCoinsEarned(int coins) {
        this.coinsEarned += coins;
        return coinsEarned;
    }
}
