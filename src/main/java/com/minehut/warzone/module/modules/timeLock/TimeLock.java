package com.minehut.warzone.module.modules.timeLock;

import com.minehut.warzone.GameHandler;
import com.minehut.warzone.module.Module;
import org.bukkit.World;

public class TimeLock implements Module {

    private World matchworld;

    protected TimeLock() {
        matchworld = GameHandler.getGameHandler().getMatchWorld();
        matchworld.setGameRuleValue("doDaylightCycle", "false");
    }

    @Override
    public void unload() {

    }


}
