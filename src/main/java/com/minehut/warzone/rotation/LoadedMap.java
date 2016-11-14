package com.minehut.warzone.rotation;

import java.io.File;

public class LoadedMap {

    private final String name, builder;
    private final int maxPlayers;
    private final File folder;

    /**
     * @param name    The name of the map
     * @param builder The builder of the map
     * @param folder  The folder where the map can be found
     */
    public LoadedMap(String name, String builder, int maxPlayers, File folder) {
        this.name = name;
        this.builder = builder;
        this.maxPlayers = maxPlayers;
        this.folder = folder;
    }

    /**
     * @return Returns the name of the map
     */
    public String getName() {
        return name;
    }

    public String getBuilder() {
        return builder;
    }

    /**
     * @return Returns the maximum number of players for the map
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @return Returns the folder where the map can be found
     */
    public File getFolder() {
        return folder;
    }
}
