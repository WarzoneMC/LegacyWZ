package com.minehut.warzone.module.modules.mobSpawn;

import com.minehut.warzone.GameHandler;
import com.minehut.warzone.module.Module;

public class MobSpawnModule implements Module {

    protected MobSpawnModule() {
        GameHandler.getGameHandler().getMatchWorld().setGameRuleValue("doMobSpawning", "false");
        GameHandler.getGameHandler().getMatchWorld().setWeatherDuration(0);
        GameHandler.getGameHandler().getMatchWorld().setStorm(false);
        GameHandler.getGameHandler().getMatchWorld().setThundering(false);
    }

    @Override
    public void unload() {

    }


}
