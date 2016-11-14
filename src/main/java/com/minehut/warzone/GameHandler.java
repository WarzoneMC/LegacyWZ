package com.minehut.warzone;

import com.minehut.warzone.cycle.Cycle;
import com.minehut.warzone.event.CycleCompleteEvent;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.ModuleFactory;
import com.minehut.warzone.rotation.Rotation;
import com.minehut.warzone.rotation.exception.RotationLoadException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.UUID;

public class GameHandler {

    private static GameHandler handler;
    private final ModuleFactory moduleFactory;
    private Rotation rotation;
    private WeakReference<World> matchWorld;
    private Match match;
    private Cycle cycle;
    private File matchFile;
    private boolean globalMute;

    public GameHandler() throws RotationLoadException {
        handler = this;
        this.moduleFactory = new ModuleFactory();
        rotation = new Rotation();
        cycle = new Cycle(rotation.getNext(), UUID.randomUUID(), this);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Warzone.getInstance(), new Runnable() {
            @Override
            public void run() {
                cycleAndMakeMatch();
            }
        });

    }

    public static GameHandler getGameHandler() {
        return handler;
    }

    public void cycleAndMakeMatch() {
        if (rotation.getNext().equals(cycle.getMap())) {
            rotation.move();
        }
        World oldMatchWorld = matchWorld == null ? null : matchWorld.get();
        cycle.run();
        if (match != null) match.unregisterModules();
        this.match = new Match(cycle.getUuid(), cycle.getMap());
        this.match.registerModules();
        Warzone.getInstance().getLogger().info(this.match.getModules().size() + " modules loaded.");
        Bukkit.getServer().getPluginManager().callEvent(new CycleCompleteEvent(match));
        cycle = new Cycle(rotation.getNext(), UUID.randomUUID(), this);
        Bukkit.unloadWorld(oldMatchWorld, true);
    }

    public Rotation getRotation() {
        return rotation;
    }

    public World getMatchWorld() {
        return matchWorld.get();
    }

    public void setMatchWorld(World world) {
        matchWorld = new WeakReference<>(world);
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public JavaPlugin getPlugin() {
        return Warzone.getInstance();
    }

    public ModuleFactory getModuleFactory() {
        return moduleFactory;
    }

    public File getMatchFile() {
        return matchFile;
    }

    public void setMatchFile(File file) {
        matchFile = file;
    }

    public boolean getGlobalMute() {
        return globalMute;
    }

    public void setGlobalMute(boolean globalMute) {
        this.globalMute = globalMute;
    }

    public boolean toggleGlobalMute() {
        globalMute = !globalMute;
        return globalMute;
    }
}
