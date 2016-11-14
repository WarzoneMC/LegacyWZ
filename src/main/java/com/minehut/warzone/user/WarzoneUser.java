package com.minehut.warzone.user;

import com.minehut.cloud.core.Cloud;
import com.minehut.warzone.Warzone;
import com.mongodb.BasicDBObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 2/24/16.
 */
public class WarzoneUser {
    private Player player;

    private boolean initialized = false;

    private String selectedKit = "";
    private String activeKit = "";
    private List<String> kits = new ArrayList<>();

    //stats
    private int totalKills = 0;
    private int totalDeaths = 0;
    private int totalMatches = 0;
    private int totalWins = 0;
    private int totalLosses = 0;
    private int longestSnipe = 0;

    private int xp = 0;

    public WarzoneUser(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public int getLongestSnipe() {
        return longestSnipe;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
    }

    public void setLongestSnipe(int longestSnipe) {
        this.longestSnipe = longestSnipe;
    }

    public int getXp() {
        return xp;
    }

    public String getLevelString() {
        int level = this.getLevel();

        if (level < 10) {
            return ChatColor.GRAY + "[" + level + "]";
        }
        else if (level < 20) {
            return ChatColor.BLUE + "[" + level + "]";
        }
        else if (level < 30) {
            return ChatColor.DARK_GREEN + "[" + level + "]";
        }
        else if (level < 40) {
            return ChatColor.GREEN + "[" + level + "]";
        } else {
            return ChatColor.RED + "[" + level + "]";
        }
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return (int) (0.6 * Math.sqrt(this.getXp())) + 1;
    }

    public String getSelectedKit() {
        return selectedKit;
    }

    public void setSelectedKit(String selectedKit) {
        this.selectedKit = selectedKit;
    }

    public List<String> getKits() {
        return kits;
    }

    public void setKits(List<String> kits) {
        this.kits = kits;
    }

    public String getActiveKit() {
        return activeKit;
    }

    public void setActiveKit(String activeKit) {
        this.activeKit = activeKit;
    }

}
