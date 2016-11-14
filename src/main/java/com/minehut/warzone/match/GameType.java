package com.minehut.warzone.match;

/**
 * Created by luke on 3/30/16.
 */
public enum GameType {
    DTW("Destroy the Wool"),
    TDM("Team Deathmatch"),
    CTW("Capture the Wool"),
    INFECTED("Infected"),
    ELIMINATION("Elimination"),
    BLITZ("Blitz");

    public String displayName;

    private GameType(String displayName) {
        this.displayName = displayName;
    }
}
