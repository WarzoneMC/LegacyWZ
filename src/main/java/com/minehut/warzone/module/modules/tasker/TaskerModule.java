package com.minehut.warzone.module.modules.tasker;

import com.minehut.warzone.Warzone;
import com.minehut.warzone.event.CycleCompleteEvent;
import com.minehut.warzone.match.Match;
import com.minehut.warzone.module.TaskedModule;
import com.minehut.warzone.GameHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class TaskerModule implements TaskedModule {

    private final Match match;

    protected TaskerModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        if (match.equals(GameHandler.getGameHandler().getMatch())) {
            for (TaskedModule task : match.getModules().getModules(TaskedModule.class)) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Warzone.getInstance(), task, 1);
            }
        }
    }

    @EventHandler
    public void onCycleComplete(CycleCompleteEvent event) {
        run();
    }
}
