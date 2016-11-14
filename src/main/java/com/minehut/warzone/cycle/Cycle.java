package com.minehut.warzone.cycle;

import com.minehut.warzone.rotation.LoadedMap;
import com.minehut.warzone.GameHandler;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.util.UUID;

public class Cycle implements Runnable {

    private final UUID uuid;
    private final GameHandler handler;
    private LoadedMap map;

    public Cycle(LoadedMap map, UUID uuid, GameHandler handler) {
        this.map = map;
        this.uuid = uuid;
        this.handler = handler;
    }

    public LoadedMap getMap() {
        return map;
    }

    public void setMap(LoadedMap map) {
        this.map = map;
    }

    public UUID getUuid() {
        return uuid;
    }

    public GameHandler getGameHandler() {
        return handler;
    }

    @Override
    public void run() {
        GenerateMap.copyWorldFromRepository(map.getFolder(), uuid);
        World world = new WorldCreator("matches/" + uuid.toString()).generator(new NullChunkGenerator()).createWorld();
        world.setPVP(true);
        handler.setMatchWorld(world);
        handler.setMatchFile(new File("matches/" + uuid.toString() + "/"));
    }
}
