package com.minehut.warzone.module.modules;

import com.minehut.warzone.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class NoRegenModule implements Module {

    public NoRegenModule() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onGameHealthRegen(EntityRegainHealthEvent event) {
        if(event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN
                || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN
                || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.CUSTOM) {
            return;
        }
        event.setCancelled(true);
    }

}
