package com.minehut.warzone.module.modules.sound;

import com.minehut.warzone.event.MatchEndEvent;
import com.minehut.warzone.module.Module;
import com.minehut.warzone.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SoundModule implements Module {

    protected SoundModule() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Teams.getTeamByPlayer(player) == event.getTeam()) {
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
            } else {
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!Teams.isObserver(event.getEntity())) {
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 0.9f, .75f);
        }
    }

}
