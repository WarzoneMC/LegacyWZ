package com.minehut.warzone.module.modules.blood;

import com.minehut.warzone.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class Blood implements Module {

    protected Blood() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBloodShow(EntityDamageEvent event) {
//        if (!event.isCancelled() && Settings.getSettingByName("Blood") != null) {
//            for (Player player : Bukkit.getOnlinePlayers()) {
//                if (Settings.getSettingByName("Blood").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
//                    player.playEffect(event.getEntity().getLocation(), event instanceof EntityDamageByEntityEvent ? Effect.STEP_SOUND : Effect.STEP_SOUND, Material.REDSTONE_WIRE);
//                }
//            }
//        }
    }

}
