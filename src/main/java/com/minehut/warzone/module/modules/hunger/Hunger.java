package com.minehut.warzone.module.modules.hunger;

import com.minehut.warzone.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Hunger implements Module {

    private final boolean hungerEnabled;

    protected Hunger(boolean hungerEnabled) {
        this.hungerEnabled = hungerEnabled;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onHungerDepletion(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

}
